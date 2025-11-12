package com.example.unit.Command;

import com.example.Command.controller.TransactionInvoker;
import com.example.Command.model.Command;
import com.example.Command.model.DepositCommand;
import com.example.Command.model.WithdrawCommand;
import com.example.Command.model.TransferCommand;
import com.example.Observer.model.ConcreteSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el TransactionInvoker
 * Prueba la gestión de comandos y la funcionalidad de deshacer/rehacer
 */
public class TransactionInvokerUnitTest {
    
    private TransactionInvoker transactionInvoker;
    private ConcreteSubject testAccount;
    private ConcreteSubject fromAccount;
    private ConcreteSubject toAccount;
    
    @BeforeEach
    public void setUp() {
        transactionInvoker = new TransactionInvoker();
        testAccount = new ConcreteSubject("TEST001", 1000.0, "Cuenta Test");
        fromAccount = new ConcreteSubject("FROM001", 1000.0, "Cuenta Origen");
        toAccount = new ConcreteSubject("TO002", 500.0, "Cuenta Destino");
    }
    
    @Test
    public void testTransactionInvokerCreation() {
        // Arrange & Act - Ya creado en setUp
        
        // Assert
        assertNotNull(transactionInvoker, "El TransactionInvoker no debería ser nulo");
        assertTrue(transactionInvoker.getCommandHistory().isEmpty(), 
            "El historial de comandos debería estar vacío inicialmente");
        assertEquals(0, transactionInvoker.getUndoableCommandsCount(),
            "La pila de comandos para deshacer debería estar vacía inicialmente");
        assertEquals(0, transactionInvoker.getRedoableCommandsCount(),
            "La pila de comandos para rehacer debería estar vacía inicialmente");
    }
    
    @Test
    public void testExecuteCommand() {
        // Arrange
        Command depositCommand = new DepositCommand(testAccount, 300.0);
        double initialBalance = testAccount.getBalance();
        
        // Act
        transactionInvoker.executeCommand(depositCommand);
        
        // Assert
        assertEquals(initialBalance + 300.0, testAccount.getBalance(), 0.001,
            "El comando debería ejecutarse correctamente");
        assertEquals(1, transactionInvoker.getCommandHistory().size(),
            "El historial debería contener un comando");
        assertEquals(1, transactionInvoker.getUndoableCommandsCount(),
            "La pila de comandos para deshacer debería contener un comando");
        assertEquals(0, transactionInvoker.getRedoableCommandsCount(),
            "La pila de comandos para rehacer debería seguir vacía");
        assertEquals(depositCommand, transactionInvoker.getCommandHistory().get(0),
            "El comando ejecutado debería estar en el historial");
    }
    
    @Test
    public void testExecuteMultipleCommands() {
        // Arrange
        Command depositCommand = new DepositCommand(testAccount, 300.0);
        Command withdrawCommand = new WithdrawCommand(testAccount, 200.0);
        double initialBalance = testAccount.getBalance();
        
        // Act
        transactionInvoker.executeCommand(depositCommand);
        transactionInvoker.executeCommand(withdrawCommand);
        
        // Assert
        assertEquals(initialBalance + 100.0, testAccount.getBalance(), 0.001,
            "El balance final debería reflejar ambas operaciones");
        assertEquals(2, transactionInvoker.getCommandHistory().size(),
            "El historial debería contener dos comandos");
        assertEquals(depositCommand, transactionInvoker.getCommandHistory().get(0),
            "Primer comando debería estar en posición 0");
        assertEquals(withdrawCommand, transactionInvoker.getCommandHistory().get(1),
            "Segundo comando debería estar en posición 1");
    }
    
