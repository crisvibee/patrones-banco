package test.java.com.example.unit.Facade;

import com.example.Facade.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankFacadeExtraTests {

    @Mock
    private AccountSystem accountSystem;
    
    @Mock
    private TransactionSystem transactionSystem;
    
    @Mock
    private NotificationSystem notificationSystem;
    
    private BankFacade bankFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bankFacade = new BankFacade(accountSystem, transactionSystem, notificationSystem);
    }

    @Test
    void testDepositSuccess() {
        when(accountSystem.verifyAccount("ACC123")).thenReturn(true);
        when(transactionSystem.processDeposit("ACC123", 500.0)).thenReturn(true);
        
        assertTrue(bankFacade.deposit("ACC123", 500.0, "test@email.com"));
        verify(notificationSystem).notifyTransactionSuccess("ACC123", "Depósito", 500.0, "test@email.com");
    }

    @Test
    void testDepositAccountNotFound() {
        when(accountSystem.verifyAccount("ACC999")).thenReturn(false);
        
        assertFalse(bankFacade.deposit("ACC999", 500.0, "test@email.com"));
        verify(notificationSystem).notifyTransactionFailure("ACC999", "Depósito", 500.0, "test@email.com", "Cuenta no existe");
    }

    @Test
    void testWithdrawSuccess() {
        when(accountSystem.verifyAccount("ACC123")).thenReturn(true);
        when(accountSystem.getBalance("ACC123")).thenReturn(1000.0);
        when(transactionSystem.processWithdrawal("ACC123", 200.0)).thenReturn(true);
        
        assertTrue(bankFacade.withdraw("ACC123", 200.0, "test@email.com"));
        verify(notificationSystem).notifyTransactionSuccess("ACC123", "Retiro", 200.0, "test@email.com");
    }

    @Test
    void testWithdrawInsufficientFunds() {
        when(accountSystem.verifyAccount("ACC123")).thenReturn(true);
        when(accountSystem.getBalance("ACC123")).thenReturn(100.0);
        
        assertFalse(bankFacade.withdraw("ACC123", 200.0, "test@email.com"));
        verify(notificationSystem).notifyTransactionFailure("ACC123", "Retiro", 200.0, "test@email.com", "Fondos insuficientes");
    }

    @Test
    void testTransferSuccess() {
        when(accountSystem.verifyAccount("ACC123")).thenReturn(true);
        when(accountSystem.verifyAccount("ACC456")).thenReturn(true);
        when(accountSystem.getBalance("ACC123")).thenReturn(1000.0);
        when(transactionSystem.processTransfer("ACC123", "ACC456", 300.0)).thenReturn(true);
        
        assertTrue(bankFacade.transfer("ACC123", "ACC456", 300.0, "from@email.com", "to@email.com"));
        verify(notificationSystem).notifyTransactionSuccess("ACC123", "Transferencia", 300.0, "from@email.com");
        verify(notificationSystem).notifyTransactionSuccess("ACC456", "Depósito", 300.0, "to@email.com");
    }

    @Test
    void testTransferInsufficientFunds() {
        when(accountSystem.verifyAccount("ACC123")).thenReturn(true);
        when(accountSystem.verifyAccount("ACC456")).thenReturn(true);
        when(accountSystem.getBalance("ACC123")).thenReturn(200.0);
        
        assertFalse(bankFacade.transfer("ACC123", "ACC456", 300.0, "from@email.com", "to@email.com"));
        verify(notificationSystem).notifyTransactionFailure("ACC123", "Transferencia", 300.0, "from@email.com", "Fondos insuficientes");
    }
}