package org.example.dtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AccountHistoryDTO {
    private UUID accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO>
            accountOperationDTOS;
}
