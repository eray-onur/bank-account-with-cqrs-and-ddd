package com.techbank.account.query.application.controllers;

import com.techbank.account.query.application.dto.AccountLookupResponse;
import com.techbank.account.query.application.dto.EqualityType;
import com.techbank.account.query.application.queries.FindAccountByHolderQuery;
import com.techbank.account.query.application.queries.FindAccountsByIdQuery;
import com.techbank.account.query.application.queries.FindAccountsWithBalanceQuery;
import com.techbank.account.query.application.queries.FindAllAccountsQuery;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
import com.techbank.cqrs.core.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/bankAccountLookup")
public class AccountLookupController {
    private final Logger logger = Logger.getLogger(AccountLookupController.class.getName());

    @Autowired
    private QueryDispatcher queryDispatcher;

    @GetMapping(path = "/")
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountsQuery());
            if(accounts == null || accounts.size() == 0)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully retrieved {0} bank account(s).", accounts.size()))
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            var safeErrorMessage = "Failed to retrieve all accounts.";
            logger.log(Level.SEVERE, safeErrorMessage, ex);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/byId/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountsById(@PathVariable(value = "id") String id) {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountsByIdQuery(id));
            if(accounts == null || accounts.size() == 0)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully retrieved bank account.")
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            var safeErrorMessage = "Failed to retrieve accounts by ID.";
            logger.log(Level.SEVERE, safeErrorMessage, ex);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/byHolder/{accountHolder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@PathVariable(value = "accountHolder") String accountHolder) {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByHolderQuery(accountHolder));
            if(accounts == null || accounts.size() == 0)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully retrieved bank account.")
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            var safeErrorMessage = "Failed to retrieve accounts by holder.";
            logger.log(Level.SEVERE, safeErrorMessage, ex);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/withBalance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountsWithBalance(
            @PathVariable(value = "equalityType") EqualityType equalityType,
            @PathVariable(value = "balance") double balance) {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountsWithBalanceQuery(equalityType, balance));
            if(accounts == null || accounts.size() == 0)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully retrieved {0} bank account(s) by balance.", accounts.size()))
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            var safeErrorMessage = "Failed to retrieve account by balance.";
            logger.log(Level.SEVERE, safeErrorMessage, ex);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
