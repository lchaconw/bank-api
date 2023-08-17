package com.banco.controller;

import com.banco.DummyMocks;
import com.banco.models.Card;
import com.banco.models.dto.CardDto;
import com.banco.models.dto.Response;
import com.banco.service.CardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CardControllerTest {
    @InjectMocks
    private CardController cardController;

    @Mock
    private CardService cardService;

    @Test
    public void generateCardNumberTest() {
        Long productId = 1L;
        String name = "Pedro";
        String lastName = "Gonzalez";
        Card card = DummyMocks.getCardInactive();
        Mockito.when(cardService.generateCardNumber(productId, name, lastName)).thenReturn(card);
        Card result = cardController.generateCardNumber(productId, name, lastName).getBody();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(card, result);
    }

    @Test
    public void enrollCardTest() {
        Card card = DummyMocks.getCardInactive();
        CardDto cardDto = new CardDto();
        cardDto.setCardId(card.getCardId());
        Mockito.doNothing().when(cardService).enrollCard(Mockito.any());
        Response response = cardController.enrollCard(cardDto).getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Tarjeta Activada", response.getMessage());
    }

    @Test
    public void blockCardTest() {
        Card card = DummyMocks.getCardActive();
        Response response = cardController.blockCard(card.getCardId()).getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Tarjeta Bloqueada", response.getMessage());
    }

    @Test
    public void rechargeBalanceTest() {
        Card card = DummyMocks.getCardActive();
        CardDto cardDto = new CardDto();
        cardDto.setCardId(card.getCardId());
        Response response = cardController.rechargeBalance(cardDto).getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Saldo Recargado", response.getMessage());
    }

    @Test
    public void getBalanceTest() {
        Card card = DummyMocks.getCardActive();
        Mockito.when(cardService.getBalance(card.getCardId())).thenReturn(card.getBalance());
        Response response = cardController.getBalance(card.getCardId()).getBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Consulta Saldo", response.getMessage());
    }


}
