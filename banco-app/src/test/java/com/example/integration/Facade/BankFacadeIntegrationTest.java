package com.example.integration.Facade;

import com.example.Facade.BankFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankFacadeIntegrationTest {

    private BankFacade bankFacade;

    @BeforeEach
    void setUp() {
        bankFacade = new BankFacade();
    }

    @Test
    void testCreateAccount_Integration() {
      
        String accountNumber = bankFacade.createAccount("María García", 500.0, "maria@example.com");
       
        assertNotNull(accountNumber);
        assertTrue(accountNumber.startsWith("ACC"));
        assertTrue(bankFacade.verifyAccount(accountNumber));
    }

    @Test
    void testDeposit_Integration() {
    
        String accountNumber = bankFacade.createAccount("Carlos López", 1000.0, "carlos@example.com");
        double initialBalance = bankFacade.getBalance(accountNumber);
        
        boolean success = bankFacade.deposit(accountNumber, 250.0, "carlos@example.com");
        double newBalance = bankFacade.getBalance(accountNumber);
       
        assertTrue(success);
        assertEquals(initialBalance + 250.0, newBalance, 0.001);
    }

    @Test
    void testWithdraw_Integration_SufficientFunds() {
   
        String accountNumber = bankFacade.createAccount("Ana Martínez", 2000.0, "ana@example.com");
        double initialBalance = bankFacade.getBalance(accountNumber);
        
        boolean success = bankFacade.withdraw(accountNumber, 300.0, "ana@example.com");
        double newBalance = bankFacade.getBalance(accountNumber);
       
        assertTrue(success);
        assertEquals(initialBalance - 300.0, newBalance, 0.001);
    }

    @Test
    void testWithdraw_Integration_InsufficientFunds() {
      
        String accountNumber = bankFacade.createAccount("Pedro Sánchez", 100.0, "pedro@example.com");
        
        boolean success = bankFacade.withdraw(accountNumber, 500.0, "pedro@example.com");
        
        assertFalse(success);
        
        assertEquals(100.0, bankFacade.getBalance(accountNumber), 0.001);
    }

    @Test
    void testTransfer_Integration_Success() {
     
        String fromAccount = bankFacade.createAccount("Emisor Test", 1500.0, "emisor@example.com");
        String toAccount = bankFacade.createAccount("Receptor Test", 500.0, "receptor@example.com");
        
        double initialBalanceFrom = bankFacade.getBalance(fromAccount);
        double initialBalanceTo = bankFacade.getBalance(toAccount);
        
        boolean success = bankFacade.transfer(fromAccount, toAccount, 200.0, "emisor@example.com", "receptor@example.com");
        
        double finalBalanceFrom = bankFacade.getBalance(fromAccount);
        double finalBalanceTo = bankFacade.getBalance(toAccount);
        
    
        assertTrue(success);
        assertEquals(initialBalanceFrom - 200.0, finalBalanceFrom, 0.001);
        assertEquals(initialBalanceTo + 200.0, finalBalanceTo, 0.001);
    }

    @Test
    void testTransfer_Integration_InsufficientFunds() {
     
        String fromAccount = bankFacade.createAccount("Emisor Pobre", 100.0, "pobre@example.com");
        String toAccount = bankFacade.createAccount("Receptor Rico", 5000.0, "rico@example.com");
        
        double initialBalanceFrom = bankFacade.getBalance(fromAccount);
        double initialBalanceTo = bankFacade.getBalance(toAccount);
       
        boolean success = bankFacade.transfer(fromAccount, toAccount, 500.0, "pobre@example.com", "rico@example.com");
        
        double finalBalanceFrom = bankFacade.getBalance(fromAccount);
        double finalBalanceTo = bankFacade.getBalance(toAccount);
        
        assertFalse(success);
       
        assertEquals(initialBalanceFrom, finalBalanceFrom, 0.001);
        assertEquals(initialBalanceTo, finalBalanceTo, 0.001);
    }

    @Test
    void testGetTransactionHistory_Integration() {
      
        String accountNumber = bankFacade.createAccount("Historial Test", 1000.0, "historial@example.com");
       
        bankFacade.deposit(accountNumber, 500.0, "historial@example.com");
        bankFacade.withdraw(accountNumber, 300.0, "historial@example.com");
        
        String history = bankFacade.getTransactionHistory(accountNumber);
        
        assertNotNull(history);
        assertFalse(history.isEmpty());
        assertTrue(history.contains("Historial de transacciones"));
    }

    @Test
    void testGetAccountHolder_Integration() {
     
        String accountNumber = bankFacade.createAccount("Titular Test", 750.0, "titular@example.com");
        
        String accountHolder = bankFacade.getAccountHolder(accountNumber);
       
        assertNotNull(accountHolder);
        assertEquals("Cliente Banco Ejemplo", accountHolder);
    }
}