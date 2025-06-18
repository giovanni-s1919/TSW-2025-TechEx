CREATE DATABASE IF NOT EXISTS TechEx;
USE TechEx;

CREATE TABLE IF NOT EXISTS User(
    ID              INT AUTO_INCREMENT PRIMARY KEY,
    Username        VARCHAR(255) NOT NULL ,
    Email           VARCHAR(255) NOT NULL,
    Password        VARCHAR(255) NOT NULL,
    is_admin        BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS Product(
    ID              INT AUTO_INCREMENT PRIMARY KEY,
    Name            VARCHAR(255) NOT NULL,
    Description     TEXT,
    Brand           VARCHAR(255) NOT NULL,
    Price           DECIMAL(10, 2) NOT NULL,        -- prezzo senza IVA
    Type            VARCHAR(255) NOT NULL,
    Quantity        INT NOT NULL DEFAULT 0 CHECK (Quantity >= 0),
    VAT             DECIMAL(5, 2) DEFAULT 22.00 NOT NULL          -- es. 22.00 -> 22%
);

