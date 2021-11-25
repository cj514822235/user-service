CREATE TABLE `task_table`
(
    `id`        int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `level`     ENUM('EASY', 'MEDIUM', 'HARD', 'HELL') NOT NULL DEFAULT 'EASY',
    `status`    boolean NOT NULL,
    `description`  varchar(36) NOT NULL,
    `user_id`      varchar(36) NOT NULL,
    `created_at`      datetime    NOT NULL,
    `updated_at`      datetime    NOT NULL,
    `version`         int(11)     NOT NULL,
    CONSTRAINT user_id_tasks FOREIGN KEY (`user_Id`) REFERENCES `user_table` (`user_id`)

)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;