    @Test
    public void testUndoLastCommand() {
        // Arrange
        Command depositCommand = new DepositCommand(testAccount, 300.0);
        double initialBalance = testAccount.getBalance();
        transactionInvoker.executeCommand(depositCommand);
        double balanceAfterDeposit = testAccount.getBalance();
        
        // Act
        transactionInvoker.undoLastCommand();
        
        // Assert
        assertEquals(initialBalance, testAccount.getBalance(), 0.001,
            "Deshacer debería revertir el comando");
        assertEquals(1, transactionInvoker.getCommandHistory().size(),
            "El historial debería mantener el comando después de deshacer");
        assertEquals(0, transactionInvoker.getUndoableCommandsCount(),
            "La pila de comandos para deshacer debería estar vacía después de deshacer");
        assertEquals(1, transactionInvoker.getRedoableCommandsCount(),
            "La pila de comandos para rehacer debería contener un comando");
    }
    
    @Test
    public void testRedoLastCommand() {
        // Arrange
        Command depositCommand = new DepositCommand(testAccount, 300.0);
        double initialBalance = testAccount.getBalance();
        transactionInvoker.executeCommand(depositCommand);
        transactionInvoker.undoLastCommand();
        double balanceAfterUndo = testAccount.getBalance();
        
        // Act
        transactionInvoker.redoLastCommand();
        
        // Assert
        assertEquals(initialBalance + 300.0, testAccount.getBalance(), 0.001,
            "Rehacer debería ejecutar el comando nuevamente");
        assertEquals(1, transactionInvoker.getCommandHistory().size(),
            "El historial debería contener el comando re-hecho");
        assertEquals(1, transactionInvoker.getUndoableCommandsCount(),
            "La pila de comandos para deshacer debería contener el comando rehecho");
        assertEquals(0, transactionInvoker.getRedoableCommandsCount(),
            "La pila de comandos para rehacer debería estar vacía después de rehacer");
        assertEquals(depositCommand, transactionInvoker.getCommandHistory().get(0),
            "El comando re-hecho debería estar en el historial");
    }
    
    @Test
    public void testUndoWithEmptyHistory() {
        // Arrange - historial vacío
        
        // Act
        transactionInvoker.undoLastCommand();
        
        // Assert - No debería lanzar excepción y el estado debería permanecer igual
        assertTrue(transactionInvoker.getCommandHistory().isEmpty(),
            "El historial debería seguir vacío");
        assertEquals(0, transactionInvoker.getUndoableCommandsCount(),
            "La pila de comandos para deshacer debería seguir vacía");
        assertEquals(0, transactionInvoker.getRedoableCommandsCount(),
            "La pila de comandos para rehacer debería seguir vacía");
    }
    
    @Test
    public void testRedoWithEmptyRedoStack() {
        // Arrange - pila de rehacer vacía
        
        // Act
        transactionInvoker.redoLastCommand();
        
        // Assert - No debería lanzar excepción y el estado debería permanecer igual
        assertTrue(transactionInvoker.getCommandHistory().isEmpty(),
            "El historial debería seguir vacío");
        assertEquals(0, transactionInvoker.getUndoableCommandsCount(),
            "La pila de comandos para deshacer debería seguir vacía");
        assertEquals(0, transactionInvoker.getRedoableCommandsCount(),
            "La pila de comandos para rehacer debería seguir vacía");
    }
    
    @Test
    public void testUndoMultipleCommands() {
        // Arrange
        Command depositCommand = new DepositCommand(testAccount, 300.0);
        Command withdrawCommand = new WithdrawCommand(testAccount, 200.0);
        double initialBalance = testAccount.getBalance();
        transactionInvoker.executeCommand(depositCommand);
        transactionInvoker.executeCommand(withdrawCommand);
        
        // Act
        transactionInvoker.undoLastCommand(); // Deshacer retiro
        transactionInvoker.undoLastCommand(); // Deshacer depósito
        
        // Assert
        assertEquals(initialBalance, testAccount.getBalance(), 0.001,
            "Deshacer ambos comandos debería dejar el balance inicial");
        assertEquals(2, transactionInvoker.getCommandHistory().size(),
            "El historial debería contener ambos comandos");
        assertEquals(0, transactionInvoker.getUndoableCommandsCount(),
            "La pila de comandos para deshacer debería estar vacía");
        assertEquals(2, transactionInvoker.getRedoableCommandsCount(),
            "La pila de comandos para rehacer debería contener dos comandos");
    }
    
