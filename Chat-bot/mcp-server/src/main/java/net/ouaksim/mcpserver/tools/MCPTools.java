package net.ouaksim.mcpserver.tools;

import org.springframework.ai.mcp.annotation.McpArg;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MCPTools {

    @McpTool(
            name = "getCustomer",
            description = "Get information about a bank customer"
    )
    public Customer getCustomer(
            @McpArg(description = "The customer name") String name
    ) {
        return new Customer(name, "MAD102030", 15000.0, "ACTIVE");
    }

    @McpTool(description = "Get all bank customers")
    public List<Customer> getAllCustomers() {

        return List.of(
                new Customer("Hassan", "MAD1001", 25000.0, "ACTIVE"),
                new Customer("Mohamed", "MAD1002", 7800.0, "SUSPENDED"),
                new Customer("Imane", "MAD1003", 54000.0, "ACTIVE")
        );
    }
}

record Customer(
        String name,
        String accountNumber,
        double balance,
        String status
) {}