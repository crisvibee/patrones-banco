package com.example.unit.Command;

import com.example.Command.model.Command;
import com.example.Command.model.DepositCommand;
import com.example.Command.model.TransferCommand;
import com.example.Command.model.WithdrawCommand;
import com.example.Observer.model.ConcreteSubject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase base Command
 * Prueba el comportamiento común de todos los comandos
 */
public class CommandBaseUnitTest {
    
    @Test
    public void testCommandInterfaceContract() {
        // Arrange - Crear un comando concreto para probar la interfaz
        ConcreteSubject account = new ConcreteSubject("TEST001", 1000.0, "Cuenta Test");
        DepositCommand command = new DepositCommand(account, 300.0);
        
        // Act & Assert - Verificar que implementa todos los métodos requeridos
        assertNotNull(command.getCommandName(), "Todos los comandos deben tener un nombre");
        assertNotNull(command.getCommandDetails(), "Todos los comandos deben proporcionar detalles");
        
        // Verificar que el comando puede ejecutarse
        double initialBalance = account.getBalance();
        command.execute();
        assertEquals(initialBalance + 300.0, account.getBalance(), 0.001,
            "El comando debería ejecutarse correctamente");
        assertTrue(command.isExecuted(), "El comando debería estar marcado como ejecutado");
        
        // Verificar que el comando puede deshacerse
        command.undo();
        assertEquals(initialBalance, account.getBalance(), 0.001,
            "El comando debería poder deshacerse");
        assertFalse(command.isExecuted(), "El comando debería estar marcado como no ejecutado después de deshacer");
    }
    
    @Test
    public void testCommandNameConsistency() {
        // Arrange
        ConcreteSubject account = new ConcreteSubject("TEST001", 1000.0, "Cuenta Test");
        
        // Act & Assert - Verificar nombres consistentes para diferentes tipos de comandos
        Command depositCommand = new DepositCommand(account, 300.0);
        assertEquals("Depósito", depositCommand.getCommandName(), 
            "Nombre del comando de depósito debería ser consistente");
        
        Command withdrawCommand = new WithdrawCommand(account, 200.0);
        assertEquals("Retiro", withdrawCommand.getCommandName(),
            "Nombre del comando de retiro debería ser consistente");
        
        Command transferCommand = new TransferCommand(account, account, 100.0);
        assertEquals("Transferencia", transferCommand.getCommandName(),
            "Nombre del comando de transferencia debería ser consistente");
    }
    
    @Test
    public void testCommandDetailsFormat() {
        // Arrange
        ConcreteSubject account = new ConcreteSubject("TEST001", 1000.0, "Cuenta Test");
        Command depositCommand = new DepositCommand(account, 300.0);
        
        // Act
        String detailsBeforeExecution = depositCommand.getCommandDetails();
        depositCommand.execute();
        String detailsAfterExecution = depositCommand.getCommandDetails();
        
        // Assert
        assertTrue(detailsBeforeExecution.contains("Pendiente"),
            "Detalles antes de ejecución deberían indicar estado pendiente");
        assertTrue(detailsAfterExecution.contains("Ejecutado"),
            "Detalles después de ejecución deberían indicar estado ejecutado");
        assertTrue(detailsAfterExecution.contains("$300.00"),
            "Detalles deberían incluir el monto formateado");
        assertTrue(detailsAfterExecution.contains("TEST001"),
            "Detalles deberían incluir el número de cuenta");
    }
    
