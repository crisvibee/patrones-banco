package com.example.integration.AbstractFactory;

import com.example.AbstractFactory.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de integración para el patrón Abstract Factory
 * Verifica la interacción entre diferentes componentes del sistema bancario
 */
public class BankIntegrationTest {

    @Test
    public void testRetailBankCustomerIntegration() {
        // Arrange - Crear una fábrica de banca minorista
        RetailBankFactory retailFactory = new RetailBankFactory();
        
        // Act - Crear un conjunto completo de productos para un cliente minorista
        Account checkingAccount = retailFactory.createAccount("ACC001", 1500.0, "CHECKING");
        Card debitCard = retailFactory.createCard("4000123456789010", "Maria Lopez", "12/26", "456");
        CreditLine personalCredit = retailFactory.createCreditLine("PCL001", 3000.0, 0.0);
        
        // Assert - Verificar que todos los productos son del tipo correcto y están integrados
        assertNotNull(checkingAccount, "Cuenta corriente no debería ser nula");
        assertNotNull(debitCard, "Tarjeta de débito no debería ser nula");
        assertNotNull(personalCredit, "Línea de crédito personal no debería ser nula");
        
        // Verificar tipos específicos
        assertTrue(checkingAccount instanceof CheckingAccount, "Debería ser CheckingAccount");
        assertTrue(debitCard instanceof DebitCard, "Debería ser DebitCard");
        assertTrue(personalCredit instanceof PersonalCreditLine, "Debería ser PersonalCreditLine");
        
        // Verificar consistencia en la información del cliente
        assertEquals("Maria Lopez", debitCard.getHolderName(), "Nombre del titular inconsistente en tarjeta");
        
        // Realizar operaciones integradas - Nota: Account no tiene métodos deposit/withdraw
        // Estas operaciones se realizan a través de otras clases como en el patrón Command
        assertEquals(1500.0, checkingAccount.getBalance(), 0.001, "Balance inicial incorrecto");
        
        personalCredit.withdraw(1000.0);
        assertEquals(1000.0, personalCredit.getBalance(), 0.001, "Retiro de línea de crédito no funcionó");
        assertEquals(2000.0, personalCredit.getAvailableCredit(), 0.001, "Crédito disponible incorrecto");
    }

    @Test
    public void testCorporateBankIntegration() {
        // Arrange - Crear una fábrica de banca corporativa
        CorporateBankFactory corporateFactory = new CorporateBankFactory();
        
        // Act - Crear productos para una empresa
        Account savingsAccount = corporateFactory.createAccount("ACC002", 50000.0, "SAVINGS");
        Card creditCard = corporateFactory.createCard("5500123456789010", "TechCorp Inc", "12/27", "789");
        CreditLine businessCredit = corporateFactory.createCreditLine("BCL001", 100000.0, 0.0);
        
        // Assert - Verificar integración de productos corporativos
        assertNotNull(savingsAccount, "Cuenta de ahorros corporativa no debería ser nula");
        assertNotNull(creditCard, "Tarjeta de crédito corporativa no debería ser nula");
        assertNotNull(businessCredit, "Línea de crédito empresarial no debería ser nula");
        
        // Verificar tipos específicos
        assertTrue(savingsAccount instanceof SavingsAccount, "Debería ser SavingsAccount");
        assertTrue(creditCard instanceof CreditCard, "Debería ser CreditCard");
        assertTrue(businessCredit instanceof BusinessCreditLine, "Debería ser BusinessCreditLine");
        
        // Verificar información de la empresa
        assertEquals("TechCorp Inc", creditCard.getHolderName(), "Nombre de empresa inconsistente en tarjeta");
        
        // Probar operaciones empresariales - Nota: Account no tiene métodos withdraw
        // Estas operaciones se realizan a través de otras clases como en el patrón Command
        assertEquals(50000.0, savingsAccount.getBalance(), 0.001, "Balance inicial incorrecto");
        
        businessCredit.withdraw(25000.0);
        assertEquals(25000.0, businessCredit.getBalance(), 0.001, "Uso de línea de crédito empresarial incorrecto");
        assertEquals(75000.0, businessCredit.getAvailableCredit(), 0.001, "Crédito disponible empresarial incorrecto");
    }

    @Test
    public void testFactoryConsistencyAcrossProductFamilies() {
        // Arrange - Crear ambas fábricas
        RetailBankFactory retailFactory = new RetailBankFactory();
        CorporateBankFactory corporateFactory = new CorporateBankFactory();
        
        // Act - Crear productos de cada familia
        Account retailAccount = retailFactory.createAccount("ACC003", 2500.0, "CHECKING");
        Account corporateAccount = corporateFactory.createAccount("ACC004", 100000.0, "SAVINGS");
        
        Card retailCard = retailFactory.createCard("4111123456789010", "Juan Perez", "12/25", "123");
        Card corporateCard = corporateFactory.createCard("5111123456789010", "GlobalCorp Ltd", "12/28", "987");
        
        CreditLine retailCredit = retailFactory.createCreditLine("PCL002", 5000.0, 0.0);
        CreditLine corporateCredit = corporateFactory.createCreditLine("BCL002", 50000.0, 0.0);
        
        // Assert - Verificar que cada fábrica produce su familia específica
        assertTrue(retailAccount instanceof CheckingAccount, "Fábrica minorista debería producir CheckingAccount");
        assertTrue(corporateAccount instanceof SavingsAccount, "Fábrica corporativa debería producir SavingsAccount");
        
        assertTrue(retailCard instanceof DebitCard, "Fábrica minorista debería producir DebitCard");
        assertTrue(corporateCard instanceof CreditCard, "Fábrica corporativa debería producir CreditCard");
        
        assertTrue(retailCredit instanceof PersonalCreditLine, "Fábrica minorista debería producir PersonalCreditLine");
        assertTrue(corporateCredit instanceof BusinessCreditLine, "Fábrica corporativa debería producir BusinessCreditLine");
        
        // Verificar que no hay mezcla entre familias
        assertFalse(retailAccount instanceof SavingsAccount, "Fábrica minorista no debería producir SavingsAccount");
        assertFalse(corporateAccount instanceof CheckingAccount, "Fábrica corporativa no debería producir CheckingAccount");
        
        assertFalse(retailCard instanceof CreditCard, "Fábrica minorista no debería producir CreditCard");
        assertFalse(corporateCard instanceof DebitCard, "Fábrica corporativa no debería producir DebitCard");
        
        assertFalse(retailCredit instanceof BusinessCreditLine, "Fábrica minorista no debería producir BusinessCreditLine");
        assertFalse(corporateCredit instanceof PersonalCreditLine, "Fábrica corporativa no debería producir PersonalCreditLine");
        
        // Verificar consistencia de operaciones - Nota: Account no tiene métodos deposit
        // Estas operaciones se realizan a través de otras clases como en el patrón Command
        assertEquals(2500.0, retailAccount.getBalance(), 0.001, "Balance inicial minorista incorrecto");
        
        corporateCredit.withdraw(10000.0);
        assertEquals(10000.0, corporateCredit.getBalance(), 0.001, "Operación en crédito corporativo inconsistente");
    }
}