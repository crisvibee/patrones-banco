package com.example.e2e.AbstractFactory;

import com.example.AbstractFactory.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba end-to-end (E2E) para el patrón Abstract Factory
 * Simula escenarios completos de uso del sistema bancario
 */
public class BankingScenarioE2ETest {

    @Test
    public void testCompleteRetailBankingScenario() {
        // Arrange - Configuración inicial del escenario
        RetailBankFactory retailFactory = new RetailBankFactory();
        
        // Act - Flujo completo de banca minorista
        // 1. Apertura de cuenta corriente
        Account checkingAccount = retailFactory.createAccount("RET001", 2000.0, "CHECKING");
        
        // 2. Emisión de tarjeta de débito
        Card debitCard = retailFactory.createCard("4111222233334444", "Carlos Mendoza", "12/25", "123");
        
        // 3. Aprobación de línea de crédito personal
        CreditLine personalCredit = retailFactory.createCreditLine("PCL002", 5000.0, 0.0);
        
        // 4. Operaciones del cliente - Nota: Account no tiene métodos deposit/withdraw
        // Estas operaciones se realizan a través de otras clases como en el patrón Command
        
        personalCredit.withdraw(2000.0);  // Uso de línea de crédito
        personalCredit.deposit(500.0);    // Pago parcial
        
        // Assert - Verificación del estado final del sistema
        assertEquals(2000.0, checkingAccount.getBalance(), 0.001, 
            "Balance inicial de cuenta corriente incorrecto");
        
        assertEquals(1500.0, personalCredit.getBalance(), 0.001, 
            "Balance final de línea de crédito incorrecto");
        
        assertEquals(3500.0, personalCredit.getAvailableCredit(), 0.001, 
            "Crédito disponible final incorrecto");
        
        // Verificar consistencia de información del cliente
        assertEquals("Carlos Mendoza", debitCard.getHolderName(), 
            "Inconsistencia en nombre del titular de tarjeta");
        
        // Verificar tipos de productos
        assertTrue(checkingAccount instanceof CheckingAccount, 
            "La cuenta debería ser del tipo CheckingAccount");
        assertTrue(debitCard instanceof DebitCard, 
            "La tarjeta debería ser del tipo DebitCard");
        assertTrue(personalCredit instanceof PersonalCreditLine, 
            "La línea de crédito debería ser PersonalCreditLine");
        
        // Verificar que los productos son funcionales
        assertTrue(checkingAccount.getBalance() >= 0, 
            "La cuenta no debería tener balance negativo");
        assertTrue(personalCredit.getBalance() <= personalCredit.getCreditLimit(), 
            "El uso de crédito no debería exceder el límite");
    }

    @Test
    public void testCompleteCorporateBankingScenario() {
        // Arrange - Configuración corporativa
        CorporateBankFactory corporateFactory = new CorporateBankFactory();
        
        // Act - Flujo completo de banca corporativa
        // 1. Apertura de cuenta de ahorros empresarial
        Account savingsAccount = corporateFactory.createAccount("CORP001", 50000.0, "SAVINGS");
        
        // 2. Emisión de tarjeta de crédito corporativa
        Card creditCard = corporateFactory.createCard("5555666677778888", "GlobalTech Ltd", "12/27", "789");
        
        // 3. Aprobación de línea de crédito empresarial
        CreditLine businessCredit = corporateFactory.createCreditLine("BCL002", 200000.0, 0.0);
        
        // 4. Operaciones empresariales - Nota: Account no tiene métodos deposit/withdraw
        // Estas operaciones se realizan a través de otras clases como en el patrón Command
        
        businessCredit.withdraw(75000.0); // Inversión en equipos
        businessCredit.deposit(20000.0);  // Reembolso parcial
        
        // Assert - Verificación del estado corporativo
        assertEquals(50000.0, savingsAccount.getBalance(), 0.001, 
            "Balance inicial de cuenta de ahorros incorrecto");
        
        assertEquals(55000.0, businessCredit.getBalance(), 0.001, 
            "Balance final de línea de crédito empresarial incorrecto");
        
        assertEquals(145000.0, businessCredit.getAvailableCredit(), 0.001, 
            "Crédito disponible empresarial incorrecto");
        
        // Verificar escala corporativa
        assertTrue(savingsAccount.getBalance() > 10000.0, 
            "Las operaciones corporativas deberían involucrar montos significativos");
        assertTrue(businessCredit.getCreditLimit() > 100000.0, 
            "Los límites corporativos deberían ser altos");
        
        // Verificar tipos de productos corporativos
        assertTrue(savingsAccount instanceof SavingsAccount, 
            "La cuenta debería ser del tipo SavingsAccount");
        assertTrue(creditCard instanceof CreditCard, 
            "La tarjeta debería ser del tipo CreditCard");
        assertTrue(businessCredit instanceof BusinessCreditLine, 
            "La línea de crédito debería ser BusinessCreditLine");
    }

