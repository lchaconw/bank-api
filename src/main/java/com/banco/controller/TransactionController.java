package com.banco.controller;

import com.banco.models.Transaction;
import com.banco.models.dto.Response;
import com.banco.models.dto.TransactionDto;
import com.banco.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<Transaction> purchase(@RequestBody TransactionDto transaction) {
        Transaction savedTransaction = transactionService.purchase(transaction);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.getTransaction(transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/anulation")
    public ResponseEntity<Response> anulateTransaction(@RequestBody TransactionDto anulateTransactionDto) {
        transactionService.anulateTransaction(anulateTransactionDto);
        Response response = new Response("Transacción anulada", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
