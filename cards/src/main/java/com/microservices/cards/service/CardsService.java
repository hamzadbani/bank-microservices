package com.microservices.cards.service;

import com.microservices.cards.entity.Cards;
import com.microservices.cards.exception.UserNotFoundException;
import com.microservices.cards.repository.CardsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CardsService {

    private final CardsRepository cardsRepository;

    public CardsService(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    public List<Cards> getCardDetails(int customerId){
        log.info("fd");
        try {
            return cardsRepository.findByCustomerId(customerId);
        } catch (UserNotFoundException e) {
            log.error("[getCardDetails] Exception occur while getting cards by customer id");
            throw new UserNotFoundException("cards not found by id: " + customerId);
        }
    }
}
