package com.sg.bankaccountkata.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperationView {
    private String type;
    private BigDecimal amount;
    private BigDecimal balance;
    private LocalDateTime date;
}
