package com.microservices.accounts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservices.accounts.config.AccountsServiceConfig;
import com.microservices.accounts.entity.Accounts;
import com.microservices.accounts.entity.Customer;
import com.microservices.accounts.entityDTO.Properties;
import com.microservices.accounts.response.CustomerDetails;
import com.microservices.accounts.service.AccountsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class AccountsController {

    private final AccountsService accountsService;
    private final AccountsServiceConfig accountsServiceConfig;

    public AccountsController(AccountsService accountsService, AccountsServiceConfig accountsServiceConfig) {
        this.accountsService = accountsService;
        this.accountsServiceConfig = accountsServiceConfig;
    }

    @PostMapping("/myAccount")
    public ResponseEntity<Accounts> getAccountDetails(@RequestBody Customer customer) {
        Accounts account = accountsService.getAccountDetails(customer.getCustomerId());
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @GetMapping("/account/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(accountsServiceConfig.getMsg(), accountsServiceConfig.getBuildVersion(),
                accountsServiceConfig.getMailDetails(), accountsServiceConfig.getActiveBranches());
        return ow.writeValueAsString(properties);
    }

    @PostMapping("/customer/details")
    public ResponseEntity<CustomerDetails> getCustomerDetails(@RequestBody Customer customer) {
        CustomerDetails customerDetails = accountsService.getCustomerDetails(customer);
        return ResponseEntity.status(HttpStatus.OK).body(customerDetails);
    }
}
