package com.sg.bankaccountkata.dto;

import java.math.BigDecimal;
import java.util.List;

public class AccountView {
    private String accountNumber;
    private BigDecimal balance;
    private List<OperationView> operations;
}
