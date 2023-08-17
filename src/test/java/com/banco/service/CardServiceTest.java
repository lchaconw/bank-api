package com.banco.service;

import com.banco.DummyMocks;
import com.banco.exceptions.CustomBadRequestException;
import com.banco.models.Card;
import com.banco.models.Product;
import com.banco.models.dto.CardDto;
import com.banco.repository.CardRepository;
import com.banco.repository.ProductRepository;
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
public class CardServiceTest {
    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;
    @Mock
    private ProductRepository productRepository;

    @Test
    public void generateCardNumberTest() {
        Long productId = 123456L;
        String name = "Pedro";
        String lastName = "Gonzalez";
        Product testProduct = DummyMocks.getProduct();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));

        Card response = cardService.generateCardNumber(productId, name, lastName);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(testProduct, response.getProduct());
        Assertions.assertEquals(DummyMocks.getCardInactive().getStatus(), response.getStatus());
        Assertions.assertEquals(DummyMocks.getCardInactive().getCardHolderName(), response.getCardHolderName());
    }

    @Test
    public void testGenerateCardNumber_ProductNotFound() {
        Long productId = 123456L;
        String name = "John";
        String lastName = "Doe";

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> cardService.generateCardNumber(productId, name, lastName)
        );

        Assertions.assertEquals("Producto no encontrado", thrown.getMessage());
    }

    @Test
    public void enrollCardTest() {
        CardDto cardDto = new CardDto();
        cardDto.setCardId("1254783156215411");
        Card card = DummyMocks.getCardInactive();
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        cardService.enrollCard(cardDto);
        Mockito.verify(cardRepository, Mockito.times(1)).save(card);
    }

    @Test
    public void enrollCardTest_error() {
        CardDto cardDto = new CardDto();
        cardDto.setCardId("1254783156215415");
        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> cardService.enrollCard(cardDto)
        );

        Assertions.assertEquals("Tarjeta no encontrada", thrown.getMessage());
    }

    @Test
    public void enrollCardTest_error2() {
        CardDto cardDto = new CardDto();
        cardDto.setCardId("1254783156215411");
        Card card = DummyMocks.getCardActive();
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> cardService.enrollCard(cardDto)
        );

        Assertions.assertEquals("Tarjeta ya ha sido activada", thrown.getMessage());
    }

    @Test
    public void blockCardTest() {
        Card card = DummyMocks.getCardActive();
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        cardService.blockCard(card.getCardId());
        Mockito.verify(cardRepository, Mockito.times(1)).save(card);
    }

    @Test
    public void blockCardTest_error() {
        Card card = DummyMocks.getCardActive();
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.empty());
        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> cardService.blockCard(card.getCardId())
        );

        Assertions.assertEquals("Tarjeta no encontrada", thrown.getMessage());
    }

    @Test
    public void blockCardTest_error2() {
        Card card = DummyMocks.getCardBlocked();
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> cardService.blockCard(card.getCardId())
        );

        Assertions.assertEquals("Tarjeta ya ha sido bloqueada previamente", thrown.getMessage());
    }

    @Test
    public void rechargeBalanceTest() {
        CardDto cardDto = new CardDto();
        cardDto.setCardId("1254783156215411");
        cardDto.setBalance(100L);
        Card card = DummyMocks.getCardActive();
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        cardService.rechargeBalance(cardDto);
        Mockito.verify(cardRepository, Mockito.times(1)).save(card);
    }

    @Test
    public void rechargeBalanceTest_error() {
        CardDto cardDto = new CardDto();
        cardDto.setCardId("1254783156215411");
        cardDto.setBalance(100L);
        Card card = DummyMocks.getCardBlocked();
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> cardService.rechargeBalance(cardDto)
        );

        Assertions.assertEquals("Tarjeta bloqueada o inactiva", thrown.getMessage());
    }

    @Test
    public void rechargeBalanceTest_error2() {
        CardDto cardDto = new CardDto();
        cardDto.setCardId("1254783156215411");
        cardDto.setBalance(-100L);
        Card card = DummyMocks.getCardActive();
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> cardService.rechargeBalance(cardDto)
        );

        Assertions.assertEquals("El saldo a recargar debe ser mayor a 0", thrown.getMessage());
    }

    @Test
    public void getBalanceTest() {
        Card card = DummyMocks.getCardActive();
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        double response = cardService.getBalance(card.getCardId());
        Assertions.assertEquals(card.getBalance(), response);
    }

    @Test
    public void getBalanceTest_error() {
        Card card = DummyMocks.getCardActive();
        Mockito.when(cardRepository.findById(card.getCardId())).thenReturn(Optional.empty());
        CustomBadRequestException thrown = assertThrows(
                CustomBadRequestException.class,
                () -> cardService.getBalance(card.getCardId())
        );

        Assertions.assertEquals("Tarjeta no encontrada", thrown.getMessage());
    }

}
