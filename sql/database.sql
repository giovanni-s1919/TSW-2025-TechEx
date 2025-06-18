CREATE DATABASE IF NOT EXISTS TechEx;
USE TechEx;

CREATE TABLE IF NOT EXISTS User(
    ID              INT AUTO_INCREMENT PRIMARY KEY,
    Username        VARCHAR(255) NOT NULL ,
    Email           VARCHAR(255) NOT NULL,
    Password        VARCHAR(255) NOT NULL,
    is_admin        BOOLEAN NOT NULL
);



