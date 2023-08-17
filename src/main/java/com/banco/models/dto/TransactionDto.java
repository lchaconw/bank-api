package com.banco.models.dto;

import com.banco.models.Card;
import com.banco.models.enums.TransactionStatus;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionDto {
    private Long id;
    private Long transactionId;
    private String cardId;
    private double price;
    private TransactionStatus status;
    private Date transactionDate;
}
