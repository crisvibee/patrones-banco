package com.example.unit.Command;

import com.example.Command.model.DepositCommand;
import com.example.Observer.model.ConcreteSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el comando de depósito (DepositCommand)
 * Prueba la funcionalidad básica del comando de forma aislada
 */
public class DepositCommandUnitTest {
    
    private ConcreteSubject testAccount;
    private DepositCommand depositCommand;
    
    @BeforeEach
    public void setUp() {
        // Crear una cuenta de prueba con saldo inicial
        testAccount = new ConcreteSubject("TEST001", 1000.0, "Ahorros");
    }
    
    @Test
    public void testDepositCommandCreation() {
        // Arrange & Act
        depositCommand = new DepositCommand(testAccount, 500.0);
        
        // Assert
        assertNotNull(depositCommand, "El comando de depósito no debería ser nulo");
        assertEquals("Depósito", depositCommand.getCommandName(), "Nombre del comando incorrecto");
        assertEquals(500.0, depositCommand.getAmount(), 0.001, "Monto del depósito incorrecto");
        assertEquals(testAccount, depositCommand.getAccount(), "Cuenta objetivo incorrecta");
        assertFalse(depositCommand.isExecuted(), "El comando no debería estar ejecutado inicialmente");
    }
    
    @Test
    public void testDepositCommandExecution() {
        // Arrange
        depositCommand = new DepositCommand(testAccount, 500.0);
        double initialBalance = testAccount.getBalance();
        
        // Act
        depositCommand.execute();
        
        // Assert
        assertTrue(depositCommand.isExecuted(), "El comando debería estar marcado como ejecutado");
        assertEquals(initialBalance + 500.0, testAccount.getBalance(), 0.001, 
            "El saldo de la cuenta debería incrementarse después del depósito");
        
        // Verificar detalles del comando
        String details = depositCommand.getCommandDetails();
        assertTrue(details.contains("Depósito: $500.00"), "Detalles del comando deberían incluir el monto");
        assertTrue(details.contains("TEST001"), "Detalles del comando deberían incluir el número de cuenta");
        assertTrue(details.contains("Ejecutado"), "Detalles del comando deberían indicar estado ejecutado");
    }
    
    @Test
    public void testDepositCommandCannotExecuteTwice() {
        // Arrange
        depositCommand = new DepositCommand(testAccount, 500.0);
        double initialBalance = testAccount.getBalance();
        
        // Act - Ejecutar primera vez
        depositCommand.execute();
        double balanceAfterFirstExecution = testAccount.getBalance();
        
        // Ejecutar segunda vez (debería mostrar mensaje pero no cambiar el balance)
        depositCommand.execute();
        
        // Assert
        assertEquals(balanceAfterFirstExecution, testAccount.getBalance(), 0.001,
            "El balance no debería cambiar en ejecuciones adicionales");
    }
    
    @Test
    public void testDepositCommandWithZeroAmount() {
        // Arrange
        depositCommand = new DepositCommand(testAccount, 0.0);
        double initialBalance = testAccount.getBalance();
        
        // Act
        depositCommand.execute();
        
        // Assert
        assertEquals(initialBalance, testAccount.getBalance(), 0.001,
            "Depósito de $0 no debería cambiar el balance");
    }
    
    @Test
    public void testDepositCommandWithNegativeAmount() {
        // Arrange
        depositCommand = new DepositCommand(testAccount, -100.0);
        double initialBalance = testAccount.getBalance();
        
        // Act
        depositCommand.execute();
        
        // Assert
        assertEquals(initialBalance, testAccount.getBalance(), 0.001,
            "Depósito negativo no debería cambiar el balance (montos negativos son ignorados)");
    }
    
    @Test
    public void testDepositCommandDetailsBeforeExecution() {
        // Arrange
        depositCommand = new DepositCommand(testAccount, 250.0);
        
        // Act
        String details = depositCommand.getCommandDetails();
        
        // Assert
        assertTrue(details.contains("Pendiente"), "Detalles deberían indicar estado pendiente antes de ejecución");
        assertFalse(details.contains("Ejecutado"), "Detalles no deberían indicar estado ejecutado antes de ejecución");
    }
}