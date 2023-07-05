package com.sg.bankaccountkata.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<Operation> operations;

    public Account() {
        this.operations = new ArrayList<>();
    }

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
        this.operations = new ArrayList<>();
    }
}
