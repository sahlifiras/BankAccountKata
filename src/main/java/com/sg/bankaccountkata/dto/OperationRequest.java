package com.sg.bankaccountkata.dto;

import com.sg.bankaccountkata.model.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class OperationRequest {

    @NotNull(message = "should not be null")
    private String accountNumber;

    @NotNull(message = "should not be null")
    private OperationType type;

    @NotNull(message = "should not be null")
    @DecimalMin(message = "The amount of the operation must be positive and greater than 0.0", value = "0.0", inclusive = false)
    private BigDecimal amount;
}
