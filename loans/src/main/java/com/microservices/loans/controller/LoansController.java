package com.microservices.loans.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.microservices.loans.config.LoansServiceConfig;
import com.microservices.loans.entity.Customer;
import com.microservices.loans.entity.Loans;
import com.microservices.loans.entityDTO.Properties;
import com.microservices.loans.service.LoansService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoansController {

    private final LoansService loansService;
    private final LoansServiceConfig loansServiceConfig;
    private static final Logger logger = LoggerFactory.getLogger(LoansController.class);

    public LoansController(LoansService loansService, LoansServiceConfig loansServiceConfig) {
        this.loansService = loansService;
        this.loansServiceConfig = loansServiceConfig;
    }

    @PostMapping("/myLoans")
    public ResponseEntity<List<Loans>> getLoansDetails(
            @RequestHeader("microservices-correlation-id") String correlationId,
            @RequestBody Customer customer) {
        logger.info("getLoansDetails() method started");
        List<Loans> loans = loansService.getLoansDetails(customer.getCustomerId());
        logger.info("getLoansDetails() method ended");
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }
    @GetMapping("/loans/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(loansServiceConfig.getMsg(), loansServiceConfig.getBuildVersion(),
                loansServiceConfig.getMailDetails(), loansServiceConfig.getActiveBranches());
        return ow.writeValueAsString(properties);
    }
}
