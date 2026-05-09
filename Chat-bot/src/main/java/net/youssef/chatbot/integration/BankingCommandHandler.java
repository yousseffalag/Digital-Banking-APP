package net.youssef.chatbot.integration;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class BankingCommandHandler {

    private final BackendClient backendClient;

    public BankingCommandHandler(BackendClient backendClient) {
        this.backendClient = backendClient;
    }

    public String handle(String message) {
        if (message == null || message.isBlank()) {
            return helpText();
        }
        String trimmed = message.trim();
        String lower = trimmed.toLowerCase(Locale.ROOT);

        if (lower.equals("/help") || lower.equals("help")) {
            return helpText();
        }
        if (lower.equals("/accounts") || lower.equals("accounts")) {
            return listAccounts();
        }
        if (lower.startsWith("/account ") || lower.startsWith("account ")) {
            String accountId = trimmed.substring(trimmed.indexOf(' ') + 1).trim();
            return accountDetail(accountId);
        }
        if (lower.startsWith("/history ") || lower.startsWith("history ")) {
            String accountId = trimmed.substring(trimmed.indexOf(' ') + 1).trim();
            return accountHistory(accountId);
        }
        if (lower.equals("/customers") || lower.equals("customers")) {
            return listCustomers();
        }
        // Natural language: "what's the balance for [name]"
        if (lower.contains("balance") && lower.contains("for")) {
            String name = extractNameFromQuery(trimmed);
            if (name != null) {
                return balanceForCustomer(name);
            }
        }
        return null;
    }

    private String listAccounts() {
        try {
            List<BankAccountInfo> accounts = backendClient.listAccounts();
            if (accounts == null || accounts.isEmpty()) {
                return "No accounts were found for your profile.";
            }
            return accounts.stream()
                    .map(account -> String.format("• %s — id=%s — balance=%.2f", account.getType(), account.getId(), account.getBalance()))
                    .collect(Collectors.joining("\n"));
        } catch (Exception ex) {
            return backendErrorMessage(ex);
        }
    }

    private String accountDetail(String accountId) {
        if (accountId.isBlank()) {
            return "Please provide an account ID. Example: /account 12345";
        }
        try {
            BankAccountInfo account = backendClient.getAccount(accountId);
            if (account == null) {
                return "I could not find an account with ID " + accountId + ".";
            }
            StringBuilder builder = new StringBuilder();
            builder.append("Account details:\n");
            builder.append("ID: ").append(account.getId()).append("\n");
            builder.append("Type: ").append(account.getType()).append("\n");
            builder.append("Balance: ").append(String.format("%.2f", account.getBalance())).append("\n");
            if (account.getStatus() != null) {
                builder.append("Status: ").append(account.getStatus()).append("\n");
            }
            if (account.getOverDraft() > 0) {
                builder.append("Overdraft: ").append(String.format("%.2f", account.getOverDraft())).append("\n");
            }
            if (account.getInterestRate() > 0) {
                builder.append("Interest rate: ").append(String.format("%.2f%%", account.getInterestRate())).append("\n");
            }
            return builder.toString().trim();
        } catch (Exception ex) {
            return backendErrorMessage(ex);
        }
    }

    private String accountHistory(String accountId) {
        if (accountId.isBlank()) {
            return "Please provide an account ID. Example: /history 12345";
        }
        try {
            AccountHistoryInfo history = backendClient.getAccountHistory(accountId, 0, 5);
            if (history == null || history.getAccountOperationDTOS() == null || history.getAccountOperationDTOS().isEmpty()) {
                return "No operations were found for account " + accountId + ".";
            }
            String rows = history.getAccountOperationDTOS().stream()
                    .map(operation -> String.format("%s | %s | %.2f | %s",
                            operation.getOperationDate(),
                            operation.getType(),
                            operation.getAmount(),
                            operation.getDescription()))
                    .collect(Collectors.joining("\n"));
            return String.format("Account history for %s (%d/%d):\n%s",
                    history.getAccountId(),
                    history.getCurrentPage() + 1,
                    history.getTotalPages(),
                    rows);
        } catch (Exception ex) {
            return backendErrorMessage(ex);
        }
    }

    private String backendErrorMessage(Exception ex) {
        return "I could not reach the backend service right now. Please make sure the backend is running and reachable, then try again.\nError: " + ex.getMessage();
    }

    private String helpText() {
        return "Banking bot commands:\n"
                + "/accounts - list all accounts\n"
                + "/account <id> - show account details\n"
                + "/history <id> - show recent operations\n"
                + "/customers - list all customers\n"
                + "Or ask: 'what's the balance for [customer name]'";
    }

    private String listCustomers() {
        try {
            List<CustomerInfo> customers = backendClient.listCustomers();
            if (customers == null || customers.isEmpty()) {
                return "No customers were found.";
            }
            return customers.stream()
                    .map(customer -> String.format("• %s (ID: %d) - %s", customer.getName(), customer.getId(), customer.getEmail()))
                    .collect(Collectors.joining("\n"));
        } catch (Exception ex) {
            return backendErrorMessage(ex);
        }
    }

    private String balanceForCustomer(String name) {
        try {
            List<CustomerInfo> customers = backendClient.listCustomers();
            if (customers == null) {
                return "Could not retrieve customer data.";
            }
            CustomerInfo customer = customers.stream()
                    .filter(c -> c.getName() != null && c.getName().toLowerCase().contains(name.toLowerCase()))
                    .findFirst()
                    .orElse(null);
            if (customer == null) {
                return "I could not find a customer with name containing '" + name + "'.";
            }
            List<BankAccountInfo> accounts = backendClient.listAccounts();
            if (accounts == null) {
                return "Could not retrieve account data.";
            }
            String balances = accounts.stream()
                    .filter(account -> account.getId() != null && account.getId().startsWith(customer.getId().toString()))
                    .map(account -> String.format("%s: %.2f", account.getType(), account.getBalance()))
                    .collect(Collectors.joining(", "));
            if (balances.isEmpty()) {
                return customer.getName() + " has no accounts.";
            }
            return customer.getName() + "'s balances: " + balances;
        } catch (Exception ex) {
            return backendErrorMessage(ex);
        }
    }

    private String extractNameFromQuery(String query) {
        // Simple extraction: after "for"
        int forIndex = query.toLowerCase().indexOf("for");
        if (forIndex == -1) return null;
        String afterFor = query.substring(forIndex + 3).trim();
        // Take first word as name
        String[] words = afterFor.split("\\s+");
        return words.length > 0 ? words[0] : null;
    }
}
