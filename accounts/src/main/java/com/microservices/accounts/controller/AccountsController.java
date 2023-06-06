package com.microservices.accounts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservices.accounts.config.AccountsServiceConfig;
import com.microservices.accounts.entity.Accounts;
import com.microservices.accounts.entity.Customer;
import com.microservices.accounts.entityDTO.Properties;
import com.microservices.accounts.response.CustomerDetails;
import com.microservices.accounts.service.AccountsService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class AccountsController {

    private final AccountsService accountsService;
    private final AccountsServiceConfig accountsServiceConfig;

    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);
    public AccountsController(AccountsService accountsService, AccountsServiceConfig accountsServiceConfig) {
        this.accountsService = accountsService;
        this.accountsServiceConfig = accountsServiceConfig;
    }

    @PostMapping("/myAccount")
    @Timed(value = "getAccountDetails.time", description = "Time taken to return Account Details")
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
    //@CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
    @Retry(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
    public ResponseEntity<CustomerDetails> getCustomerDetails(
            @RequestHeader("microservices-correlation-id") String correlationId,
            @RequestBody Customer customer) {
        logger.info("getCustomerDetails() method started");
        CustomerDetails customerDetails = accountsService.getCustomerDetails(correlationId, customer);
        logger.info("getCustomerDetails() method ended");
        return ResponseEntity.status(HttpStatus.OK).body(customerDetails);
    }
    private ResponseEntity<CustomerDetails> myCustomerDetailsFallBack(String correlationId, Customer customer, Throwable t) {
        CustomerDetails customerDetails = accountsService.getCustomerDetailsFallBack(correlationId, customer);
        return ResponseEntity.status(HttpStatus.OK).body(customerDetails);
    }

    @GetMapping("/sayHello")
    @RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
    public String sayHello() {
        return "Hello, Welcome to microservices app";
    }

    private String sayHelloFallback(Throwable t) {
        return "Hi, Welcome to microservices app";
    }
}
