package com.example.unit.Command;

import com.example.Command.model.WithdrawCommand;
import com.example.Observer.model.ConcreteSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el comando de retiro (WithdrawCommand)
 * Prueba la funcionalidad básica del comando de forma aislada
 */
public class WithdrawCommandUnitTest {
    
    private ConcreteSubject testAccount;
    private WithdrawCommand withdrawCommand;
    
    @BeforeEach
    public void setUp() {
        // Crear una cuenta de prueba con saldo inicial
        testAccount = new ConcreteSubject("TEST001", 1000.0, "Ahorros");
    }
    
    @Test
    public void testWithdrawCommandCreation() {
        // Arrange & Act
        withdrawCommand = new WithdrawCommand(testAccount, 500.0);
        
        // Assert
        assertNotNull(withdrawCommand, "El comando de retiro no debería ser nulo");
        assertEquals("Retiro", withdrawCommand.getCommandName(), "Nombre del comando incorrecto");
        assertEquals(500.0, withdrawCommand.getAmount(), 0.001, "Monto del retiro incorrecto");
        assertEquals(testAccount, withdrawCommand.getAccount(), "Cuenta objetivo incorrecta");
        assertFalse(withdrawCommand.isExecuted(), "El comando no debería estar ejecutado inicialmente");
    }
    
    @Test
    public void testWithdrawCommandExecution() {
        // Arrange
        withdrawCommand = new WithdrawCommand(testAccount, 500.0);
        double initialBalance = testAccount.getBalance();
        
        // Act
        withdrawCommand.execute();
        
        // Assert
        assertTrue(withdrawCommand.isExecuted(), "El comando debería estar marcado como ejecutado");
        assertEquals(initialBalance - 500.0, testAccount.getBalance(), 0.001, 
            "El saldo de la cuenta debería decrementarse después del retiro");
        
        // Verificar detalles del comando
        String details = withdrawCommand.getCommandDetails();
        assertTrue(details.contains("Retiro: $500.00"), "Detalles del comando deberían incluir el monto");
        assertTrue(details.contains("TEST001"), "Detalles del comando deberían incluir el número de cuenta");
        assertTrue(details.contains("Ejecutado"), "Detalles del comando deberían indicar estado ejecutado");
    }
    
    @Test
    public void testWithdrawCommandCannotExecuteTwice() {
        // Arrange
        withdrawCommand = new WithdrawCommand(testAccount, 500.0);
        double initialBalance = testAccount.getBalance();
        
        // Act - Ejecutar primera vez
        withdrawCommand.execute();
        double balanceAfterFirstExecution = testAccount.getBalance();
        
        // Ejecutar segunda vez (debería mostrar mensaje pero no cambiar el balance)
        withdrawCommand.execute();
        
        // Assert
        assertEquals(balanceAfterFirstExecution, testAccount.getBalance(), 0.001,
            "El balance no debería cambiar en ejecuciones adicionales");
    }
    
    @Test
    public void testWithdrawCommandWithZeroAmount() {
        // Arrange
        withdrawCommand = new WithdrawCommand(testAccount, 0.0);
        double initialBalance = testAccount.getBalance();
        
        // Act
        withdrawCommand.execute();
        
        // Assert
        assertEquals(initialBalance, testAccount.getBalance(), 0.001,
            "Retiro de $0 no debería cambiar el balance");
    }
    
    @Test
    public void testWithdrawCommandWithNegativeAmount() {
        // Arrange
        withdrawCommand = new WithdrawCommand(testAccount, -100.0);
        double initialBalance = testAccount.getBalance();
        
        // Act
        withdrawCommand.execute();
        
        // Assert
        assertEquals(initialBalance, testAccount.getBalance(), 0.001,
            "Retiro negativo no debería cambiar el balance (montos negativos son ignorados)");
    }
    
    @Test
    public void testWithdrawCommandWithInsufficientFunds() {
        // Arrange
        withdrawCommand = new WithdrawCommand(testAccount, 1500.0); // Más que el saldo disponible
        double initialBalance = testAccount.getBalance();
        
        // Act
        withdrawCommand.execute();
        
        // Assert
        assertEquals(initialBalance, testAccount.getBalance(), 0.001,
            "Retiro con fondos insuficientes no debería cambiar el balance");
        assertTrue(withdrawCommand.isExecuted(), "El comando debería estar marcado como ejecutado");
    }
    
    @Test
    public void testWithdrawCommandDetailsBeforeExecution() {
        // Arrange
        withdrawCommand = new WithdrawCommand(testAccount, 250.0);
        
        // Act
        String details = withdrawCommand.getCommandDetails();
        
        // Assert
        assertTrue(details.contains("Pendiente"), "Detalles deberían indicar estado pendiente antes de ejecución");
        assertFalse(details.contains("Ejecutado"), "Detalles no deberían indicar estado ejecutado antes de ejecución");
    }
}