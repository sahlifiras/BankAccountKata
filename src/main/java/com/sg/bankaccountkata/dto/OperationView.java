package com.sg.bankaccountkata.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OperationView {
    private String type;
    private BigDecimal amount;
    private BigDecimal balance;
    private LocalDateTime date;
}
