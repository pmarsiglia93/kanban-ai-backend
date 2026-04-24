CREATE TABLE tasks (
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(255)  NOT NULL,
    description TEXT,
    deadline   DATE,
    priority   VARCHAR(50),
    status     VARCHAR(50)   NOT NULL DEFAULT 'TODO'
);

CREATE TABLE task_labels (
    task_id BIGINT       NOT NULL,
    label   VARCHAR(255),
    CONSTRAINT fk_task_labels FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);
