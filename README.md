# **Bank account kata**

## Requirements
- Deposit and Withdrawal
- Account statement (date, amount, balance)
- Statement printing

## User Stories
### US 1:
In order to save money As a bank client I want to make a deposit in my account
### US 2:
In order to retrieve some or all of my savings As a bank client I want to make a withdrawal from my account
### US 3:
In order to check my operations As a bank client I want to see the history (operation, date, amount, balance) of my operations

## Solution
The implementation of a simplified bank account management, the Account class is ready to be used by front-end services or apis but cannot be exposed to the user directly. The validation of the amounts of the different operations must be done before the call of the Account operations.

## Usage
mvn test

## Solution

Exposed REST API with the following endpoints

| feature            | Description                                             | verb | url                                 | request body                                             |
|--------------------|---------------------------------------------------------|------|-------------------------------------|----------------------------------------------------------|
| Deposit/Withdraw   | Do operations of Deposit and withdraw                   | POST | /accounts                           | { "accountNumber": 123, amount": 10, "type": "DEPOSIT" } |
| Account Operations | Get the informations of the account and its operations  | GET  | /accounts/{accountId}               | NA                                                       |
| Statement printing | Get Statement printing of the last {monthNumber} months | GET  | /accounts/{accountId}/{monthNumber} | NA                                                       |

