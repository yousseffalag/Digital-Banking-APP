package org.example.mappers;

import org.example.dtos.*;
import org.example.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    // CUSTOMER MAPPING
    public CustomerDTO fromCustomer(Customer customer) {
        if (customer == null) return null;

        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(customer, dto);
        return dto;
    }

    public Customer fromCustomerDTO(CustomerDTO dto) {
        if (dto == null) return null;

        Customer customer = new Customer();
        BeanUtils.copyProperties(dto, customer);
        return customer;
    }

    // SAVING ACCOUNT MAPPING
    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount account) {
        if (account == null) return null;

        SavingBankAccountDTO dto = new SavingBankAccountDTO();
        BeanUtils.copyProperties(account, dto);

        dto.setCustomerDTO(fromCustomer(account.getCustomer()));
        dto.setType(account.getClass().getSimpleName());

        return dto;
    }

    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO dto) {
        if (dto == null) return null;

        SavingAccount account = new SavingAccount();
        BeanUtils.copyProperties(dto, account);

        account.setCustomer(fromCustomerDTO(dto.getCustomerDTO()));

        return account;
    }

    // CURRENT ACCOUNT MAPPING
    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount account) {
        if (account == null) return null;

        CurrentBankAccountDTO dto = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(account, dto);

        dto.setCustomerDTO(fromCustomer(account.getCustomer()));
        dto.setType(account.getClass().getSimpleName());

        return dto;
    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO dto) {
        if (dto == null) return null;

        CurrentAccount account = new CurrentAccount();
        BeanUtils.copyProperties(dto, account);

        account.setCustomer(fromCustomerDTO(dto.getCustomerDTO()));

        return account;
    }

    // OPERATION MAPPING
    public AccountOperationDTO fromAccountOperation(Operation operation) {
        if (operation == null) return null;

        AccountOperationDTO dto = new AccountOperationDTO();
        BeanUtils.copyProperties(operation, dto);

        return dto;
    }
}
