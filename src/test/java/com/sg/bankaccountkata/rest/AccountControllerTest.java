package com.sg.bankaccountkata.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.bankaccountkata.controller.AccountController;
import com.sg.bankaccountkata.dto.OperationRequest;
import com.sg.bankaccountkata.mapper.AccountMapper;
import com.sg.bankaccountkata.model.OperationType;
import com.sg.bankaccountkata.service.Interface.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;
    @MockBean
    private AccountMapper accountMapper;

    @Test
    public void testDepositWithNegativeAmount() throws Exception {
        OperationRequest operationRequest = new OperationRequest("1234567890", OperationType.DEPOSIT, new BigDecimal("-500.00"));
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/doOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(operationRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(0)).doOperation(operationRequest);
    }

    @Test
    public void testDeposit() throws Exception {
        OperationRequest operationRequest = new OperationRequest("1234567890", OperationType.DEPOSIT, new BigDecimal("500.00"));
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/doOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(operationRequest)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).doOperation(operationRequest);
    }

    @Test
    public void testWithdraw() throws Exception {
        OperationRequest operationRequest = new OperationRequest("1234567890", OperationType.WITHDRAW, new BigDecimal("500.00"));

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/doOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(operationRequest)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).doOperation(operationRequest);
    }

    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}