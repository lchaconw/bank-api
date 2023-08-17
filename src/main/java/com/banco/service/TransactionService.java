package com.banco.service;

import com.banco.exceptions.CustomBadRequestException;
import com.banco.models.Card;
import com.banco.models.Transaction;
import com.banco.models.dto.TransactionDto;
import com.banco.models.enums.CardStatus;
import com.banco.models.enums.TransactionStatus;
import com.banco.repository.CardRepository;
import com.banco.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction purchase(TransactionDto transaction) {
        try {
            Card card = cardRepository.findById(transaction.getCardId()).orElseThrow(() -> new CustomBadRequestException("Tarjeta no encontrada"));
            if (!card.getStatus().equals(CardStatus.ACTIVE)) {
                throw new CustomBadRequestException("Transacción no permitida tarjeta no se encuentra activa");
            }
            if (transaction.getPrice() <= 0) {
                throw new CustomBadRequestException("El monto de la transacción debe ser mayor a 0");
            }
            if (card.getBalance() - transaction.getPrice() < 0) {
                throw new CustomBadRequestException("Saldo insuficiente");
            }
            if (card.getExpiryDate().before(new Date())) {
                throw new CustomBadRequestException("Tarjeta vencida");
            }
            card.setBalance(card.getBalance() - transaction.getPrice());
            cardRepository.save(card);
            Transaction newTransaction = new Transaction();
            newTransaction.setCard(card);
            newTransaction.setPrice(transaction.getPrice());
            newTransaction.setTransactionDate(new Date());

            return transactionRepository.save(newTransaction);
        } catch (CustomBadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomBadRequestException("Error al realizar la transacción");
        }

    }

    public Transaction getTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new CustomBadRequestException("Transacción no encontrada"));
    }

    public Transaction anulateTransaction(TransactionDto anulateTransactionDto) {
        try {
            Transaction transaction = transactionRepository.findById(anulateTransactionDto.getTransactionId())
                    .orElseThrow(() -> new CustomBadRequestException("Transacción no encontrada"));

            Date currentTime = new Date();
            long diff = currentTime.getTime() - transaction.getTransactionDate().getTime();
            long diffHours = diff / (60 * 60 * 1000);

            if (diffHours > 24) {
                throw new CustomBadRequestException("No se puede anular una transacción después de 24 horas");
            }

            if (transaction.getStatus().equals(TransactionStatus.ANULATED)) {
                throw new CustomBadRequestException("La transacción ya ha sido anulada");
            }

            Card card = transaction.getCard();
            card.setBalance(card.getBalance() + transaction.getPrice());
            cardRepository.save(card);

            transaction.setStatus(TransactionStatus.ANULATED);
            return transactionRepository.save(transaction);
        } catch (CustomBadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomBadRequestException("Error al anular la transacción");
        }
    }

}