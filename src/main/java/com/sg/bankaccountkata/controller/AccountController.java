package com.sg.bankaccountkata.controller;

import com.sg.bankaccountkata.dto.AccountView;
import com.sg.bankaccountkata.dto.OperationRequest;
import com.sg.bankaccountkata.mapper.AccountMapper;
import com.sg.bankaccountkata.model.Account;
import com.sg.bankaccountkata.service.Interface.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping("/doOperation")
    public ResponseEntity<String> deposit(@Valid @RequestBody OperationRequest operation) {
        accountService.doOperation(operation);
        return new ResponseEntity(operation.getType() + " : Successful", HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountView> getAccount(@PathVariable String accountNumber) {
        Account account = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(accountMapper.convert(account));
    }

    @GetMapping("/{accountNumber}/{monthNumber}")
    public ResponseEntity<AccountView> getAccount(@PathVariable String accountNumber, @PathVariable int monthNumber) {
        Account account = accountService.getStatementPrinting(accountNumber, monthNumber);
        return ResponseEntity.ok(accountMapper.convert(account));
    }
}
