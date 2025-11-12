package com.example.unit.Strategy;

import com.example.Strategy.model.CreditCardPayment;
import com.example.Strategy.model.DebitCardPayment;
import com.example.Strategy.model.PaymentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para las implementaciones del patrón Strategy
 * Prueba las estrategias de pago (CreditCardPayment y DebitCardPayment) de forma aislada
 */
public class PaymentStrategyUnitTest {
    
    private PaymentStrategy creditCardPayment;
    private PaymentStrategy debitCardPayment;
    
    @BeforeEach
    public void setUp() {
        // Crear instancias de las estrategias de pago para pruebas
        creditCardPayment = new CreditCardPayment(
            "1234567812345678", 
            "Juan Pérez", 
            "12/25", 
            "123"
        );
        
        debitCardPayment = new DebitCardPayment(
            "8765432187654321", 
            "María García", 
            "Bancolombia"
        );
    }
    
    @Test
    public void testCreditCardPaymentCreation() {
        // Assert
        assertNotNull(creditCardPayment, "La estrategia de pago con tarjeta de crédito no debería ser nula");
        assertEquals("Credit Card", creditCardPayment.getPaymentType(), "Tipo de pago incorrecto");
        
        // Verificar que es instancia de CreditCardPayment
        assertTrue(creditCardPayment instanceof CreditCardPayment, "Debería ser instancia de CreditCardPayment");
    }
    
    @Test
    public void testDebitCardPaymentCreation() {
        // Assert
        assertNotNull(debitCardPayment, "La estrategia de pago con tarjeta de débito no debería ser nula");
        assertEquals("Debit Card", debitCardPayment.getPaymentType(), "Tipo de pago incorrecto");
        
        // Verificar que es instancia de DebitCardPayment
        assertTrue(debitCardPayment instanceof DebitCardPayment, "Debería ser instancia de DebitCardPayment");
    }
    
    @Test
    public void testCreditCardPaymentExecution() {
        // Arrange
        double amount = 150.75;
        String paymentDetails = "Compra en McDonalds";
        
        // Act
        String result = creditCardPayment.pay(amount, paymentDetails);
        
        // Assert
        assertNotNull(result, "El resultado del pago no debería ser nulo");
        assertTrue(result.contains("Pago de $150.75 procesado exitosamente"), 
            "El resultado debería incluir el monto del pago");
        assertTrue(result.contains("Tarjeta de Crédito"), 
            "El resultado debería indicar el tipo de tarjeta");
        assertTrue(result.contains("****-****-****-5678"), 
            "El resultado debería mostrar el número de tarjeta enmascarado");
        assertTrue(result.contains("Juan Pérez"), 
            "El resultado debería incluir el nombre del titular");
        assertTrue(result.contains("Compra en McDonalds"), 
            "El resultado debería incluir los detalles del pago");
    }
    
    @Test
    public void testDebitCardPaymentExecution() {
        // Arrange
        double amount = 89.99;
        String paymentDetails = "Suscripción Premium";
        
        // Act
        String result = debitCardPayment.pay(amount, paymentDetails);
        
        // Assert
        assertNotNull(result, "El resultado del pago no debería ser nulo");
        assertTrue(result.contains("Pago de $89.99 procesado exitosamente"), 
            "El resultado debería incluir el monto del pago");
        assertTrue(result.contains("Tarjeta de Débito"), 
            "El resultado debería indicar el tipo de tarjeta");
        assertTrue(result.contains("****-****-****-4321"), 
            "El resultado debería mostrar el número de tarjeta enmascarado");
        assertTrue(result.contains("María García"), 
            "El resultado debería incluir el nombre del titular");
        assertTrue(result.contains("Bancolombia"), 
            "El resultado debería incluir el nombre del banco");
        assertTrue(result.contains("Suscripción Premium"), 
            "El resultado debería incluir los detalles del pago");
    }
    
    @Test
    public void testCreditCardPaymentWithZeroAmount() {
        // Arrange
        double amount = 0.0;
        String paymentDetails = "Pago simbólico";
        
        // Act
        String result = creditCardPayment.pay(amount, paymentDetails);
        
        // Assert
        assertNotNull(result, "El resultado del pago no debería ser nulo");
        assertTrue(result.contains("Pago de $0.00 procesado exitosamente"), 
            "El resultado debería procesar montos de cero correctamente");
    }
    
    @Test
    public void testDebitCardPaymentWithLargeAmount() {
        // Arrange
        double amount = 9999.99;
        String paymentDetails = "Compra grande";
        
        // Act
        String result = debitCardPayment.pay(amount, paymentDetails);
        
        // Assert
        assertNotNull(result, "El resultado del pago no debería ser nulo");
        assertTrue(result.contains("Pago de $9999.99 procesado exitosamente"), 
            "El resultado debería procesar montos grandes correctamente");
    }
    
    @Test
    public void testCreditCardPaymentCardInformation() {
        // Verificar que los getters funcionan correctamente
        CreditCardPayment creditCard = (CreditCardPayment) creditCardPayment;
        
        assertEquals("1234567812345678", creditCard.getCardNumber(), "Número de tarjeta incorrecto");
        assertEquals("Juan Pérez", creditCard.getCardHolderName(), "Nombre del titular incorrecto");
        assertEquals("12/25", creditCard.getExpiryDate(), "Fecha de expiración incorrecta");
        assertEquals("123", creditCard.getCvv(), "CVV incorrecto");
    }
    
    @Test
    public void testDebitCardPaymentCardInformation() {
        // Verificar que los getters funcionan correctamente
        DebitCardPayment debitCard = (DebitCardPayment) debitCardPayment;
        
        assertEquals("8765432187654321", debitCard.getCardNumber(), "Número de tarjeta incorrecto");
        assertEquals("María García", debitCard.getAccountHolderName(), "Nombre del titular incorrecto");
        assertEquals("Bancolombia", debitCard.getBankName(), "Nombre del banco incorrecto");
    }
    
    @Test
    public void testPaymentStrategyInterfaceMethods() {
        // Verificar que ambas implementaciones cumplen con la interfaz
        assertNotNull(creditCardPayment.getPaymentType(), "Método getPaymentType() debería retornar valor");
        assertNotNull(debitCardPayment.getPaymentType(), "Método getPaymentType() debería retornar valor");
        
        // Verificar que pay() retorna String para ambas implementaciones
        String creditResult = creditCardPayment.pay(100.0, "Test");
        String debitResult = debitCardPayment.pay(100.0, "Test");
        
        assertTrue(creditResult instanceof String, "pay() debería retornar String");
        assertTrue(debitResult instanceof String, "pay() debería retornar String");
    }
}