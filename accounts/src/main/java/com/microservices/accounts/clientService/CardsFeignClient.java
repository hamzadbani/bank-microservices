package com.microservices.accounts.clientService;

import com.microservices.accounts.entity.Customer;
import com.microservices.accounts.entityDTO.Cards;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("cards")
public interface CardsFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "myCards", consumes = "application/json")
    List<Cards> getCardDetails(@RequestHeader("microservices-correlation-id") String correlationId,
                               @RequestBody Customer customer);
}
