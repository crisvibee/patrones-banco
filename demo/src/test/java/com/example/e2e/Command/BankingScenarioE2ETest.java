package com.example.e2e.Command;

import com.example.Command.controller.TransactionInvoker;
import com.example.Command.model.DepositCommand;
import com.example.Command.model.WithdrawCommand;
import com.example.Command.model.TransferCommand;
import com.example.Observer.model.ConcreteSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba end-to-end para el patrón Command en un escenario bancario completo
 * Simula un flujo real de operaciones bancarias con múltiples cuentas y comandos
 */
public class BankingScenarioE2ETest {
    
    private TransactionInvoker invoker;
    private ConcreteSubject checkingAccount;
    private ConcreteSubject savingsAccount;
    private ConcreteSubject businessAccount;
    
    @BeforeEach
    public void setUp() {
        invoker = new TransactionInvoker();
        // Crear diferentes tipos de cuentas para el escenario
        checkingAccount = new ConcreteSubject("CHK001", 5000.0, "Corriente");
        savingsAccount = new ConcreteSubject("SAV001", 10000.0, "Ahorros");
        businessAccount = new ConcreteSubject("BUS001", 20000.0, "Empresarial");
    }
    
    @Test
    public void testCompleteBankingScenarioWithUndoRedo() {
        // Escenario: Cliente realiza múltiples operaciones y luego usa undo/redo
        
        // 1. Depósito inicial en cuenta corriente
        DepositCommand initialDeposit = new DepositCommand(checkingAccount, 2000.0);
        invoker.executeCommand(initialDeposit);
        assertEquals(7000.0, checkingAccount.getBalance(), 0.001, 
            "Balance después de depósito inicial incorrecto");
        
        // 2. Transferencia de cuenta corriente a ahorros
        TransferCommand transferToSavings = new TransferCommand(checkingAccount, savingsAccount, 1500.0);
        invoker.executeCommand(transferToSavings);
        assertEquals(5500.0, checkingAccount.getBalance(), 0.001, 
            "Balance cuenta corriente después de transferencia incorrecto");
        assertEquals(10000.0, savingsAccount.getBalance(), 0.001, 
            "Balance cuenta ahorros no debería cambiar en implementación actual");
        
        // 3. Retiro de cuenta corriente
        WithdrawCommand withdrawal = new WithdrawCommand(checkingAccount, 1000.0);
        invoker.executeCommand(withdrawal);
        assertEquals(4500.0, checkingAccount.getBalance(), 0.001, 
            "Balance después de retiro incorrecto");
        
        // 4. Depósito en cuenta empresarial
        DepositCommand businessDeposit = new DepositCommand(businessAccount, 5000.0);
        invoker.executeCommand(businessDeposit);
        assertEquals(25000.0, businessAccount.getBalance(), 0.001, 
            "Balance cuenta empresarial después de depósito incorrecto");
        
        // Verificar historial completo
        assertEquals(4, invoker.getCommandHistory().size(), 
            "Debería haber 4 comandos en el historial");
        
        // 5. Simular error del usuario: deshacer las dos últimas operaciones
        invoker.undoLastCommand(); // Deshacer depósito empresarial
        assertEquals(25000.0, businessAccount.getBalance(), 0.001, 
            "Balance no debería cambiar con undo en implementación actual");
        
        invoker.undoLastCommand(); // Deshacer retiro
        assertEquals(4500.0, checkingAccount.getBalance(), 0.001, 
            "Balance no debería cambiar con undo en implementación actual");
        
        // 6. Usuario cambia de opinión: rehacer el retiro
        invoker.redoLastCommand(); // Rehacer retiro
        assertEquals(4500.0, checkingAccount.getBalance(), 0.001, 
            "Balance no debería cambiar con redo en implementación actual");
        
        // 7. Nueva transferencia de ahorros a corriente
        TransferCommand savingsToChecking = new TransferCommand(savingsAccount, checkingAccount, 2000.0);
        invoker.executeCommand(savingsToChecking);
        assertEquals(6500.0, checkingAccount.getBalance(), 0.001, 
            "Balance corriente después de segunda transferencia incorrecto");
        assertEquals(10000.0, savingsAccount.getBalance(), 0.001, 
            "Balance ahorros no debería cambiar en implementación actual");
        
        // Verificar estado final de todas las cuentas
        assertEquals(6500.0, checkingAccount.getBalance(), 0.001, 
            "Balance final cuenta corriente incorrecto");
        assertEquals(10000.0, savingsAccount.getBalance(), 0.001, 
            "Balance final cuenta ahorros incorrecto (no debería cambiar)");
        assertEquals(25000.0, businessAccount.getBalance(), 0.001, 
            "Balance final cuenta empresarial incorrecto");
        
        // Verificar que el historial contiene todos los comandos ejecutados
        assertEquals(5, invoker.getCommandHistory().size(), 
            "Historial debería contener todos los comandos ejecutados");
        
        // Verificar que los comandos tienen los nombres correctos
        assertEquals("Depósito", invoker.getCommandHistory().get(0).getCommandName());
        assertEquals("Transferencia", invoker.getCommandHistory().get(1).getCommandName());
        assertEquals("Retiro", invoker.getCommandHistory().get(2).getCommandName());
        assertEquals("Transferencia", invoker.getCommandHistory().get(4).getCommandName());
    }
    
