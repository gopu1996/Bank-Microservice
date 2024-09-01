package com.bank.cards.service.impl;

import com.bank.cards.constant.CardsConstatns;
import com.bank.cards.dto.CardsDto;
import com.bank.cards.entity.Cards;
import com.bank.cards.exception.CardAlreadyExistsException;
import com.bank.cards.exception.ResourceNotFoundException;
import com.bank.cards.mapper.CardsMapper;
import com.bank.cards.repository.CardsRepository;
import com.bank.cards.service.CardsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class CardsServiceImpl implements CardsServices {

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private HealthEndpoint healthEndpoint;

    @Override
    public boolean createCards(String mobileNumber) {

       Optional<Cards> optionalCards = cardsRepository.findByMobileNumber(mobileNumber);
       if (optionalCards.isPresent()){
           throw new CardAlreadyExistsException("Card already registered with given mobileNumber "+mobileNumber);
       }
        cardsRepository.save(createNewCard(mobileNumber));
       return true;
    }

    @Override
    public CardsDto fetchCardsDetails(String mobileNumber) {

      Cards cards =  cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(()->
                new ResourceNotFoundException("Cards" , "MobileNumber ", mobileNumber));

        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    @Override
    public boolean updateCards(CardsDto cardsDto, String cardNumber) {

        Cards cards =  cardsRepository.findByCardNumber(cardNumber).orElseThrow(()->
                new ResourceNotFoundException("Cards" , "CardNumber ", cardNumber));


        CardsMapper.mapToCards(cardsDto,cards);
        cards.setCardNumber(cardNumber);
        cards.setAvailableAmount(cardsDto.getTotalLimit() - cardsDto.getAmountUsed());
        cardsRepository.save(cards);
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {

        Cards cards =  cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(()->
                new ResourceNotFoundException("Cards" , "MobileNumber ", mobileNumber));
        cardsRepository.deleteById(cards.getCardId());
        return true;
    }


    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstatns.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstatns.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstatns.NEW_CARD_LIMIT);
        return newCard;
    }

    @Override
    public boolean getHealthCheck() {
        String isUp = String.valueOf(healthEndpoint.health().getStatus());
        if (isUp.equalsIgnoreCase("UP")){
            return true;
        } else {
            return false;
        }
    }
}
