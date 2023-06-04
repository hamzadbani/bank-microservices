package com.microservices.accounts.service;

import com.microservices.accounts.clientService.CardsFeignClient;
import com.microservices.accounts.clientService.LoansFeignClient;
import com.microservices.accounts.entity.Accounts;
import com.microservices.accounts.entity.Customer;
import com.microservices.accounts.entityDTO.Cards;
import com.microservices.accounts.entityDTO.Loans;
import com.microservices.accounts.exception.AccountsException;
import com.microservices.accounts.exception.UserNotFoundException;
import com.microservices.accounts.repository.AccountsRepository;
import com.microservices.accounts.response.CustomerDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import java.util.List;

@Slf4j
@Service
public class AccountsService {

    private final AccountsRepository accountsRepository;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;

    public AccountsService(AccountsRepository accountsRepository, CardsFeignClient cardsFeignClient, LoansFeignClient loansFeignClient) {
        this.accountsRepository = accountsRepository;
        this.cardsFeignClient = cardsFeignClient;
        this.loansFeignClient = loansFeignClient;
    }
    public Accounts getAccountDetails(int customerId) {
        try {
            return accountsRepository.findByCustomerId(customerId);
        } catch (UserNotFoundException e) {
            log.error("[getAccountDetails] Exception occur while getting account by customer id");
            throw new UserNotFoundException("account not found by id: " + customerId);
        }
    }

    public CustomerDetails getCustomerDetails(String correlationId, Customer customer) {
        try {
            Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
            List<Loans> loans = loansFeignClient.getLoansDetails(correlationId, customer);
            List<Cards> cards = cardsFeignClient.getCardDetails(correlationId, customer);

            return CustomerDetails.builder().
                    accounts(accounts).
                    cards(cards).
                    loans(loans).
                    build();
        } catch (AccountsException e) {
            log.error("[getCustomerDetails] Exception occur while getting customer details");
            throw new AccountsException("Exception occur while getting customer details, reason: " + e.getMessage());
        }
    }

    public CustomerDetails getCustomerDetailsFallBack(String correlationId, Customer customer) {
        try {
            Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
            List<Loans> loans = loansFeignClient.getLoansDetails(correlationId, customer);
            return CustomerDetails.builder().
                    accounts(accounts).
                    loans(loans).
                    build();
        } catch (AccountsException e) {
            log.error("[getCustomerDetailsFallBack] Exception occur while getting customer details fallback");
            throw new AccountsException("Exception occur while getting customer details fallback, reason: " + e.getMessage());
        }
    }
}