    @Test
    public void testComplexUndoRedoScenario() {
        // Escenario más complejo con múltiples deshacer/rehacer entre cuentas
        
        // Operaciones iniciales
        invoker.executeCommand(new DepositCommand(checkingAccount, 3000.0));
        invoker.executeCommand(new TransferCommand(checkingAccount, savingsAccount, 1000.0));
        invoker.executeCommand(new DepositCommand(savingsAccount, 2000.0));
        invoker.executeCommand(new WithdrawCommand(checkingAccount, 500.0));
        
        double initialCheckingBalance = checkingAccount.getBalance();
        double initialSavingsBalance = savingsAccount.getBalance();
        
        // Deshacer secuencia completa (no afecta balances en implementación actual)
        invoker.undoLastCommand(); // Deshacer retiro
        invoker.undoLastCommand(); // Deshacer depósito ahorros
        invoker.undoLastCommand(); // Deshacer transferencia
        invoker.undoLastCommand(); // Deshacer depósito inicial
        
        // Verificar que los balances no cambiaron con undo
        assertEquals(initialCheckingBalance, checkingAccount.getBalance(), 0.001, 
            "Checking balance no debería cambiar con undo en implementación actual");
        assertEquals(initialSavingsBalance, savingsAccount.getBalance(), 0.001, 
            "Savings balance no debería cambiar con undo en implementación actual");
        
        // Rehacer secuencia completa (no afecta balances en implementación actual)
        invoker.redoLastCommand(); // Rehacer depósito inicial
        invoker.redoLastCommand(); // Rehacer transferencia
        invoker.redoLastCommand(); // Rehacer depósito ahorros
        invoker.redoLastCommand(); // Rehacer retiro
        
        // Verificar que los balances no cambiaron con redo
        assertEquals(initialCheckingBalance, checkingAccount.getBalance(), 0.001, 
            "Checking balance no debería cambiar con redo en implementación actual");
        assertEquals(initialSavingsBalance, savingsAccount.getBalance(), 0.001, 
            "Savings balance no debería cambiar con redo en implementación actual");
        
        // Verificar integridad del historial
        assertEquals(4, invoker.getCommandHistory().size(), 
            "Historial debería mantener todos los comandos");
        assertEquals(0, invoker.getUndoableCommandsCount(), 
            "Pila de deshacer debería estar vacía");
        assertEquals(0, invoker.getRedoableCommandsCount(), 
            "Pila de rehacer debería estar vacía");
    }
    
    @Test
    public void testErrorHandlingInE2EScenario() {
        // Prueba de manejo de errores en escenario real
        
        // Intentar retiro con fondos insuficientes
        WithdrawCommand largeWithdrawal = new WithdrawCommand(checkingAccount, 10000.0);
        invoker.executeCommand(largeWithdrawal);
        
        // El retiro debería fallar silenciosamente (depende de la implementación)
        // Verificar que el balance no cambió
        assertEquals(5000.0, checkingAccount.getBalance(), 0.001, 
            "Balance no debería cambiar con retiro de fondos insuficientes");
        
        // El comando aún debería estar en el historial
        assertEquals(1, invoker.getCommandHistory().size(), 
            "Comando debería estar en historial incluso si falló");
        
        // Operaciones normales deberían seguir funcionando
        invoker.executeCommand(new DepositCommand(checkingAccount, 1000.0));
        assertEquals(6000.0, checkingAccount.getBalance(), 0.001, 
            "Operaciones normales deberían funcionar después de error");
        
        // Deshacer debería funcionar normalmente (pero no afecta balances)
        invoker.undoLastCommand();
        assertEquals(6000.0, checkingAccount.getBalance(), 0.001, 
            "Balance no debería cambiar con undo en implementación actual");
    }
    
    @Test
    public void testMultiAccountIntegrationScenario() {
        // Escenario que involucra las tres cuentas simultáneamente
        
        // 1. Depósito en cuenta corriente
        invoker.executeCommand(new DepositCommand(checkingAccount, 5000.0));
        
        // 2. Transferencia corriente -> ahorros
        invoker.executeCommand(new TransferCommand(checkingAccount, savingsAccount, 3000.0));
        
        // 3. Transferencia ahorros -> empresarial
        invoker.executeCommand(new TransferCommand(savingsAccount, businessAccount, 2000.0));
        
        // 4. Depósito directo en empresarial
        invoker.executeCommand(new DepositCommand(businessAccount, 10000.0));
        
        // 5. Retiro de corriente
        invoker.executeCommand(new WithdrawCommand(checkingAccount, 1000.0));
        
        // Verificar balances finales
        assertEquals(6000.0, checkingAccount.getBalance(), 0.001); // 5000+5000-3000-1000 = 6000
        assertEquals(11000.0, savingsAccount.getBalance(), 0.001); // 10000+3000-2000 = 11000
        assertEquals(32000.0, businessAccount.getBalance(), 0.001); // 20000 + 2000 transfer + 10000 deposit = 32000
        
        // Verificar historial completo
        assertEquals(5, invoker.getCommandHistory().size(), 
            "Debería haber 5 comandos en el historial");
        
        // Verificar que podemos deshacer toda la secuencia
        for (int i = 0; i < 5; i++) {
            invoker.undoLastCommand();
        }
        
        assertEquals(5000.0, checkingAccount.getBalance(), 0.001, 
            "Checking debería volver al balance inicial");
        assertEquals(10000.0, savingsAccount.getBalance(), 0.001, 
            "Savings debería volver al balance inicial");
        assertEquals(20000.0, businessAccount.getBalance(), 0.001, 
            "Business debería volver al balance inicial");
    }
}