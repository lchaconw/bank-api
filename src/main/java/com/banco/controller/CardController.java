package com.banco.controller;

import com.banco.models.Card;
import com.banco.models.dto.CardDto;
import com.banco.models.dto.Response;
import com.banco.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    private CardService cardService;

    /**
     * Generate a card number for a given product
     * @param productId Product id
     * @param name Card holder name
     * @param lastName Card holder last name
     * @return Card object
     */
    @PostMapping("/{productId}/number")
    public ResponseEntity<Card> generateCardNumber(@PathVariable Long productId, @RequestParam String name, @RequestParam String lastName) {
        Card card = cardService.generateCardNumber(productId, name, lastName);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    /**
     * Enroll a card
     * @param card Card object
     * @return Response object
     */

    @PostMapping("/enroll")
    public ResponseEntity<Response> enrollCard(@RequestBody CardDto card) {
        cardService.enrollCard(card);
        Response response = new Response("Tarjeta Activada", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Block a card
     * @param cardId Card id
     * @return Response object
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Response> blockCard(@PathVariable String cardId) {
        cardService.blockCard(cardId);
        Response response = new Response("Tarjeta Bloqueada", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Recharge card balance
     * @param card Card object
     * @return Response object
     */
    @PostMapping("/balance")
    public ResponseEntity<Response> rechargeBalance(@RequestBody CardDto card) {
        cardService.rechargeBalance(card);
        Response response = new Response("Saldo Recargado", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get card balance
     * @param cardId Card id
     * @return Response object
     */
    @GetMapping("/balance/{cardId}")
    public ResponseEntity<Response> getBalance(@PathVariable String cardId) {
        double balance = cardService.getBalance(cardId);
        Response response = new Response("Consulta Saldo", Map.of("balance", balance));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}