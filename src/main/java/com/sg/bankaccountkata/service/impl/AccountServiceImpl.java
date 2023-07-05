package com.sg.bankaccountkata.service.impl;

import com.sg.bankaccountkata.dto.OperationRequest;
import com.sg.bankaccountkata.model.Account;
import com.sg.bankaccountkata.model.Operation;
import com.sg.bankaccountkata.model.OperationType;
import com.sg.bankaccountkata.repository.AccountRepository;
import com.sg.bankaccountkata.service.Interface.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account;
    }

    @Override
    public Account getStatementPrinting(String accountNumber, int monthNumbers) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        account.setOperations(account.getOperations().stream().filter(elm -> elm.getDate().isAfter(LocalDateTime.now().minusMonths(monthNumbers))).collect(Collectors.toList()));
        return account;
    }

    @Override
    public void doOperation(OperationRequest operation) {
        Account account = getAccount(operation.getAccountNumber());

        account.setBalance(getBalance(operation, account));

        Operation transaction = new Operation(
                operation.getType(),
                getAmount(operation.getType(), operation.getAmount()),
                account.getBalance(),
                LocalDateTime.now()
        );
        account.addOperation(transaction);

        accountRepository.save(account);
    }

    public BigDecimal getBalance(OperationRequest operation, Account account) {
        BigDecimal newBalance;
        switch (operation.getType()) {
            case DEPOSIT: {
                newBalance = account.getBalance().add(operation.getAmount());
                break;
            }
            case WITHDRAW: {
                BigDecimal currentBalance = account.getBalance();
                if (operation.getAmount().compareTo(currentBalance) > 0) {
                    throw new IllegalArgumentException("Insufficient funds");
                }
                newBalance = currentBalance.subtract(operation.getAmount());
                break;
            }
            default:
                newBalance = account.getBalance();
                break;
        }
        return newBalance;
    }

    private BigDecimal getAmount(OperationType type, BigDecimal amount) {
        return type.equals(OperationType.DEPOSIT) ? amount : amount.negate();
    }
}
