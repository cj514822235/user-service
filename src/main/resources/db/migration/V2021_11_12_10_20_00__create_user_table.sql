CREATE TABLE `user_table` (
  `id`          int(11)     NOT NULL AUTO_INCREMENT ,
  `user_id`     varchar(36) NOT NULL PRIMARY KEY,
  `status`      boolean     NOT NULL ,
  `role`        varchar(36) NOT NULL ,
  `age`         int         NOT NULL ,
  `name`        varchar(36) NOT NULL,
  `cellphone`   varchar(36) NOT NULL ,
  `address`     varchar(36) NOT NULL ,
  `email`       varchar(36) NOT NULL,
  `created_at`      datetime    NOT NULL,
  `updated_at`      datetime    NOT NULL,
  `version`         int(11)     NOT NULL,
   INDEX(id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;



