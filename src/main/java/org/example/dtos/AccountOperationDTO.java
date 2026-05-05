package org.example.dtos;

import lombok.Data;
import org.example.enums.OperationType;

import java.time.LocalDateTime;

@Data
public class AccountOperationDTO {
    private Long id;
    private LocalDateTime operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
