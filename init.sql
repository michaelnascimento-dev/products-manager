create database pm_appdb;

use pm_appdb;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt9ajreq5lrb0b89vhnrpq7kcs` (`user_id`),
  CONSTRAINT `FKt9ajreq5lrb0b89vhnrpq7kcs` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);