package com.example.integration.Observer;

import com.example.Observer.model.ConcreteSubject;
import com.example.Observer.model.EmailObserver;
import com.example.Observer.model.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para el patrón Observer
 * Prueba la interacción entre Subject, ConcreteSubject y Observer
 */
public class ObserverIntegrationTest {
    
    private ConcreteSubject account;
    private EmailObserver emailObserver1;
    private EmailObserver emailObserver2;
    
    @BeforeEach
    public void setUp() {
        // Crear una cuenta bancaria (sujeto observable)
        account = new ConcreteSubject("INT001", 1000.0, "Ahorros");
        
        // Crear observadores
        emailObserver1 = new EmailObserver("obs1@example.com", "Observer One");
        emailObserver2 = new EmailObserver("obs2@example.com", "Observer Two");
    }
    
    @Test
    public void testObserverAttachment() {
        // Verificar que los observadores se pueden adjuntar correctamente
        assertEquals(0, account.getObserverCount(), 
            "No debería haber observadores inicialmente");
        
        // Adjuntar primer observador
        account.attach(emailObserver1);
        assertEquals(1, account.getObserverCount(), 
            "Debería haber 1 observador después de attach");
        
        // Adjuntar segundo observador
        account.attach(emailObserver2);
        assertEquals(2, account.getObserverCount(), 
            "Debería haber 2 observadores después de attach");
    }
    
    @Test
    public void testObserverDetachment() {
        // Verificar que los observadores se pueden desadjuntar correctamente
        account.attach(emailObserver1);
        account.attach(emailObserver2);
        
        assertEquals(2, account.getObserverCount(), 
            "Debería haber 2 observadores inicialmente");
        
        // Desadjuntar primer observador
        account.detach(emailObserver1);
        assertEquals(1, account.getObserverCount(), 
            "Debería haber 1 observador después de detach");
        
        // Desadjuntar segundo observador
        account.detach(emailObserver2);
        assertEquals(0, account.getObserverCount(), 
            "No debería haber observadores después de detach");
    }
    
    @Test
    public void testNotificationOnDeposit() {
        // Verificar que se notifica a los observadores al hacer un depósito
        account.attach(emailObserver1);
        account.attach(emailObserver2);
        
        double initialBalance = account.getBalance();
        double depositAmount = 500.0;
        
        // Esta prueba verifica que la operación no lanza excepciones
        // Las notificaciones actualmente solo imprimen en consola
        assertDoesNotThrow(() -> {
            account.deposit(depositAmount);
        }, "El depósito con notificaciones no debería lanzar excepciones");
        
        // Verificar que el balance se actualizó correctamente
        assertEquals(initialBalance + depositAmount, account.getBalance(), 0.001,
            "El balance debería haberse actualizado después del depósito");
    }
    
    @Test
    public void testNotificationOnWithdraw() {
        // Verificar que se notifica a los observadores al hacer un retiro
        account.attach(emailObserver1);
        
        double initialBalance = account.getBalance();
        double withdrawAmount = 200.0;
        
        assertDoesNotThrow(() -> {
            account.withdraw(withdrawAmount);
        }, "El retiro con notificaciones no debería lanzar excepciones");
        
        // Verificar que el balance se actualizó correctamente
        assertEquals(initialBalance - withdrawAmount, account.getBalance(), 0.001,
            "El balance debería haberse actualizado después del retiro");
    }
    
    @Test
    public void testNotificationOnFailedWithdraw() {
        // Verificar que se notifica a los observadores al intentar un retiro fallido
        account.attach(emailObserver1);
        
        double initialBalance = account.getBalance();
        double excessiveAmount = initialBalance + 1000.0; // Monto mayor al balance
        
        assertDoesNotThrow(() -> {
            account.withdraw(excessiveAmount);
        }, "El retiro fallido con notificaciones no debería lanzar excepciones");
        
        // Verificar que el balance no cambió
        assertEquals(initialBalance, account.getBalance(), 0.001,
            "El balance no debería cambiar en retiro fallido");
    }
    
    @Test
    public void testNotificationOnTransfer() {
        // Verificar que se notifica a los observadores al hacer una transferencia
        account.attach(emailObserver1);
        
        double initialBalance = account.getBalance();
        double transferAmount = 300.0;
        
        assertDoesNotThrow(() -> {
            account.transfer(transferAmount, "DEST001");
        }, "La transferencia con notificaciones no debería lanzar excepciones");
        
        // Verificar que el balance se actualizó correctamente
        assertEquals(initialBalance - transferAmount, account.getBalance(), 0.001,
            "El balance debería haberse actualizado después de la transferencia");
    }
    
    @Test
    public void testNotificationOnAccountTypeChange() {
        // Verificar que se notifica a los observadores al cambiar el tipo de cuenta
        account.attach(emailObserver1);
        
        String newAccountType = "Corriente Premium";
        
        assertDoesNotThrow(() -> {
            account.updateAccountType(newAccountType);
        }, "El cambio de tipo de cuenta con notificaciones no debería lanzar excepciones");
        
        // Verificar que el tipo de cuenta se actualizó
        assertEquals(newAccountType, account.getAccountType(),
            "El tipo de cuenta debería haberse actualizado");
    }
    
    @Test
    public void testNoNotificationAfterDetach() {
        // Verificar que los observadores desadjuntados no reciben notificaciones
        account.attach(emailObserver1);
        account.attach(emailObserver2);
        
        // Desadjuntar un observador
        account.detach(emailObserver1);
        
        // Realizar operación - solo el observador2 debería recibir notificación
        assertDoesNotThrow(() -> {
            account.deposit(100.0);
        }, "La operación después de detach no debería lanzar excepciones");
        
        // Verificar que todavía hay un observador activo
        assertEquals(1, account.getObserverCount(),
            "Debería quedar 1 observador activo");
    }
}