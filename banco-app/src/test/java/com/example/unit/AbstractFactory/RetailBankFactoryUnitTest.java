package com.example.unit.AbstractFactory;

import com.example.AbstractFactory.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba unitaria para RetailBankFactory
 * Verifica que la fábrica cree los productos correctos para banca minorista
 */
public class RetailBankFactoryUnitTest {

    @Test
    public void testCreateCheckingAccount() {
        // Arrange
        RetailBankFactory factory = new RetailBankFactory();
        
        // Act
        Account account = factory.createAccount("ACC001", 1000.0, "CHECKING");
        
        // Assert
        assertNotNull(account, "La cuenta no debería ser nula");
        assertTrue(account instanceof CheckingAccount, "Debería ser una CheckingAccount");
        assertEquals("ACC001", account.getAccountNumber(), "Número de cuenta incorrecto");
        assertEquals(1000.0, account.getBalance(), 0.001, "Balance incorrecto");
        assertEquals("CHECKING", account.getType(), "Tipo de cuenta incorrecto");
    }

    @Test
    public void testCreateDebitCard() {
        // Arrange
        RetailBankFactory factory = new RetailBankFactory();
        
        // Act
        Card card = factory.createCard("4111111111111111", "John Doe", "12/25", "123");
        
        // Assert
        assertNotNull(card, "La tarjeta no debería ser nula");
        assertTrue(card instanceof DebitCard, "Debería ser una DebitCard");
        assertEquals("4111111111111111", card.getCardNumber(), "Número de tarjeta incorrecto");
        assertEquals("John Doe", card.getHolderName(), "Titular incorrecto");
    }

    @Test
    public void testCreatePersonalCreditLine() {
        // Arrange
        RetailBankFactory factory = new RetailBankFactory();
        
        // Act
        CreditLine creditLine = factory.createCreditLine("CL001", 5000.0, 0.0);
        
        // Assert
        assertNotNull(creditLine, "La línea de crédito no debería ser nula");
        assertTrue(creditLine instanceof PersonalCreditLine, "Debería ser una PersonalCreditLine");
        assertEquals("CL001", creditLine.getCreditLineNumber(), "Número de línea incorrecto");
        assertEquals(5000.0, creditLine.getCreditLimit(), 0.001, "Límite de crédito incorrecto");
    }

    @Test
    public void testInvalidAccountTypeThrowsException() {
        // Arrange
        RetailBankFactory factory = new RetailBankFactory();
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> factory.createAccount("123", 100.0, "INVALID"));
        
        assertEquals("Tipo de cuenta no válido: INVALID", exception.getMessage());
    }

    @Test
    public void testRetailFactoryCreatesDebitCard() {
        // Arrange
        RetailBankFactory factory = new RetailBankFactory();
        
        // Act
        Card card = factory.createCard("4111111111111111", "John Doe", "12/25", "123");
        
        // Assert
        assertNotNull(card, "La tarjeta no debería ser nula");
        assertTrue(card instanceof DebitCard, "RetailBankFactory debería crear DebitCard");
        assertEquals("4111111111111111", card.getCardNumber(), "Número de tarjeta incorrecto");
        assertEquals("John Doe", card.getHolderName(), "Titular incorrecto");
    }

    @Test
    public void testRetailFactoryCreatesPersonalCreditLine() {
        // Arrange
        RetailBankFactory factory = new RetailBankFactory();
        
        // Act
        CreditLine creditLine = factory.createCreditLine("CL001", 5000.0, 0.0);
        
        // Assert
        assertNotNull(creditLine, "La línea de crédito no debería ser nula");
        assertTrue(creditLine instanceof PersonalCreditLine, "RetailBankFactory debería crear PersonalCreditLine");
        assertEquals("CL001", creditLine.getCreditLineNumber(), "Número de línea incorrecto");
        assertEquals(5000.0, creditLine.getCreditLimit(), 0.001, "Límite de crédito incorrecto");
    }
}