    @Test
    public void testCommandExecutionState() {
        // Arrange
        ConcreteSubject account = new ConcreteSubject("TEST001", 1000.0, "Cuenta Test");
        DepositCommand command = new DepositCommand(account, 300.0);
        double initialBalance = account.getBalance();
        
        // Act & Assert - Verificar estado inicial
        assertFalse(command.isExecuted(), "Comando no debería estar ejecutado inicialmente");
        
        // Verificar que ejecutar cambia el estado
        command.execute();
        assertEquals(initialBalance + 300.0, account.getBalance(), 0.001,
            "Ejecutar comando debería cambiar el balance");
        assertTrue(command.isExecuted(), "Comando debería estar marcado como ejecutado después de execute()");
        
        // Verificar que deshacer revierte el cambio
        command.undo();
        assertEquals(initialBalance, account.getBalance(), 0.001,
            "Deshacer comando debería revertir el balance");
        assertFalse(command.isExecuted(), "Comando debería estar marcado como no ejecutado después de deshacer");
        
        // Verificar que se puede ejecutar nuevamente
        command.execute();
        assertEquals(initialBalance + 300.0, account.getBalance(), 0.001,
            "Ejecutar comando nuevamente debería funcionar");
        assertTrue(command.isExecuted(), "Comando debería estar marcado como ejecutado después de ejecutar nuevamente");
    }
    
    @Test
    public void testCommandWithZeroAmount() {
        // Arrange
        ConcreteSubject account = new ConcreteSubject("TEST001", 1000.0, "Cuenta Test");
        DepositCommand command = new DepositCommand(account, 0.0);
        double initialBalance = account.getBalance();
        
        // Act
        command.execute();
        
        // Assert
        assertEquals(initialBalance, account.getBalance(), 0.001,
            "Comando con monto cero no debería cambiar el balance");
        assertTrue(command.isExecuted(), "Comando con monto cero debería marcarse como ejecutado");
    }
    
    @Test
    public void testCommandWithNegativeAmount() {
        // Arrange
        ConcreteSubject account = new ConcreteSubject("TEST001", 1000.0, "Cuenta Test");
        DepositCommand command = new DepositCommand(account, -100.0);
        double initialBalance = account.getBalance();
        
        // Act
        command.execute();
        
        // Assert
        assertEquals(initialBalance, account.getBalance(), 0.001,
            "Comando con monto negativo no debería cambiar el balance");
        assertTrue(command.isExecuted(), "Comando con monto negativo debería marcarse como ejecutado");
    }
    
    @Test
    public void testCommandDetailsIncludeAccountInfo() {
        // Arrange
        ConcreteSubject account = new ConcreteSubject("ACC123", 500.0, "Cuenta de Ahorros");
        Command command = new DepositCommand(account, 150.0);
        
        // Act
        String details = command.getCommandDetails();
        
        // Assert
        assertTrue(details.contains("ACC123"), "Detalles deberían incluir el número de cuenta");
        assertTrue(details.contains("Cuenta de Ahorros") || details.contains("ACC123"),
            "Detalles deberían incluir información de la cuenta");
    }
    
    @Test
    public void testCommandDetailsIncludeAmountInfo() {
        // Arrange
        ConcreteSubject account = new ConcreteSubject("TEST001", 1000.0, "Cuenta Test");
        Command command = new DepositCommand(account, 123.45);
        
        // Act
        String details = command.getCommandDetails();
        
        // Assert
        assertTrue(details.contains("123.45") || details.contains("$123.45"),
            "Detalles deberían incluir el monto específico");
    }
    
    @Test
    public void testCommandDetailsIncludeExecutionStatus() {
        // Arrange
        ConcreteSubject account = new ConcreteSubject("TEST001", 1000.0, "Cuenta Test");
        Command command = new DepositCommand(account, 300.0);
        
        // Act - Verificar antes de ejecución
        String detailsBefore = command.getCommandDetails();
        command.execute();
        String detailsAfter = command.getCommandDetails();
        
        // Assert
        assertTrue(detailsBefore.contains("Pendiente") || detailsBefore.contains("pendiente"),
            "Detalles antes de ejecución deberían indicar estado pendiente");
        assertTrue(detailsAfter.contains("Ejecutado") || detailsAfter.contains("ejecutado"),
            "Detalles después de ejecución deberían indicar estado ejecutado");
    }
}