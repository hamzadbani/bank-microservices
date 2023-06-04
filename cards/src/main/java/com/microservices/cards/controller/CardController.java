package com.microservices.cards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.microservices.cards.config.CardsServiceConfig;
import com.microservices.cards.entity.Cards;
import com.microservices.cards.entity.Customer;
import com.microservices.cards.entityDTO.Properties;
import com.microservices.cards.service.CardsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController {

    private final CardsService cardsService;
    private final CardsServiceConfig cardsServiceConfig;

    public CardController(CardsService cardsService, CardsServiceConfig cardsServiceConfig) {
        this.cardsService = cardsService;
        this.cardsServiceConfig = cardsServiceConfig;
    }

    @PostMapping("/myCards")
    public ResponseEntity<List<Cards>> getCardDetails(
            @RequestHeader("microservices-correlation-id") String correlationId,
            @RequestBody Customer customer) {

        List<Cards> cards = cardsService.getCardDetails(customer.getCustomerId());
        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    @GetMapping("/cards/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(cardsServiceConfig.getMsg(), cardsServiceConfig.getBuildVersion(),
                cardsServiceConfig.getMailDetails(), cardsServiceConfig.getActiveBranches());
        return ow.writeValueAsString(properties);
    }
}
