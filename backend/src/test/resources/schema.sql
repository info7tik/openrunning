DROP TABLE IF EXISTS records;
CREATE TABLE `records` (
  `distance_m` int NOT NULL,
  `timestamp` bigint NOT NULL,
  `user_id` int NOT NULL,
  `first_point_index` int DEFAULT NULL,
  `last_point_index` int DEFAULT NULL,
  `time_s` bigint DEFAULT NULL,
  PRIMARY KEY (`distance_m`,`timestamp`,`user_id`)
);
