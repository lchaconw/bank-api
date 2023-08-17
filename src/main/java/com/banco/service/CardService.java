package com.banco.service;

import com.banco.exceptions.CustomBadRequestException;
import com.banco.models.Card;
import com.banco.models.Product;
import com.banco.models.dto.CardDto;
import com.banco.models.enums.CardStatus;
import com.banco.repository.CardRepository;
import com.banco.repository.ProductRepository;
import com.banco.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class CardService {

    private static final Random RANDOM = new Random();

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Card generateCardNumber(Long productId, String name, String lastName) {
            Product product = productRepository.findById(productId).orElseThrow(() -> new CustomBadRequestException("Producto no encontrado"));
            String randomDigits = generateRandomNumber(10);
            Card card = new Card();
            card.setCardId(productId + randomDigits);
            card.setCardHolderName(name + " " + lastName);
            card.setStatus(CardStatus.INACTIVE);
            card.setBalance(0);
            card.setProduct(product);
            card.setExpiryDate(new Date(System.currentTimeMillis() + 94608000000L));
            cardRepository.save(card);
            return card;
    }

    public void enrollCard(CardDto card) {
        Card cardDB = cardRepository.findById(card.getCardId()).orElseThrow(() -> new CustomBadRequestException("Tarjeta no encontrada"));

        if (cardDB.getStatus().equals(CardStatus.ACTIVE)) {
            throw new CustomBadRequestException("Tarjeta ya ha sido activada");
        }

        cardDB.setStatus(CardStatus.ACTIVE);
        cardRepository.save(cardDB);
    }

    public String generateRandomNumber(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(RANDOM.nextInt(length));
        }
        return result.toString();
    }

    public Card blockCard(String cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CustomBadRequestException("Tarjeta no encontrada"));
        if (card.getStatus().equals(CardStatus.BLOCKED)) {
            throw new CustomBadRequestException("Tarjeta ya ha sido bloqueada previamente");
        }
        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }

    public Card rechargeBalance(CardDto card) {
        Card cardDb = cardRepository.findById(card.getCardId()).orElseThrow(() -> new CustomBadRequestException("Tarjeta no encontrada"));
        if (!cardDb.getStatus().equals(CardStatus.ACTIVE)) {
            throw new CustomBadRequestException("Tarjeta bloqueada o inactiva");
        }

        if (card.getBalance() <= 0) {
            throw new CustomBadRequestException("El saldo a recargar debe ser mayor a 0");
        }
        cardDb.setBalance(cardDb.getBalance() + card.getBalance());
        return cardRepository.save(cardDb);
    }

    public double getBalance(String cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CustomBadRequestException("Tarjeta no encontrada"));
        return card.getBalance();
    }

}