package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.InsufficientBalanceException;
import com.eteration.simplebanking.model.Transaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/account/v1")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Optional<Account> account = accountService.findAccount(accountNumber);
        if(account.isPresent()){
            return ResponseEntity.ok(account.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<TransactionStatus> credit(@PathVariable String accountNumber, @RequestBody DepositTransaction transaction) {
        Optional<Account> account = accountService.findAccount(accountNumber);
        if(account.isPresent()){
            account.get().deposit(transaction.getAmount());
            accountService.saveAccount(account.get());
            return ResponseEntity.ok(new TransactionStatus("OK", transaction.getApprovalCode()));
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<TransactionStatus> debit(@PathVariable String accountNumber, @RequestBody WithdrawalTransaction transaction) {
        Optional<Account> account = accountService.findAccount(accountNumber);
        if(account.isPresent()){
            try { 
                account.get().withdraw(transaction.getAmount());
                accountService.saveAccount(account.get());
                return ResponseEntity.ok(new TransactionStatus("OK", transaction.getApprovalCode()));
            } catch (InsufficientBalanceException e) {
            	throw new InsufficientBalanceException(); 
                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TransactionStatus("FAIL", "Insufficient balance"));
            }
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    
}

