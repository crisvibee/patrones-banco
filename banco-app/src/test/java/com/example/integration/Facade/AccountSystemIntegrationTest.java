package com.example.integration.Facade;

import com.example.Facade.model.AccountSystemImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountSystemIntegrationTest {

    private AccountSystemImpl accountSystem;

    @BeforeEach
    void setUp() {
        accountSystem = new AccountSystemImpl();
    }

    @Test
    void testCreateAccount_Success() {
        
        String accountNumber = accountSystem.createAccount("Juan Pérez", 1000.0);

        assertNotNull(accountNumber);
        assertTrue(accountNumber.startsWith("ACC"));
        assertTrue(accountSystem.verifyAccount(accountNumber));
    }

    @Test
    void testVerifyAccount_AccountExists() {
  
        String accountNumber = accountSystem.createAccount("María García", 500.0);

        boolean result = accountSystem.verifyAccount(accountNumber);

        assertTrue(result);
    }

    @Test
    void testVerifyAccount_AccountNotExists() {
        
        boolean result = accountSystem.verifyAccount("NON_EXISTENT_ACC");

        assertFalse(result);
    }

    @Test
    void testGetBalance_AccountExists() {
      
        String accountNumber = accountSystem.createAccount("Carlos López", 1500.0);

        double balance = accountSystem.getBalance(accountNumber);

        assertEquals(1500.0, balance);
    }

    @Test
    void testGetBalance_AccountNotExists() {
       
        double balance = accountSystem.getBalance("NON_EXISTENT_ACC");

        assertEquals(0.0, balance);
    }

    @Test
    void testUpdateBalance_Deposit() {
       
        String accountNumber = accountSystem.createAccount("Ana Rodríguez", 1000.0);

        accountSystem.updateBalance(accountNumber, 500.0);
        double newBalance = accountSystem.getBalance(accountNumber);

        assertEquals(1500.0, newBalance);
    }

    @Test
    void testUpdateBalance_Withdraw() {
       
        String accountNumber = accountSystem.createAccount("Pedro Martínez", 1000.0);

        accountSystem.updateBalance(accountNumber, -300.0);
        double newBalance = accountSystem.getBalance(accountNumber);

        assertEquals(700.0, newBalance);
    }

    @Test
    void testUpdateBalance_NewAccount() {
       
        String accountNumber = "NEW_ACC_123";

        accountSystem.updateBalance(accountNumber, 1000.0);
        double balance = accountSystem.getBalance(accountNumber);

        assertEquals(1000.0, balance);
    }

    @Test
    void testGetAccountHolder_AccountExists() {
        
        String accountHolder = "Laura Sánchez";
        String accountNumber = accountSystem.createAccount(accountHolder, 2000.0);

        String result = accountSystem.getAccountHolder(accountNumber);

        assertEquals(accountHolder, result);
    }

    @Test
    void testGetAccountHolder_AccountNotExists() {
      
        String result = accountSystem.getAccountHolder("NON_EXISTENT_ACC");

        assertEquals("Cliente Banco Ejemplo", result);
    }

    @Test
    void testMultipleAccounts() {
       
        String account1 = accountSystem.createAccount("Cliente 1", 1000.0);
        String account2 = accountSystem.createAccount("Cliente 2", 2000.0);

        assertTrue(accountSystem.verifyAccount(account1));
        assertTrue(accountSystem.verifyAccount(account2));
        assertEquals(1000.0, accountSystem.getBalance(account1));
        assertEquals(2000.0, accountSystem.getBalance(account2));
        assertEquals("Cliente 1", accountSystem.getAccountHolder(account1));
        assertEquals("Cliente 2", accountSystem.getAccountHolder(account2));
    }
}