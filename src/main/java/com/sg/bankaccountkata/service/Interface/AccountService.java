package com.sg.bankaccountkata.service.Interface;

import com.sg.bankaccountkata.dto.OperationRequest;
import com.sg.bankaccountkata.model.Account;

public interface AccountService {
    Account getAccount(String accountNumber);

    Account getStatementPrinting(String accountNumber, int monthNumbers);
    void doOperation(OperationRequest operation);
}
