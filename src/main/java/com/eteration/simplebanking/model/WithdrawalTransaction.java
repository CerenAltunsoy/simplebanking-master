package com.eteration.simplebanking.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WithdrawalTransaction extends Transaction {
	 @Id
	 @GeneratedValue
	 private Long id;

    public WithdrawalTransaction(double amount) {
        super(amount);
    }
    
    public WithdrawalTransaction() {
        super(0);
    }
    
    @Override
    public void executeOn(Account account) throws InsufficientBalanceException {
        if (account.getBalance() < this.getAmount()) {
            throw new InsufficientBalanceException();
        }

        account.setBalance(account.getBalance() - this.getAmount());
        this.setApprovalCode(UUID.randomUUID().toString());
    }

}
