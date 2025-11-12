package test.java.com.example.e2e.Facade;

import com.example.Facade.BankFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas end-to-end (E2E) para BankFacade
 * Simula un escenario bancario completo desde la perspectiva del usuario final
 */
class BankFacadeE2ETest {

    private BankFacade bankFacade;

    @BeforeEach
    void setUp() {
        bankFacade = new BankFacade();
    }

    @Test
    void testCompleteBankingScenario_E2E() {
        System.out.println("=== INICIO ESCENARIO E2E BANCARIO ===");
        
        // Paso 1: Crear cuentas para dos clientes
        System.out.println("1. Creando cuentas para clientes...");
        String account1 = bankFacade.createAccount("Juan Pérez", 1000.0, "juan@example.com");
        String account2 = bankFacade.createAccount("María García", 500.0, "maria@example.com");
        
        assertNotNull(account1);
        assertNotNull(account2);
        assertTrue(bankFacade.verifyAccount(account1));
        assertTrue(bankFacade.verifyAccount(account2));
        
        System.out.println("Cuenta 1: " + account1 + " - Titular: " + bankFacade.getAccountHolder(account1));
        System.out.println("Cuenta 2: " + account2 + " - Titular: " + bankFacade.getAccountHolder(account2));
        
        // Paso 2: Verificar saldos iniciales
        System.out.println("\n2. Verificando saldos iniciales...");
        double balance1 = bankFacade.getBalance(account1);
        double balance2 = bankFacade.getBalance(account2);
        
        assertEquals(1000.0, balance1, 0.001);
        assertEquals(500.0, balance2, 0.001);
        System.out.println("Saldo inicial Cuenta 1: $" + balance1);
        System.out.println("Saldo inicial Cuenta 2: $" + balance2);
        
        // Paso 3: Realizar depósito en cuenta 1
        System.out.println("\n3. Realizando depósito en Cuenta 1...");
        boolean depositSuccess = bankFacade.deposit(account1, 300.0, "juan@example.com");
        assertTrue(depositSuccess);
        
        double newBalance1 = bankFacade.getBalance(account1);
        assertEquals(1300.0, newBalance1, 0.001);
        System.out.println("Nuevo saldo Cuenta 1 después de depósito: $" + newBalance1);
        
        // Paso 4: Realizar transferencia de cuenta 1 a cuenta 2
        System.out.println("\n4. Realizando transferencia de Cuenta 1 a Cuenta 2...");
        boolean transferSuccess = bankFacade.transfer(account1, account2, 200.0, "juan@example.com", "maria@example.com");
        assertTrue(transferSuccess);
        
        double finalBalance1 = bankFacade.getBalance(account1);
        double finalBalance2 = bankFacade.getBalance(account2);
        
        assertEquals(1100.0, finalBalance1, 0.001); // 1300 - 200
        assertEquals(700.0, finalBalance2, 0.001);   // 500 + 200
        System.out.println("Saldo final Cuenta 1: $" + finalBalance1);
        System.out.println("Saldo final Cuenta 2: $" + finalBalance2);
        
        // Paso 5: Intentar retiro con fondos insuficientes
        System.out.println("\n5. Intentando retiro con fondos insuficientes...");
        boolean withdrawFailure = bankFacade.withdraw(account2, 1000.0, "maria@example.com");
        assertFalse(withdrawFailure);
        
        // El saldo debe permanecer igual
        assertEquals(700.0, bankFacade.getBalance(account2), 0.001);
        System.out.println("Saldo Cuenta 2 después de retiro fallido: $" + bankFacade.getBalance(account2));
        
        // Paso 6: Realizar retiro exitoso
        System.out.println("\n6. Realizando retiro exitoso...");
        boolean withdrawSuccess = bankFacade.withdraw(account2, 150.0, "maria@example.com");
        assertTrue(withdrawSuccess);
        
        double finalBalanceAfterWithdraw = bankFacade.getBalance(account2);
        assertEquals(550.0, finalBalanceAfterWithdraw, 0.001); // 700 - 150
        System.out.println("Saldo final Cuenta 2 después de retiro: $" + finalBalanceAfterWithdraw);
        
        // Paso 7: Consultar historial de transacciones
        System.out.println("\n7. Consultando historial de transacciones...");
        String history1 = bankFacade.getTransactionHistory(account1);
        String history2 = bankFacade.getTransactionHistory(account2);
        
        assertNotNull(history1);
        assertNotNull(history2);
        assertFalse(history1.isEmpty());
        assertFalse(history2.isEmpty());
        
        System.out.println("Historial Cuenta 1:");
        System.out.println(history1);
        System.out.println("\nHistorial Cuenta 2:");
        System.out.println(history2);
        
        // Paso 8: Verificación final de saldos
        System.out.println("\n8. Verificación final de saldos...");
        double finalAccount1Balance = bankFacade.getBalance(account1);
        double finalAccount2Balance = bankFacade.getBalance(account2);
        
        assertEquals(1100.0, finalAccount1Balance, 0.001);
        assertEquals(550.0, finalAccount2Balance, 0.001);
        
        System.out.println("Saldo final verificado - Cuenta 1: $" + finalAccount1Balance);
        System.out.println("Saldo final verificado - Cuenta 2: $" + finalAccount2Balance);
        
        System.out.println("=== FIN ESCENARIO E2E BANCARIO ===");
        
        // Assert final - verificar que todas las operaciones fueron consistentes
        assertTrue(finalAccount1Balance > 0);
        assertTrue(finalAccount2Balance > 0);
        assertTrue(finalAccount1Balance + finalAccount2Balance > 1500.0); // Suma mayor que saldos iniciales combinados
    }

    @Test
    void testEdgeCases_E2E() {
        System.out.println("\n=== PRUEBAS DE CASOS LÍMITE E2E ===");
        
        // Crear cuenta
        String account = bankFacade.createAccount("Test Edge Cases", 100.0, "edge@example.com");
        
        // Test 1: Depósito de monto cero (debería fallar)
        System.out.println("1. Probando depósito de monto cero...");
        boolean zeroDeposit = bankFacade.deposit(account, 0.0, "edge@example.com");
        assertFalse(zeroDeposit);
        
        // Test 2: Depósito de monto negativo (debería fallar)
        System.out.println("2. Probando depósito de monto negativo...");
        boolean negativeDeposit = bankFacade.deposit(account, -50.0, "edge@example.com");
        assertFalse(negativeDeposit);
        
        // Test 3: Transferencia a cuenta inexistente
        System.out.println("3. Probando transferencia a cuenta inexistente...");
        boolean transferToNonExistent = bankFacade.transfer(account, "NON_EXISTENT_ACC", 10.0, "edge@example.com", "none@example.com");
        assertFalse(transferToNonExistent);
        
        // Test 4: Consulta de cuenta inexistente
        System.out.println("4. Probando consulta de cuenta inexistente...");
        assertThrows(IllegalArgumentException.class, () -> {
            bankFacade.getBalance("INVALID_ACCOUNT");
        });
        
        // El saldo de la cuenta original debe permanecer intacto
        assertEquals(100.0, bankFacade.getBalance(account), 0.001);
        System.out.println("Saldo final después de casos límite: $" + bankFacade.getBalance(account));
        
        System.out.println("=== FIN PRUEBAS DE CASOS LÍMITE E2E ===");
    }
}