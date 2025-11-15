package com.example.integration.Facade;

import com.example.Facade.model.TransactionSystemImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionSystemIntegrationTest {

    private TransactionSystemImpl transactionSystem;

    @BeforeEach
    void setUp() {
        transactionSystem = new TransactionSystemImpl();
    }

    @Test
    void testProcessTransfer_Success() {
      
        boolean result = transactionSystem.processTransfer("ACC123", "ACC456", 500.0);

        assertTrue(result);
    }

    @Test
    void testProcessTransfer_ZeroAmount() {
      
        boolean result = transactionSystem.processTransfer("ACC123", "ACC456", 0.0);

        assertFalse(result);
    }

    @Test
    void testProcessTransfer_NegativeAmount() {
       
        boolean result = transactionSystem.processTransfer("ACC123", "ACC456", -100.0);

        assertFalse(result);
    }

    @Test
    void testProcessTransfer_SameAccount() {
      
        boolean result = transactionSystem.processTransfer("ACC123", "ACC123", 500.0);

        assertFalse(result);
    }

    @Test
    void testProcessDeposit_Success() {
       
        boolean result = transactionSystem.processDeposit("ACC123", 1000.0);

        assertTrue(result);
    }

    @Test
    void testProcessDeposit_ZeroAmount() {
      
        boolean result = transactionSystem.processDeposit("ACC123", 0.0);

        assertFalse(result);
    }

    @Test
    void testProcessDeposit_NegativeAmount() {
     
        boolean result = transactionSystem.processDeposit("ACC123", -200.0);

        assertFalse(result);
    }

    @Test
    void testProcessWithdrawal_Success() {
      
        boolean result = transactionSystem.processWithdrawal("ACC123", 300.0);

        assertTrue(result);
    }

    @Test
    void testProcessWithdrawal_ZeroAmount() {
       
        boolean result = transactionSystem.processWithdrawal("ACC123", 0.0);

        assertFalse(result);
    }

    @Test
    void testProcessWithdrawal_NegativeAmount() {
      
        boolean result = transactionSystem.processWithdrawal("ACC123", -150.0);

        assertFalse(result);
    }

    @Test
    void testGetTransactionHistory() {
      
        String history = transactionSystem.getTransactionHistory("ACC123");

        assertNotNull(history);
        assertTrue(history.contains("Historial de transacciones"));
        assertTrue(history.contains("ACC123"));
        assertTrue(history.contains("Depósito"));
        assertTrue(history.contains("Transferencia"));
        assertTrue(history.contains("Retiro"));
    }

    @Test
    void testGetTransactionHistory_DifferentAccounts() {
       
        String history1 = transactionSystem.getTransactionHistory("ACC123");
        String history2 = transactionSystem.getTransactionHistory("ACC456");

        assertNotNull(history1);
        assertNotNull(history2);
        assertTrue(history1.contains("ACC123"));
        assertTrue(history2.contains("ACC456"));
    }
}