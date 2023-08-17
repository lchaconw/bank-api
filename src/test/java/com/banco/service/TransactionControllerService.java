package com.banco.service;

import com.banco.DummyMocks;
import com.banco.exceptions.CustomBadRequestException;
import com.banco.models.Card;
import com.banco.models.Transaction;
import com.banco.models.dto.TransactionDto;
import com.banco.repository.CardRepository;
import com.banco.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class TransactionControllerService {
    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardRepository cardRepository;

    @Test
    public void purchaseTest() {
        Card card = DummyMocks.getCardActive();
        Transaction transaction = DummyMocks.getTransaction();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCardId(card.getCardId());
        transactionDto.setPrice(transaction.getPrice());
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);
        Transaction result = transactionService.purchase(transactionDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(transaction, result);
    }

    @Test
    public void purchaseTest_Error() {
        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> transactionService.purchase(null)
        );

        Assertions.assertEquals("Error al realizar la transacción", thrown.getMessage());
    }

    @Test
    public void purchaseTest_CardNotFound() {
        Card card = DummyMocks.getCardActive();
        Transaction transaction = DummyMocks.getTransaction();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCardId(card.getCardId());
        transactionDto.setPrice(transaction.getPrice());
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.empty());
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> transactionService.purchase(transactionDto)
        );

        Assertions.assertEquals("Tarjeta no encontrada", thrown.getMessage());
    }

    @Test
    public void purchaseTest_CardNotActive() {
        Card card = DummyMocks.getCardInactive();
        Transaction transaction = DummyMocks.getTransaction();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCardId(card.getCardId());
        transactionDto.setPrice(transaction.getPrice());
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> transactionService.purchase(transactionDto)
        );

        Assertions.assertEquals("Transacción no permitida tarjeta no se encuentra activa", thrown.getMessage());
    }

    @Test
    public void purchaseTest_TransactionPriceGreaterThan0Error() {
        Card card = DummyMocks.getCardActive();
        Transaction transaction = DummyMocks.getTransactionWithoutMount();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCardId(card.getCardId());
        transactionDto.setPrice(transaction.getPrice());
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> transactionService.purchase(transactionDto)
        );

        Assertions.assertEquals("El monto de la transacción debe ser mayor a 0", thrown.getMessage());
    }

    @Test
    public void purchaseTest_InsufficientBalance() {
        Card card = DummyMocks.getCardActive();
        Transaction transaction = DummyMocks.getTransaction();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCardId(card.getCardId());
        transactionDto.setPrice(1000000.0);
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> transactionService.purchase(transactionDto)
        );

        Assertions.assertEquals("Saldo insuficiente", thrown.getMessage());
    }

    @Test
    public void purchaseTest_ExpiredCard() {
        Card card = DummyMocks.getCardExpired();
        Transaction transaction = DummyMocks.getTransaction();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCardId(card.getCardId());
        transactionDto.setPrice(transaction.getPrice());
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> transactionService.purchase(transactionDto)
        );

        Assertions.assertEquals("Tarjeta vencida", thrown.getMessage());
    }

    @Test
    public void getTransactionTest() {
        Transaction transaction = DummyMocks.getTransaction();
        Mockito.when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        Transaction result = transactionService.getTransaction(transaction.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(transaction, result);
    }

    @Test
    public void anulateTransactionTest() {
        Transaction transaction = DummyMocks.getTransaction();
        TransactionDto transactionDto = new TransactionDto();
        Card card = DummyMocks.getCardActive();
        transactionDto.setCardId(card.getCardId());
        transactionDto.setTransactionId(transaction.getId());
        Mockito.when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);
        Transaction result = transactionService.anulateTransaction(transactionDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(transaction, result);
    }

    @Test
    public void anulateTransactionTest_CardNotFound() {
        Transaction transaction = DummyMocks.getTransactionExpired();
        TransactionDto transactionDto = new TransactionDto();
        Card card = DummyMocks.getCardActive();
        transactionDto.setCardId(card.getCardId());
        transactionDto.setTransactionId(transaction.getId());
        Mockito.when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> transactionService.anulateTransaction(transactionDto)
        );

        Assertions.assertEquals("No se puede anular una transacción después de 24 horas", thrown.getMessage());
    }

    @Test
    public void anulateTransactionTest_TransactionAnulatedAlready() {
        Transaction transaction = DummyMocks.getTransactionAnulated();
        TransactionDto transactionDto = new TransactionDto();
        Card card = DummyMocks.getCardActive();
        transactionDto.setCardId(card.getCardId());
        transactionDto.setTransactionId(transaction.getId());
        Mockito.when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> transactionService.anulateTransaction(transactionDto)
        );

        Assertions.assertEquals("La transacción ya ha sido anulada", thrown.getMessage());
    }

    @Test
    public void anulateTransactionTest_Error() {
        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> transactionService.anulateTransaction(null)
        );

        Assertions.assertEquals("Error al anular la transacción", thrown.getMessage());
    }

}
