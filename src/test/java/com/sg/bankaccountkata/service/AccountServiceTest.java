package com.sg.bankaccountkata.service;

import com.sg.bankaccountkata.dto.OperationRequest;
import com.sg.bankaccountkata.model.Account;
import com.sg.bankaccountkata.model.Operation;
import com.sg.bankaccountkata.model.OperationType;
import com.sg.bankaccountkata.repository.AccountRepository;
import com.sg.bankaccountkata.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    public void getAccountShouldReturnException() {
        when(accountRepository.findByAccountNumber("123")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> accountService.getAccount("123"));
    }
    @Test
    public void getAccountStatementPrinting() {
        Operation op1 = new Operation(OperationType.DEPOSIT, new BigDecimal("1000"), new BigDecimal("1000"), LocalDateTime.now().minusMonths(4));
        Operation op2 = new Operation(OperationType.DEPOSIT, new BigDecimal("1000"), new BigDecimal("1000"), LocalDateTime.now().minusDays(70));
        Operation op3 = new Operation(OperationType.DEPOSIT, new BigDecimal("1000"), new BigDecimal("1000"), LocalDateTime.now().minusMonths(1));
        List<Operation> operatons = new ArrayList<>();
        operatons.add(op1);
        operatons.add(op2);
        operatons.add(op3);
        Account ac = new Account("123");
        ac.setOperations(operatons);
        when(accountRepository.findByAccountNumber(ac.getAccountNumber())).thenReturn(ac);
        Account a = accountService.getStatementPrinting(ac.getAccountNumber(), 3);
        assertEquals(2, a.getOperations().size());
    }

    @Test
    public void getBalanceShouldReturnNewAmount() {
        String accountNumber = "123";
        OperationRequest op = new OperationRequest("123", OperationType.DEPOSIT, new BigDecimal("1"));
        Account account = new Account(accountNumber);
        BigDecimal newBalance = accountService.getBalance(op, account);
        assertEquals(newBalance, new BigDecimal(1));
    }

    @Test
    public void getBalanceShouldThrowError() {
        String accountNumber = "123";
        OperationRequest op = new OperationRequest("123", OperationType.WITHDRAW, new BigDecimal("1"));
        Account account = new Account(accountNumber);
        assertThrows(IllegalArgumentException.class, () -> accountService.getBalance(op, account));
    }

    @Test
    public void getBalanceShouldReturnTheDifference() {
        String accountNumber = "123";
        OperationRequest op = new OperationRequest("123", OperationType.WITHDRAW, new BigDecimal("100"));
        Account account = new Account(accountNumber);
        account.setBalance(new BigDecimal("1000"));
        BigDecimal newBalance = accountService.getBalance(op, account);
        assertEquals(newBalance, new BigDecimal("900"));
    }

    @Test
    public void testDeposit() {
        String accountNumber = "1234567890";
        BigDecimal initialBalance = new BigDecimal("1000.00");
        BigDecimal depositAmount = new BigDecimal("500.00");
        BigDecimal expectedBalance = initialBalance.add(depositAmount);

        Account account = new Account(accountNumber);
        account.setBalance(initialBalance);
        OperationRequest op  = new OperationRequest(accountNumber, OperationType.DEPOSIT, depositAmount);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.doOperation(op);

        assertEquals(expectedBalance, account.getBalance());
        assertEquals(1, account.getOperations().size());

        Operation operation = account.getOperations().get(0);
        assertEquals(OperationType.DEPOSIT, operation.getType());
        assertEquals(depositAmount, operation.getAmount());
        assertEquals(expectedBalance, operation.getBalance());
        assertNotNull(operation.getDate());

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testWithdrawSufficientFunds() {
        String accountNumber = "1234567890";
        BigDecimal initialBalance = new BigDecimal("1000.00");
        BigDecimal withdrawalAmount = new BigDecimal("500.00");
        BigDecimal expectedBalance = initialBalance.subtract(withdrawalAmount);

        Account account = new Account(accountNumber);
        account.setBalance(initialBalance);
        OperationRequest op  = new OperationRequest(accountNumber, OperationType.WITHDRAW, withdrawalAmount);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.doOperation(op);

        assertEquals(expectedBalance, account.getBalance());
        assertEquals(1, account.getOperations().size());

        Operation operation = account.getOperations().get(0);
        assertEquals(OperationType.WITHDRAW, operation.getType());
        assertEquals(withdrawalAmount.negate(), operation.getAmount());
        assertEquals(expectedBalance, operation.getBalance());
        assertNotNull(operation.getDate());

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        String accountNumber = "1234567890";
        BigDecimal initialBalance = new BigDecimal("500.00");
        BigDecimal withdrawalAmount = new BigDecimal("1000.00");

        Account account = new Account(accountNumber);
        account.setBalance(initialBalance);
        OperationRequest op  = new OperationRequest(accountNumber, OperationType.WITHDRAW, withdrawalAmount);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account);

        assertThrows(IllegalArgumentException.class,
                () -> accountService.doOperation(op));

        assertEquals(initialBalance, account.getBalance());
        assertEquals(0, account.getOperations().size());

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountRepository, never()).save(any(Account.class));
    }
}
