CREATE TABLE `user_table` (
  `id`          int(11)     NOT NULL PRIMARY KEY,
  `age`         int         NOT NULL ,
  `name`        varchar(36) NOT NULL,
  `cellphone`   varchar(36) NOT NULL ,
  `address`     varchar(36) NOT NULL ,
  `email`       varchar(36) NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


