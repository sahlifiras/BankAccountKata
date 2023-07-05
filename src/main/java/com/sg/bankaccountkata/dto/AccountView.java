package com.sg.bankaccountkata.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class AccountView {
    private String accountNumber;
    private BigDecimal balance;
    private List<OperationView> operations;
}
