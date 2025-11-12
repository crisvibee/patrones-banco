package com.example.unit.Facade;

import com.example.Facade.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para BankFacade
 * Se utilizan mocks para aislar el componente bajo prueba
 */
class BankFacadeUnitTest {

    @Mock
    private AccountSystem mockAccountSystem;
    
    @Mock
    private TransactionSystem mockTransactionSystem;
    
    @Mock
    private NotificationSystem mockNotificationSystem;
    
    private BankFacade bankFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bankFacade = new BankFacade(mockAccountSystem, mockTransactionSystem, mockNotificationSystem);
    }

    @Test
    void testCreateAccount_Success() {
        // Arrange
        String accountNumber = "ACC1234567890";
        String accountHolder = "Juan Pérez";
        double initialBalance = 1000.0;
        String email = "juan@example.com";
        
        when(mockAccountSystem.createAccount(accountHolder, initialBalance)).thenReturn(accountNumber);
        when(mockAccountSystem.verifyAccount(accountNumber)).thenReturn(true);
        
        // Act
        String result = bankFacade.createAccount(accountHolder, initialBalance, email);
        
        // Assert
        assertEquals(accountNumber, result);
        verify(mockAccountSystem).createAccount(accountHolder, initialBalance);
        verify(mockAccountSystem).verifyAccount(accountNumber);
        verify(mockNotificationSystem).notifyAccountCreation(accountNumber, accountHolder, initialBalance, email);
    }

    @Test
    void testCreateAccount_Failure() {
        // Arrange
        String accountHolder = "Juan Pérez";
        double initialBalance = 1000.0;
        String email = "juan@example.com";
        String accountNumber = "ACC1234567890";
        
        when(mockAccountSystem.createAccount(accountHolder, initialBalance)).thenReturn(accountNumber);
        when(mockAccountSystem.verifyAccount(accountNumber)).thenReturn(false);
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            bankFacade.createAccount(accountHolder, initialBalance, email);
        });
        
        verify(mockAccountSystem).createAccount(accountHolder, initialBalance);
        verify(mockAccountSystem).verifyAccount(accountNumber);
        verify(mockNotificationSystem, never()).notifyAccountCreation(any(), any(), anyDouble(), any());
    }

    @Test
    void testGetBalance_Success() {
        // Arrange
        String accountNumber = "ACC1234567890";
        double expectedBalance = 1500.0;
        
        when(mockAccountSystem.verifyAccount(accountNumber)).thenReturn(true);
        when(mockAccountSystem.getBalance(accountNumber)).thenReturn(expectedBalance);
        
        // Act
        double result = bankFacade.getBalance(accountNumber);
        
        // Assert
        assertEquals(expectedBalance, result);
        verify(mockAccountSystem).verifyAccount(accountNumber);
        verify(mockAccountSystem).getBalance(accountNumber);
    }

    @Test
    void testGetBalance_AccountNotFound() {
        // Arrange
        String accountNumber = "ACC9999999999";
        
        when(mockAccountSystem.verifyAccount(accountNumber)).thenReturn(false);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            bankFacade.getBalance(accountNumber);
        });
        
        verify(mockAccountSystem).verifyAccount(accountNumber);
        verify(mockAccountSystem, never()).getBalance(any());
    }

    @Test
    void testVerifyAccount() {
        // Arrange
        String accountNumber = "ACC1234567890";
        
        when(mockAccountSystem.verifyAccount(accountNumber)).thenReturn(true);
        
        // Act
        boolean result = bankFacade.verifyAccount(accountNumber);
        
        // Assert
        assertTrue(result);
        verify(mockAccountSystem).verifyAccount(accountNumber);
    }

    @Test
    void testGetAccountHolder_Success() {
        // Arrange
        String accountNumber = "ACC1234567890";
        String expectedHolder = "Cliente Banco Ejemplo";
        
        when(mockAccountSystem.verifyAccount(accountNumber)).thenReturn(true);
        when(mockAccountSystem.getAccountHolder(accountNumber)).thenReturn(expectedHolder);
        
        // Act
        String result = bankFacade.getAccountHolder(accountNumber);
        
        // Assert
        assertEquals(expectedHolder, result);
        verify(mockAccountSystem).verifyAccount(accountNumber);
        verify(mockAccountSystem).getAccountHolder(accountNumber);
    }

    @Test
    void testGetTransactionHistory_Success() {
        // Arrange
        String accountNumber = "ACC1234567890";
        String expectedHistory = "Historial de transacciones simuladas";
        
        when(mockAccountSystem.verifyAccount(accountNumber)).thenReturn(true);
        when(mockTransactionSystem.getTransactionHistory(accountNumber)).thenReturn(expectedHistory);
        
        // Act
        String result = bankFacade.getTransactionHistory(accountNumber);
        
        // Assert
        assertEquals(expectedHistory, result);
        verify(mockAccountSystem).verifyAccount(accountNumber);
        verify(mockTransactionSystem).getTransactionHistory(accountNumber);
    }
}