package com.kanban.ai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "GEMINI_API_KEY=test-key")
class KanbanAiBackendApplicationTests {

    @Test
    void contextLoads() {
    }
}
