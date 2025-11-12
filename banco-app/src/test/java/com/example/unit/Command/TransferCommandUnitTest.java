package test.java.com.example.unit.Command;

import com.example.Command.model.TransferCommand;
import com.example.Observer.model.ConcreteSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el comando de transferencia (TransferCommand)
 * Prueba la funcionalidad básica del comando de forma aislada
 */
public class TransferCommandUnitTest {
    
    private ConcreteSubject fromAccount;
    private ConcreteSubject toAccount;
    private TransferCommand transferCommand;
    
    @BeforeEach
    public void setUp() {
        // Crear cuentas de prueba con saldos iniciales
        fromAccount = new ConcreteSubject("FROM001", 1000.0, "Cuenta Origen");
        toAccount = new ConcreteSubject("TO002", 500.0, "Cuenta Destino");
    }
    
    @Test
    public void testTransferCommandCreation() {
        // Arrange & Act
        transferCommand = new TransferCommand(fromAccount, toAccount, 300.0);
        
        // Assert
        assertNotNull(transferCommand, "El comando de transferencia no debería ser nulo");
        assertEquals("Transferencia", transferCommand.getCommandName(), "Nombre del comando incorrecto");
        assertEquals(300.0, transferCommand.getAmount(), 0.001, "Monto de la transferencia incorrecto");
        assertEquals(fromAccount, transferCommand.getSourceAccount(), "Cuenta origen incorrecta");
        assertEquals(toAccount, transferCommand.getDestinationAccount(), "Cuenta destino incorrecta");
        assertFalse(transferCommand.isExecuted(), "El comando no debería estar ejecutado inicialmente");
    }
    
    @Test
    public void testTransferCommandExecution() {
        // Arrange
        transferCommand = new TransferCommand(fromAccount, toAccount, 300.0);
        double initialFromBalance = fromAccount.getBalance();
        double initialToBalance = toAccount.getBalance();
        
        // Act
        transferCommand.execute();
        
        // Assert
        assertTrue(transferCommand.isExecuted(), "El comando debería estar marcado como ejecutado");
        assertEquals(initialFromBalance - 300.0, fromAccount.getBalance(), 0.001, 
            "El saldo de la cuenta origen debería decrementarse después de la transferencia");
        assertEquals(initialToBalance + 300.0, toAccount.getBalance(), 0.001,
            "El saldo de la cuenta destino debería incrementarse después de la transferencia");
        
        // Verificar detalles del comando
        String details = transferCommand.getCommandDetails();
        assertTrue(details.contains("Transferencia: $300.00"), "Detalles del comando deberían incluir el monto");
        assertTrue(details.contains("FROM001"), "Detalles del comando deberían incluir la cuenta origen");
        assertTrue(details.contains("TO002"), "Detalles del comando deberían incluir la cuenta destino");
        assertTrue(details.contains("Ejecutado"), "Detalles del comando deberían indicar estado ejecutado");
    }
    
    @Test
    public void testTransferCommandCannotExecuteTwice() {
        // Arrange
        transferCommand = new TransferCommand(fromAccount, toAccount, 300.0);
        double initialFromBalance = fromAccount.getBalance();
        double initialToBalance = toAccount.getBalance();
        
        // Act - Ejecutar primera vez
        transferCommand.execute();
        double fromBalanceAfterFirst = fromAccount.getBalance();
        double toBalanceAfterFirst = toAccount.getBalance();
        
        // Ejecutar segunda vez (debería mostrar mensaje pero no cambiar los balances)
        transferCommand.execute();
        
        // Assert
        assertEquals(fromBalanceAfterFirst, fromAccount.getBalance(), 0.001,
            "El balance de origen no debería cambiar en ejecuciones adicionales");
        assertEquals(toBalanceAfterFirst, toAccount.getBalance(), 0.001,
            "El balance de destino no debería cambiar en ejecuciones adicionales");
    }
    
    @Test
    public void testTransferCommandWithZeroAmount() {
        // Arrange
        transferCommand = new TransferCommand(fromAccount, toAccount, 0.0);
        double initialFromBalance = fromAccount.getBalance();
        double initialToBalance = toAccount.getBalance();
        
        // Act
        transferCommand.execute();
        
        // Assert
        assertEquals(initialFromBalance, fromAccount.getBalance(), 0.001,
            "Transferencia de $0 no debería cambiar el balance de origen");
        assertEquals(initialToBalance, toAccount.getBalance(), 0.001,
            "Transferencia de $0 no debería cambiar el balance de destino");
    }
    
    @Test
    public void testTransferCommandWithNegativeAmount() {
        // Arrange
        transferCommand = new TransferCommand(fromAccount, toAccount, -100.0);
        double initialFromBalance = fromAccount.getBalance();
        double initialToBalance = toAccount.getBalance();
        
        // Act
        transferCommand.execute();
        
        // Assert
        assertEquals(initialFromBalance, fromAccount.getBalance(), 0.001,
            "Transferencia negativa no debería cambiar el balance de origen");
        assertEquals(initialToBalance, toAccount.getBalance(), 0.001,
            "Transferencia negativa no debería cambiar el balance de destino");
    }
    
    @Test
    public void testTransferCommandWithInsufficientFunds() {
        // Arrange
        transferCommand = new TransferCommand(fromAccount, toAccount, 1500.0); // Más que el saldo disponible
        double initialFromBalance = fromAccount.getBalance();
        double initialToBalance = toAccount.getBalance();
        
        // Act
        transferCommand.execute();
        
        // Assert
        assertEquals(initialFromBalance, fromAccount.getBalance(), 0.001,
            "Transferencia con fondos insuficientes no debería cambiar el balance de origen");
        assertEquals(initialToBalance, toAccount.getBalance(), 0.001,
            "Transferencia con fondos insuficientes no debería cambiar el balance de destino");
        assertTrue(transferCommand.isExecuted(), "El comando debería estar marcado como ejecutado");
    }
    
    @Test
    public void testTransferCommandDetailsBeforeExecution() {
        // Arrange
        transferCommand = new TransferCommand(fromAccount, toAccount, 250.0);
        
        // Act
        String details = transferCommand.getCommandDetails();
        
        // Assert
        assertTrue(details.contains("Pendiente"), "Detalles deberían indicar estado pendiente antes de ejecución");
        assertFalse(details.contains("Ejecutado"), "Detalles no deberían indicar estado ejecutado antes de ejecución");
    }
    
    @Test
    public void testTransferCommandBetweenSameAccount() {
        // Arrange
        transferCommand = new TransferCommand(fromAccount, fromAccount, 300.0);
        double initialBalance = fromAccount.getBalance();
        
        // Act
        transferCommand.execute();
        
        // Assert
        assertEquals(initialBalance, fromAccount.getBalance(), 0.001,
            "Transferencia a la misma cuenta no debería cambiar el balance");
        assertTrue(transferCommand.isExecuted(), "El comando debería estar marcado como ejecutado");
    }
}