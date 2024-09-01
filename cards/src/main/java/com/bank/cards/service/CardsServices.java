package com.bank.cards.service;

import com.bank.cards.dto.CardsDto;
import com.bank.cards.entity.Cards;
import jakarta.transaction.Transactional;

public interface CardsServices {

    boolean createCards(String mobileNumber);

    CardsDto fetchCardsDetails(String mobileNumber);

    boolean updateCards(CardsDto cardsDto, String cardNumber);
    @Transactional
    boolean deleteCard(String mobileNumber);

    boolean getHealthCheck();
}
