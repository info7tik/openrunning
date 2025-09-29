use openrunning;

CREATE TABLE `gpxfiles` (
  `filename`    VARCHAR(255) NOT NULL,
  `user_id`     INT NOT NULL,
  `status`      VARCHAR(20) NOT NULL,

  PRIMARY KEY (`filename`)
);

CREATE TABLE `tracks` (
  `timestamp` BIGINT NOT NULL,
  `user_id` INT NOT NULL,
  `distance_m` INT NOT NULL,
  `filename` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `time_s` INT NOT NULL,

  PRIMARY KEY (`timestamp`,`user_id`)
);

CREATE TABLE `samples` (
  `number`      INT NOT NULL,
  `timestamp`   BIGINT NOT NULL,
  `user_id`     INT NOT NULL,
  `distance_m`  INT NOT NULL,
  `nb_points`   INT NOT NULL,
  `time_s`      INT NOT NULL,
  `unit`        TINYINT NOT NULL,

  PRIMARY KEY (`number`,`timestamp`,`user_id`)
);

CREATE TABLE `records` (
  `distance_m`          INT NOT NULL,
  `timestamp`           BIGINT NOT NULL,
  `user_id`             INT NOT NULL,
  `first_point_index`   INT NOT NULL,
  `last_point_index`    INT NOT NULL,
  `time_s`              INT NOT NULL,

  PRIMARY KEY (`distance_m`,`timestamp`,`user_id`)
);

CREATE TABLE `frequencies` (
  `frequency`   VARCHAR(20) NOT NULL,
  `timestamp`   BIGINT NOT NULL,
  `user_id`     INT NOT NULL,
  `distance_m`  INT NOT NULL,
  `time_s`      INT NOT NULL,

  PRIMARY KEY (`frequency`,`timestamp`,`user_id`)
);

CREATE TABLE `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,

  PRIMARY KEY (`id`)
);
