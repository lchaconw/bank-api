package com.banco.controller;

import com.banco.DummyMocks;
import com.banco.models.Transaction;
import com.banco.models.dto.Response;
import com.banco.models.dto.TransactionDto;
import com.banco.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
public class TransactionControllerTest {
    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @Test
    public void purchaseTest() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCardId("1254783156215411");
        transactionDto.setPrice(100L);
        Transaction transaction = DummyMocks.getTransaction();
        Mockito.when(transactionService.purchase(transactionDto)).thenReturn(transaction);
        ResponseEntity<Transaction> response = transactionController.purchase(transactionDto);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(transaction, response.getBody());
    }

    @Test
    public void getTransactionTest() {
        Long transactionId = 1L;
        Transaction transaction = DummyMocks.getTransaction();
        Mockito.when(transactionService.getTransaction(transactionId)).thenReturn(transaction);
        ResponseEntity<Transaction> response = transactionController.getTransaction(transactionId);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(transaction, response.getBody());
    }

    @Test
    public void anulateTransactionTest() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionId(1L);
        Transaction transaction = DummyMocks.getTransaction();
        Mockito.when(transactionService.anulateTransaction(transactionDto)).thenReturn(transaction);
        ResponseEntity<Response> response = transactionController.anulateTransaction(transactionDto);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals("Transacción anulada", response.getBody().getMessage());
    }

}
