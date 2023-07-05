package com.sg.bankaccountkata.mapper;

import com.googlecode.jmapper.JMapper;
import com.sg.bankaccountkata.dto.AccountView;
import com.sg.bankaccountkata.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountView convert(Account account) {
        JMapper<AccountView, Account> userMapper= new JMapper<>(
                AccountView.class, Account.class);
        return userMapper.getDestination(account);
    }
}
