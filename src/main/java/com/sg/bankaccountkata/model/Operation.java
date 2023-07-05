package com.sg.bankaccountkata.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OperationType type;
    private BigDecimal amount;
    private BigDecimal balance;
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Operation(OperationType type, BigDecimal amount, BigDecimal balance, LocalDateTime date) {
        this.type = type;
        this.amount = amount;
        this.balance = balance;
        this.date = date;
    }
}
