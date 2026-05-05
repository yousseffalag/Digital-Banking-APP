package org.example.dtos;

import lombok.Data;
import org.example.enums.AccountStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SavingBankAccountDTO extends
        BankAccountDTO {
    private UUID id;
    private double balance;
    private LocalDateTime createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}