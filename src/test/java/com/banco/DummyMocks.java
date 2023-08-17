package com.banco;

import com.banco.models.Card;
import com.banco.models.Product;
import com.banco.models.Transaction;
import com.banco.models.enums.CardStatus;
import com.banco.models.enums.TransactionStatus;

import java.util.Date;

public final class DummyMocks {
    public static Card getCardInactive() {
        Card card = new Card();
        card.setCardId("1254783156215411");
        card.setBalance(0);
        card.setCardHolderName("Pedro Gonzalez");
        card.setStatus(CardStatus.INACTIVE);
        card.setExpiryDate(new Date());
        card.setProduct(getProduct());

        return card;
    }

    public static Card getCardActive() {
        Card card = new Card();
        card.setCardId("1254783156215411");
        card.setBalance(100);
        card.setCardHolderName("Pedro Gonzalez");
        card.setStatus(CardStatus.ACTIVE);
        card.setExpiryDate(new Date(System.currentTimeMillis() + 86400000L));
        card.setProduct(getProduct());

        return card;
    }

    public static Card getCardActiveWithBalance() {
        Card card = new Card();
        card.setCardId("1254783156215411");
        card.setBalance(100);
        card.setCardHolderName("Pedro Gonzalez");
        card.setStatus(CardStatus.ACTIVE);
        card.setExpiryDate(new Date(System.currentTimeMillis() + 86400000L));
        card.setProduct(getProduct());

        return card;
    }

    public static Card getCardExpired() {
        Card card = new Card();
        card.setCardId("1254783156215411");
        card.setBalance(100);
        card.setCardHolderName("Pedro Gonzalez");
        card.setStatus(CardStatus.ACTIVE);
        card.setExpiryDate(new Date(System.currentTimeMillis() - 86400000L));
        card.setProduct(getProduct());

        return card;
    }

    public static Card getCardBlocked() {
        Card card = new Card();
        card.setCardId("1254783156215411");
        card.setBalance(0);
        card.setCardHolderName("Pedro Gonzalez");
        card.setStatus(CardStatus.BLOCKED);
        card.setExpiryDate(new Date());
        card.setProduct(getProduct());

        return card;
    }

    public static Product getProduct() {
        Product product = new Product();
        product.setProductId(125478L);
        product.setName("Credit Card");
        return product;
    }

    public static Transaction getTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCard(getCardActive());
        transaction.setPrice(100L);
        transaction.setTransactionDate(new Date());
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transaction;
    }

    public static Transaction getTransactionWithoutMount() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCard(getCardActive());
        transaction.setPrice(0L);
        transaction.setTransactionDate(new Date());
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transaction;
    }

    public static Transaction getTransactionExpired() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCard(getCardActive());
        transaction.setPrice(100L);
        transaction.setTransactionDate(new Date(System.currentTimeMillis() - 25 * (60 * 60 * 1000)));
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transaction;
    }

    public static Transaction getTransactionAnulated() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCard(getCardActive());
        transaction.setPrice(100L);
        transaction.setTransactionDate(new Date());
        transaction.setStatus(TransactionStatus.ANULATED);
        return transaction;
    }

}
