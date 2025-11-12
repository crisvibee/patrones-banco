package com.example.integration.Facade;

import com.example.Facade.BankFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para BankFacade
 * Prueba la interacción entre BankFacade y los sistemas reales
 */
class BankFacadeIntegrationTest {

    private BankFacade bankFacade;

    @BeforeEach
    void setUp() {
        bankFacade = new BankFacade();
    }

    @Test
    void testCreateAccount_Integration() {
        // Act
        String accountNumber = bankFacade.createAccount("María García", 500.0, "maria@example.com");
        
        // Assert
        assertNotNull(accountNumber);
        assertTrue(accountNumber.startsWith("ACC"));
        assertTrue(bankFacade.verifyAccount(accountNumber));
    }

    @Test
    void testDeposit_Integration() {
        // Arrange
        String accountNumber = bankFacade.createAccount("Carlos López", 1000.0, "carlos@example.com");
        double initialBalance = bankFacade.getBalance(accountNumber);
        
        // Act
        boolean success = bankFacade.deposit(accountNumber, 250.0, "carlos@example.com");
        double newBalance = bankFacade.getBalance(accountNumber);
        
        // Assert
        assertTrue(success);
        assertEquals(initialBalance + 250.0, newBalance, 0.001);
    }

    @Test
    void testWithdraw_Integration_SufficientFunds() {
        // Arrange
        String accountNumber = bankFacade.createAccount("Ana Martínez", 2000.0, "ana@example.com");
        double initialBalance = bankFacade.getBalance(accountNumber);
        
        // Act
        boolean success = bankFacade.withdraw(accountNumber, 300.0, "ana@example.com");
        double newBalance = bankFacade.getBalance(accountNumber);
        
        // Assert
        assertTrue(success);
        assertEquals(initialBalance - 300.0, newBalance, 0.001);
    }

    @Test
    void testWithdraw_Integration_InsufficientFunds() {
        // Arrange
        String accountNumber = bankFacade.createAccount("Pedro Sánchez", 100.0, "pedro@example.com");
        
        // Act
        boolean success = bankFacade.withdraw(accountNumber, 500.0, "pedro@example.com");
        
        // Assert
        assertFalse(success);
        // El saldo debe permanecer igual
        assertEquals(100.0, bankFacade.getBalance(accountNumber), 0.001);
    }

    @Test
    void testTransfer_Integration_Success() {
        // Arrange
        String fromAccount = bankFacade.createAccount("Emisor Test", 1500.0, "emisor@example.com");
        String toAccount = bankFacade.createAccount("Receptor Test", 500.0, "receptor@example.com");
        
        double initialBalanceFrom = bankFacade.getBalance(fromAccount);
        double initialBalanceTo = bankFacade.getBalance(toAccount);
        
        // Act
        boolean success = bankFacade.transfer(fromAccount, toAccount, 200.0, "emisor@example.com", "receptor@example.com");
        
        double finalBalanceFrom = bankFacade.getBalance(fromAccount);
        double finalBalanceTo = bankFacade.getBalance(toAccount);
        
        // Assert
        assertTrue(success);
        assertEquals(initialBalanceFrom - 200.0, finalBalanceFrom, 0.001);
        assertEquals(initialBalanceTo + 200.0, finalBalanceTo, 0.001);
    }

    @Test
    void testTransfer_Integration_InsufficientFunds() {
        // Arrange
        String fromAccount = bankFacade.createAccount("Emisor Pobre", 100.0, "pobre@example.com");
        String toAccount = bankFacade.createAccount("Receptor Rico", 5000.0, "rico@example.com");
        
        double initialBalanceFrom = bankFacade.getBalance(fromAccount);
        double initialBalanceTo = bankFacade.getBalance(toAccount);
        
        // Act
        boolean success = bankFacade.transfer(fromAccount, toAccount, 500.0, "pobre@example.com", "rico@example.com");
        
        double finalBalanceFrom = bankFacade.getBalance(fromAccount);
        double finalBalanceTo = bankFacade.getBalance(toAccount);
        
        // Assert
        assertFalse(success);
        // Los saldos deben permanecer iguales
        assertEquals(initialBalanceFrom, finalBalanceFrom, 0.001);
        assertEquals(initialBalanceTo, finalBalanceTo, 0.001);
    }

    @Test
    void testGetTransactionHistory_Integration() {
        // Arrange
        String accountNumber = bankFacade.createAccount("Historial Test", 1000.0, "historial@example.com");
        
        // Realizar algunas operaciones
        bankFacade.deposit(accountNumber, 500.0, "historial@example.com");
        bankFacade.withdraw(accountNumber, 300.0, "historial@example.com");
        
        // Act
        String history = bankFacade.getTransactionHistory(accountNumber);
        
        // Assert
        assertNotNull(history);
        assertFalse(history.isEmpty());
        assertTrue(history.contains("Historial de transacciones"));
    }

    @Test
    void testGetAccountHolder_Integration() {
        // Arrange
        String accountNumber = bankFacade.createAccount("Titular Test", 750.0, "titular@example.com");
        
        // Act
        String accountHolder = bankFacade.getAccountHolder(accountNumber);
        
        // Assert
        assertNotNull(accountHolder);
        assertEquals("Cliente Banco Ejemplo", accountHolder);
    }
}