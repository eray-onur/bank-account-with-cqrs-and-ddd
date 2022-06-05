package com.techbank.account.query.application.queries;

import com.techbank.account.query.application.dto.EqualityType;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountQueryHandler implements QueryHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> bankAccounts = accountRepository.findAll();
        List<BaseEntity> bankAccountsList = new ArrayList<BaseEntity>();
        bankAccounts.forEach(bankAccountsList::add);
        return bankAccountsList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountsByIdQuery query) {
        var bankAccount = accountRepository.findById(query.getId());

        if(bankAccount.isEmpty())
            return null;

        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());

        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        var bankAccount = accountRepository.findByAccountHolder(query.getAccountHolder());

        if(bankAccount.isEmpty())
            return null;

        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());

        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountsWithBalanceQuery query) {
        List<BaseEntity> bankAccountsList =
                query.getEqualityType() == EqualityType.GREATER_THAN
                ? accountRepository.findByBalanceGreaterThan(query.getBalance())
                        : accountRepository.findByBalanceLessThan(query.getBalance());

        return bankAccountsList;

    }
}
