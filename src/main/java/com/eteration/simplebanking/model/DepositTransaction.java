package com.eteration.simplebanking.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DepositTransaction extends Transaction {

	@Id
    @GeneratedValue
    private Long id;
	
    public DepositTransaction(double amount) {
        super(amount);
    }
    
    public DepositTransaction() {
        super(0);
    }

 
    @Override
    public void executeOn(Account account) {
        account.setBalance(account.getBalance() + this.getAmount());
        this.setApprovalCode(UUID.randomUUID().toString());
    }
}
