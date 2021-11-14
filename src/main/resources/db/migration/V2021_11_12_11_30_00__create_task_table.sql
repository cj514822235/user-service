CREATE TABLE `task_table`
(
    `id`        int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`    int         NOT NULL,
    `status`    enum ('EASY', 'MEDIUM', 'HARD', 'HELL'),

    `description`  varchar(36) NOT NULL,
  CONSTRAINT user_id_fk_task FOREIGN KEY (user_id) REFERENCES user_table(id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;



