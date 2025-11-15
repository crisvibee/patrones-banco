package test.java.com.example.integration.Facade;

import com.example.Facade.model.NotificationSystemImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationSystemIntegrationTest {

    private NotificationSystemImpl notificationSystem;

    @BeforeEach
    void setUp() {
        notificationSystem = new NotificationSystemImpl();
    }

    @Test
    void testSendEmail() {
       
        assertDoesNotThrow(() -> {
            notificationSystem.sendEmail("test@example.com", "Asunto de prueba", "Mensaje de prueba");
        });
    }

    @Test
    void testSendSMS() {
        
        assertDoesNotThrow(() -> {
            notificationSystem.sendSMS("+1234567890", "Mensaje SMS de prueba");
        });
    }

    @Test
    void testSendPushNotification() {
       
        assertDoesNotThrow(() -> {
            notificationSystem.sendPushNotification("device123", "Título push", "Cuerpo del mensaje push");
        });
    }

    @Test
    void testNotifyTransactionSuccess_Deposit() {
      
        assertDoesNotThrow(() -> {
            notificationSystem.notifyTransactionSuccess("ACC123", "Depósito", 500.0, "cliente@example.com");
        });
    }

    @Test
    void testNotifyTransactionSuccess_Withdraw() {
     
        assertDoesNotThrow(() -> {
            notificationSystem.notifyTransactionSuccess("ACC123", "Retiro", 200.0, "cliente@example.com");
        });
    }

    @Test
    void testNotifyTransactionSuccess_Transfer() {
     
        assertDoesNotThrow(() -> {
            notificationSystem.notifyTransactionSuccess("ACC123", "Transferencia", 300.0, "cliente@example.com");
        });
    }

    @Test
    void testNotifyTransactionFailure_Deposit() {
  
        assertDoesNotThrow(() -> {
            notificationSystem.notifyTransactionFailure("ACC123", "Depósito", 500.0, "cliente@example.com", "Fondos insuficientes");
        });
    }

    @Test
    void testNotifyTransactionFailure_Withdraw() {

        assertDoesNotThrow(() -> {
            notificationSystem.notifyTransactionFailure("ACC123", "Retiro", 200.0, "cliente@example.com", "Cuenta no existe");
        });
    }

    @Test
    void testNotifyTransactionFailure_Transfer() {
     
        assertDoesNotThrow(() -> {
            notificationSystem.notifyTransactionFailure("ACC123", "Transferencia", 300.0, "cliente@example.com", "Error en procesamiento");
        });
    }

    @Test
    void testNotifyAccountCreation() {

        assertDoesNotThrow(() -> {
            notificationSystem.notifyAccountCreation("ACC123", "Juan Pérez", 1000.0, "juan@example.com");
        });
    }

    @Test
    void testMultipleNotifications() {
     
        assertDoesNotThrow(() -> {
            notificationSystem.notifyAccountCreation("ACC123", "Cliente 1", 1000.0, "cliente1@example.com");
            notificationSystem.notifyTransactionSuccess("ACC123", "Depósito", 500.0, "cliente1@example.com");
            notificationSystem.notifyTransactionFailure("ACC456", "Transferencia", 300.0, "cliente2@example.com", "Cuenta destino no existe");
            notificationSystem.sendEmail("admin@example.com", "Reporte diario", "Resumen de actividades");
        });
    }
}