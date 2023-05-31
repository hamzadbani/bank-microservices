package com.microservices.loans.service;

import com.microservices.loans.entity.Loans;
import com.microservices.loans.exception.UserNotFoundException;
import com.microservices.loans.repository.LoansRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LoansService {

    private final LoansRepository loansRepository;

    public LoansService(LoansRepository loansRepository) {
        this.loansRepository = loansRepository;
    }

    public List<Loans> getLoansDetails(int customerId){
        try {
            return loansRepository.findByCustomerIdOrderByStartDtDesc(customerId);
        } catch (UserNotFoundException e) {
            log.error("[getLoansDetails] Exception occur while getting loans by customer id");
            throw new UserNotFoundException("loans not found by id: " + customerId);
        }
    }
}
