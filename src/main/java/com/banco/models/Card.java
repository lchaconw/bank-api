package com.banco.models;

import com.banco.models.enums.CardStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @Column(name = "cardId", nullable = false, unique = true)
    private String cardId;

    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Column(name = "balance", nullable = false)
    private double balance;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}