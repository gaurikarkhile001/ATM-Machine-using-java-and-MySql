create database ATM;
use ATM;
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(255) NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    contact_number VARCHAR(15),
    email VARCHAR(255),
    FOREIGN KEY (account_name) REFERENCES accounts(account_name)
);

CREATE TABLE IF NOT EXISTS accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL,
    pin VARCHAR(4) NOT NULL
);

-- Create an index on the account_name column in the accounts table
CREATE INDEX idx_account_name ON accounts(account_name);

-- Create the transaction_history table with the foreign key constraint
CREATE TABLE IF NOT EXISTS transaction_history (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(255) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_name) REFERENCES accounts(account_name)
);
CREATE TABLE IF NOT EXISTS deposit_transactions (
    deposit_id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_name) REFERENCES accounts(account_name)
);
select * from deposit_transactions;
CREATE TABLE IF NOT EXISTS withdrawal_transactions (
    withdrawal_id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_name) REFERENCES accounts(account_name)
);

CREATE TABLE IF NOT EXISTS transfer_transactions (
    transfer_id INT AUTO_INCREMENT PRIMARY KEY,
    from_account_name VARCHAR(255) NOT NULL,
    to_account_name VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_account_name) REFERENCES accounts(account_name),
    FOREIGN KEY (to_account_name) REFERENCES accounts(account_name)
);

CREATE TABLE IF NOT EXISTS loan_accounts (
    loan_id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(255) NOT NULL,
    loan_amount DECIMAL(10, 2) NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL,
    tenure INT NOT NULL,
    start_date DATE,
    FOREIGN KEY (account_name) REFERENCES accounts(account_name)
);

select * from transaction_history;
select * from accounts;
select * from transfer_transactions;
select * from deposit_transactions;
select * from withdrawal_transactions;