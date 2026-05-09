package org.example.web;

import lombok.AllArgsConstructor;
import org.example.repositories.BankAccountRepository;
import org.example.repositories.CustomerRepository;
import org.example.repositories.OperationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class StatisticsRestController {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private OperationRepository operationRepository;

    @GetMapping("/statistics")
    @org.springframework.security.access.prepost.PreAuthorize("hasAuthority('SCOPE_USER')")
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCustomers", customerRepository.count());
        stats.put("totalAccounts", bankAccountRepository.count());
        stats.put("totalOperations", operationRepository.count());
        
        double totalBalance = bankAccountRepository.findAll().stream()
                .mapToDouble(acc -> acc.getBalance())
                .sum();
        stats.put("totalBalance", totalBalance);
        
        stats.put("recentOperations", operationRepository.findAll().stream()
                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                .limit(5)
                .map(op -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", op.getId());
                    m.put("amount", op.getAmount());
                    m.put("type", op.getType());
                    m.put("date", op.getDate());
                    m.put("account", op.getBankAccount().getId());
                    return m;
                })
                .toList());
                
        return stats;
    }
}
