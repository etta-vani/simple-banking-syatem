-- schema.sql
-- Run this in MySQL before running the Java app.

CREATE DATABASE IF NOT EXISTS banking_system;
USE banking_system;

CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    balance        DOUBLE NOT NULL DEFAULT 0,
    account_type   VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    type           VARCHAR(30) NOT NULL,
    amount         DOUBLE NOT NULL,
    txn_date       TIMESTAMP NOT NULL,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);
