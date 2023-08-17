package com.banco.models.dto;

import com.banco.models.Product;
import com.banco.models.enums.CardStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class CardDto {
    private String cardId;
    private String cardHolderName;
    private Date expiryDate;
    private CardStatus status;
    private double balance;
    private Product product;
}