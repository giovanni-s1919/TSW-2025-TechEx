DROP DATABASE IF EXISTS TechEx;

CREATE DATABASE TechEx;
USE TechEx;

CREATE TABLE IF NOT EXISTS User(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    Username            VARCHAR(255) NOT NULL ,
    Email               VARCHAR(255) UNIQUE NOT NULL,
    Password            VARCHAR(255) NOT NULL,
    Role                ENUM ('Customer', 'Admin') DEFAULT 'Customer'
);

CREATE TABLE IF NOT EXISTS Address(
    ID                  INT AUTO_INCREMENT PRIMARY KEY,
    Street              TEXT NOT NULL,
    AdditionalInfo      TEXT,
    City                VARCHAR(100) NOT NULL,
    PostalCode          VARCHAR(20) NOT NULL,
    Region              VARCHAR(100),
    Country             VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS UserAddress(
    AddressID           INT,
    UserID              INT,
    AddressType         ENUM('Shipping', 'Billing') NOT NULL,
    Name                VARCHAR(255) NOT NULL,
    Surname             VARCHAR(255) NOT NULL,
    Phone               VARCHAR(20) NOT NULL,
    FOREIGN KEY (AddressID) REFERENCES Address(ID),
    FOREIGN KEY (UserID) REFERENCES User(ID),
    PRIMARY KEY (AddressID, UserID, AddressType)
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

