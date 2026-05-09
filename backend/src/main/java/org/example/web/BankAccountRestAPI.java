package org.example.web;

import org.example.dtos.AccountHistoryDTO;
import org.example.dtos.AccountOperationDTO;
import org.example.dtos.BankAccountDTO;
import org.example.dtos.CurrentBankAccountDTO;
import org.example.dtos.SavingBankAccountDTO;
import org.example.exceptions.BankAccountNotFoundException;
import org.example.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountRestAPI {

    private BankAccountService bankAccountService;
    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }
    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name="page",defaultValue = "0") int page,
            @RequestParam(name="size",defaultValue = "5")int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }

    @GetMapping("/customers/{customerId}/accounts")
    public List<BankAccountDTO> getAccountsByCustomer(@PathVariable Long customerId){
        return bankAccountService.getAccountsByCustomer(customerId);
    }
    @PostMapping("/accounts/current")
    @org.springframework.security.access.prepost.PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CurrentBankAccountDTO saveCurrentAccount(@RequestBody java.util.Map<String, Object> data) throws org.example.exceptions.CustomerNotFoundException {
        double initialBalance = Double.parseDouble(data.get("initialBalance").toString());
        double overDraft = Double.parseDouble(data.get("overDraft").toString());
        Long customerId = Long.parseLong(data.get("customerId").toString());
        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
    }
    @PostMapping("/accounts/saving")
    @org.springframework.security.access.prepost.PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public SavingBankAccountDTO saveSavingAccount(@RequestBody java.util.Map<String, Object> data) throws org.example.exceptions.CustomerNotFoundException {
        double initialBalance = Double.parseDouble(data.get("initialBalance").toString());
        double interestRate = Double.parseDouble(data.get("interestRate").toString());
        Long customerId = Long.parseLong(data.get("customerId").toString());
        return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
    }

    @PutMapping("/accounts/{accountId}/status")
    @org.springframework.security.access.prepost.PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void changeAccountStatus(@PathVariable String accountId, @RequestParam(name = "status") String status) throws BankAccountNotFoundException {
        bankAccountService.updateAccountStatus(accountId, org.example.enums.AccountStatus.valueOf(status));
    }
}
