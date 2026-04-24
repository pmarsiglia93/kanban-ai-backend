package com.kanban.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanban.ai.dto.PrioritySuggestion;
import com.kanban.ai.entity.Priority;
import com.kanban.ai.entity.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class AiPriorityService {

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";

    private static final String SYSTEM_PROMPT = """
            Você é um assistente especializado em gestão de projetos ágeis e kanban.
            Analise a tarefa fornecida e sugira a prioridade mais adequada.
            Responda SOMENTE com um JSON válido, sem markdown, sem código, sem texto adicional.
            Use exatamente este formato:
            {"priority":"HIGH","justification":"Motivo detalhado da priorização","confidenceScore":85}
            Valores válidos para priority: LOW, MEDIUM, HIGH, URGENT
            O confidenceScore deve ser um número inteiro entre 0 e 100.
            """;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api-key}")
    private String apiKey;

    public AiPriorityService(RestClient.Builder builder, ObjectMapper objectMapper) {
        this.restClient = builder.build();
        this.objectMapper = objectMapper;
    }

    public PrioritySuggestion suggestPriority(Task task) {
        String fullPrompt = SYSTEM_PROMPT + "\n\nTarefa a analisar:\n" + buildTaskDescription(task);

        var request = new GeminiRequest(
                List.of(new GeminiContent(List.of(new GeminiPart(fullPrompt))))
        );

        GeminiResponse response = restClient.post()
                .uri(GEMINI_URL + "?key=" + apiKey)
                .header("Content-Type", "application/json")
                .body(request)
                .retrieve()
                .body(GeminiResponse.class);

        String text = response.candidates().get(0).content().parts().get(0).text();
        text = text.replaceAll("(?s)```json\\s*", "").replaceAll("(?s)```\\s*", "").trim();

        try {
            AiResponse aiResponse = objectMapper.readValue(text, AiResponse.class);
            return new PrioritySuggestion(
                    Priority.valueOf(aiResponse.priority()),
                    aiResponse.justification(),
                    aiResponse.confidenceScore()
            );
        } catch (Exception e) {
            throw new RuntimeException("Falha ao interpretar resposta da IA: " + e.getMessage(), e);
        }
    }

    private String buildTaskDescription(Task task) {
        return """
                Título: %s
                Descrição: %s
                Prazo: %s
                Status atual: %s
                Labels: %s
                """.formatted(
                task.getTitle(),
                task.getDescription() != null ? task.getDescription() : "Não informada",
                task.getDeadline() != null ? task.getDeadline().toString() : "Não informado",
                task.getStatus(),
                task.getLabels().isEmpty() ? "Nenhuma" : String.join(", ", task.getLabels())
        );
    }

    // Records para comunicação com a API do Gemini
    private record GeminiPart(String text) {}
    private record GeminiContent(List<GeminiPart> parts) {}
    private record GeminiRequest(List<GeminiContent> contents) {}
    private record GeminiCandidate(GeminiContent content) {}
    private record GeminiResponse(List<GeminiCandidate> candidates) {}

    // Record para o JSON retornado pela IA
    private record AiResponse(String priority, String justification, int confidenceScore) {}
}
