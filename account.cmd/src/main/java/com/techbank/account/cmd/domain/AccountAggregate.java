package com.techbank.account.cmd.domain;

import com.techbank.account.cmd.application.commands.OpenAccountCommand;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean isActive;
    private double balance;

    public double getBalance() {
        return this.balance;
    }
    public Boolean getIsActive() { return this.isActive;}

    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(AccountOpenedEvent.builder()
                        .id(command.getId())
                        .accountHolder(command.getAccountHolder())
                        .createdDate(new Date())
                        .accountType(command.getAccountType())
                        .openingBalance(command.getOpeningBalance())
                        .build());
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.isActive = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFunds(double amount) {
        if(!isActive) {
            throw new IllegalStateException("Funds cannot be deposited into a closed bank account.");
        }

        if(amount <= 0) {
            throw new IllegalStateException("Deposited amount must be greater than zero.");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id).amount(amount).build());
    }

    public void apply(FundsDepositedEvent event) {
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    public void withdrawFunds(double amount) {
        if(!isActive) {
            throw new IllegalStateException("Funds cannot be withdrawn from a closed bank account.");
        }
        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsWithdrawnEvent event) {
        this.id = event.getId();
        this.balance = event.getAmount();
    }

    public void closeAccount() {
        if(!isActive) {
            throw new IllegalStateException("Bank account has only been closed.");
        }

        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    public void apply(AccountClosedEvent event) {
        this.id = event.getId();
        isActive = false;
    }
}
