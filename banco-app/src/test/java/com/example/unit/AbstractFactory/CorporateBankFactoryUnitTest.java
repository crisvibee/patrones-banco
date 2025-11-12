package test.java.com.example.unit.AbstractFactory;

import com.example.AbstractFactory.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba unitaria para CorporateBankFactory
 * Verifica que la fábrica cree los productos correctos para banca corporativa
 */
public class CorporateBankFactoryUnitTest {

    @Test
    public void testCreateSavingsAccount() {
        // Arrange
        CorporateBankFactory factory = new CorporateBankFactory();
        
        // Act
        Account account = factory.createAccount("CORP001", 50000.0, "SAVINGS");
        
        // Assert
        assertNotNull(account, "La cuenta no debería ser nula");
        assertTrue(account instanceof SavingsAccount, "Debería ser una SavingsAccount");
        assertEquals("CORP001", account.getAccountNumber(), "Número de cuenta incorrecto");
        assertEquals(50000.0, account.getBalance(), 0.001, "Balance incorrecto");
        assertEquals("SAVINGS", account.getType(), "Tipo de cuenta incorrecto");
    }

    @Test
    public void testCreateCheckingAccount() {
        // Arrange
        CorporateBankFactory factory = new CorporateBankFactory();
        
        // Act
        Account account = factory.createAccount("CORP002", 25000.0, "CHECKING");
        
        // Assert
        assertNotNull(account, "La cuenta no debería ser nula");
        assertTrue(account instanceof CheckingAccount, "Debería ser una CheckingAccount");
        assertEquals("CORP002", account.getAccountNumber(), "Número de cuenta incorrecto");
        assertEquals(25000.0, account.getBalance(), 0.001, "Balance incorrecto");
        assertEquals("CHECKING", account.getType(), "Tipo de cuenta incorrecto");
    }

    @Test
    public void testCreateCreditCard() {
        // Arrange
        CorporateBankFactory factory = new CorporateBankFactory();
        
        // Act
        Card card = factory.createCard("5555555555554444", "Corporation Inc", "12/27", "789");
        
        // Assert
        assertNotNull(card, "La tarjeta no debería ser nula");
        assertTrue(card instanceof CreditCard, "Debería ser una CreditCard");
        assertEquals("5555555555554444", card.getCardNumber(), "Número de tarjeta incorrecto");
        assertEquals("Corporation Inc", card.getHolderName(), "Titular incorrecto");
    }

    @Test
    public void testCreateBusinessCreditLine() {
        // Arrange
        CorporateBankFactory factory = new CorporateBankFactory();
        
        // Act
        CreditLine creditLine = factory.createCreditLine("BL001", 100000.0, 0.0);
        
        // Assert
        assertNotNull(creditLine, "La línea de crédito no debería ser nula");
        assertTrue(creditLine instanceof BusinessCreditLine, "Debería ser una BusinessCreditLine");
        assertEquals("BL001", creditLine.getCreditLineNumber(), "Número de línea incorrecto");
        assertEquals(100000.0, creditLine.getCreditLimit(), 0.001, "Límite de crédito incorrecto");
    }

    @Test
    public void testInvalidAccountTypeThrowsException() {
        // Arrange
        CorporateBankFactory factory = new CorporateBankFactory();
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> factory.createAccount("123", 100.0, "INVALID_TYPE"));
        
        assertEquals("Tipo de cuenta no válido: INVALID_TYPE", exception.getMessage());
    }

    @Test
    public void testCorporateFactoryCreatesCreditCard() {
        // Arrange
        CorporateBankFactory factory = new CorporateBankFactory();
        
        // Act
        Card card = factory.createCard("5555555555554444", "Corporation Inc", "12/27", "789");
        
        // Assert
        assertNotNull(card, "La tarjeta no debería ser nula");
        assertTrue(card instanceof CreditCard, "CorporateBankFactory debería crear CreditCard");
        assertEquals("5555555555554444", card.getCardNumber(), "Número de tarjeta incorrecto");
        assertEquals("Corporation Inc", card.getHolderName(), "Titular incorrecto");
    }

    @Test
    public void testCorporateFactoryCreatesBusinessCreditLine() {
        // Arrange
        CorporateBankFactory factory = new CorporateBankFactory();
        
        // Act
        CreditLine creditLine = factory.createCreditLine("BL001", 100000.0, 0.0);
        
        // Assert
        assertNotNull(creditLine, "La línea de crédito no debería ser nula");
        assertTrue(creditLine instanceof BusinessCreditLine, "CorporateBankFactory debería crear BusinessCreditLine");
        assertEquals("BL001", creditLine.getCreditLineNumber(), "Número de línea incorrecto");
        assertEquals(100000.0, creditLine.getCreditLimit(), 0.001, "Límite de crédito incorrecto");
    }

    @Test
    public void testAccountCaseInsensitiveType() {
        // Arrange
        CorporateBankFactory factory = new CorporateBankFactory();
        
        // Act - Probar con diferentes casos
        Account savings1 = factory.createAccount("CORP003", 30000.0, "savings");
        Account savings2 = factory.createAccount("CORP004", 40000.0, "Savings");
        Account checking1 = factory.createAccount("CORP005", 15000.0, "checking");
        Account checking2 = factory.createAccount("CORP006", 20000.0, "Checking");
        
        // Assert
        assertTrue(savings1 instanceof SavingsAccount, "Debería ser SavingsAccount (case insensitive)");
        assertTrue(savings2 instanceof SavingsAccount, "Debería ser SavingsAccount (case insensitive)");
        assertTrue(checking1 instanceof CheckingAccount, "Debería ser CheckingAccount (case insensitive)");
        assertTrue(checking2 instanceof CheckingAccount, "Debería ser CheckingAccount (case insensitive)");
    }
}