    @Test
    public void testMixedBankingEnvironmentScenario() {
        // Arrange - Ambientes de banca coexistiendo
        RetailBankFactory retailFactory = new RetailBankFactory();
        CorporateBankFactory corporateFactory = new CorporateBankFactory();
        
        // Act - Escenario donde ambos tipos de bancos operan simultáneamente
        
        // Cliente minorista
        Account retailAccount = retailFactory.createAccount("RET002", 3000.0, "CHECKING");
        Card retailCard = retailFactory.createCard("4222333344445555", "Ana Silva", "12/26", "456");
        CreditLine retailCredit = retailFactory.createCreditLine("PCL003", 7000.0, 0.0);
        
        // Cliente corporativo
        Account corporateAccount = corporateFactory.createAccount("CORP002", 80000.0, "SAVINGS");
        Card corporateCard = corporateFactory.createCard("5666777788889999", "InnovateCorp", "12/28", "890");
        CreditLine corporateCredit = corporateFactory.createCreditLine("BCL003", 300000.0, 0.0);
        
        // Operaciones concurrentes - Nota: Account no tiene métodos deposit/withdraw
        // Estas operaciones se realizan a través de otras clases como en el patrón Command
        retailCredit.withdraw(3000.0);
        
        corporateCredit.withdraw(100000.0);
        corporateCredit.deposit(30000.0);
        
        // Assert - Verificar que ambos ambientes funcionan independientemente
        assertEquals(3000.0, retailAccount.getBalance(), 0.001, 
            "Balance inicial minorista incorrecto");
        assertEquals(3000.0, retailCredit.getBalance(), 0.001, 
            "Balance de crédito minorista incorrecto");
        assertEquals(4000.0, retailCredit.getAvailableCredit(), 0.001, 
            "Crédito disponible minorista incorrecto");
        
        assertEquals(80000.0, corporateAccount.getBalance(), 0.001, 
            "Balance inicial corporativo incorrecto");
        assertEquals(70000.0, corporateCredit.getBalance(), 0.001, 
            "Balance de crédito corporativo incorrecto");
        assertEquals(230000.0, corporateCredit.getAvailableCredit(), 0.001, 
            "Crédito disponible corporativo incorrecto");
        
        // Verificar aislamiento entre familias de productos
        assertNotEquals(retailAccount.getClass(), corporateAccount.getClass(), 
            "Las cuentas de diferentes familias no deberían ser del mismo tipo");
        assertNotEquals(retailCard.getClass(), corporateCard.getClass(), 
            "Las tarjetas de diferentes familias no deberían ser del mismo tipo");
        assertNotEquals(retailCredit.getClass(), corporateCredit.getClass(), 
            "Las líneas de crédito de diferentes familias no deberían ser del mismo tipo");
        
        // Verificar que no hay interferencia entre clientes
        assertNotEquals(retailCard.getHolderName(), corporateCard.getHolderName(), 
            "Los clientes deberían ser diferentes");
        assertNotEquals(retailAccount.getAccountNumber(), corporateAccount.getAccountNumber(), 
            "Los números de cuenta deberían ser únicos");
    }
}