    @Test
    public void testRedoMultipleCommands() {
        // Arrange
        Command depositCommand = new DepositCommand(testAccount, 300.0);
        Command withdrawCommand = new WithdrawCommand(testAccount, 200.0);
        double initialBalance = testAccount.getBalance();
        transactionInvoker.executeCommand(depositCommand);
        transactionInvoker.executeCommand(withdrawCommand);
        transactionInvoker.undoLastCommand(); // Deshacer retiro
        transactionInvoker.undoLastCommand(); // Deshacer depósito
        
        // Act
        transactionInvoker.redoLastCommand(); // Rehacer depósito
        transactionInvoker.redoLastCommand(); // Rehacer retiro
        
        // Assert
        assertEquals(initialBalance + 100.0, testAccount.getBalance(), 0.001,
            "Rehacer ambos comandos debería dejar el balance final correcto");
        assertEquals(2, transactionInvoker.getCommandHistory().size(),
            "El historial debería contener dos comandos");
        assertEquals(2, transactionInvoker.getUndoableCommandsCount(),
            "La pila de comandos para deshacer debería contener dos comandos");
        assertEquals(0, transactionInvoker.getRedoableCommandsCount(),
            "La pila de comandos para rehacer debería estar vacía");
    }
    
    @Test
    public void testExecuteAfterUndoClearsRedoStack() {
        // Arrange
        Command depositCommand = new DepositCommand(testAccount, 300.0);
        Command withdrawCommand = new WithdrawCommand(testAccount, 200.0);
        transactionInvoker.executeCommand(depositCommand);
        transactionInvoker.undoLastCommand();
        
        // Act - Ejecutar nuevo comando después de deshacer
        transactionInvoker.executeCommand(withdrawCommand);
        
        // Assert
        assertEquals(2, transactionInvoker.getCommandHistory().size(),
            "El historial debería contener ambos comandos ejecutados");
        assertEquals(1, transactionInvoker.getUndoableCommandsCount(),
            "La pila de comandos para deshacer debería contener el comando reciente");
        assertEquals(0, transactionInvoker.getRedoableCommandsCount(),
            "La pila de comandos para rehacer debería estar vacía (limpiada por nuevo comando)");
        assertEquals(depositCommand, transactionInvoker.getCommandHistory().get(0),
            "El primer comando debería estar en posición 0 del historial");
        assertEquals(withdrawCommand, transactionInvoker.getCommandHistory().get(1),
            "El comando reciente debería estar en posición 1 del historial");
    }
    
    @Test
    public void testTransferCommandExecutionThroughInvoker() {
        // Arrange
        Command transferCommand = new TransferCommand(fromAccount, toAccount, 300.0);
        double initialFromBalance = fromAccount.getBalance();
        double initialToBalance = toAccount.getBalance();
        
        // Act
        transactionInvoker.executeCommand(transferCommand);
        
        // Assert
        assertEquals(initialFromBalance - 300.0, fromAccount.getBalance(), 0.001,
            "Transferencia debería decrementar balance origen");
        assertEquals(initialToBalance + 300.0, toAccount.getBalance(), 0.001,
            "Transferencia debería incrementar balance destino");
        assertEquals(1, transactionInvoker.getCommandHistory().size(),
            "El historial debería contener el comando de transferencia");
    }
    
    @Test
    public void testGetCommandHistoryReturnsCopy() {
        // Arrange
        Command depositCommand = new DepositCommand(testAccount, 300.0);
        transactionInvoker.executeCommand(depositCommand);
        
        // Act
        var history = transactionInvoker.getCommandHistory();
        history.clear(); // Esto no debería afectar el historial interno
        
        // Assert
        assertEquals(1, transactionInvoker.getCommandHistory().size(),
            "El historial interno no debería verse afectado por modificaciones externas");
    }
    

}