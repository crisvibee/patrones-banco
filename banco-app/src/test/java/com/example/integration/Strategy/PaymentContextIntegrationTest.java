package com.example.integration.Strategy;

import com.example.Strategy.controller.PaymentContext;
import com.example.Strategy.model.CreditCardPayment;
import com.example.Strategy.model.DebitCardPayment;
import com.example.Strategy.model.PaymentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para el patrón Strategy
 * Prueba la interacción entre PaymentContext y las diferentes estrategias de pago
 */
public class PaymentContextIntegrationTest {
    
    private PaymentContext paymentContext;
    private PaymentStrategy creditCardStrategy;
    private PaymentStrategy debitCardStrategy;
    
    @BeforeEach
    public void setUp() {
        // Crear estrategias de pago
        creditCardStrategy = new CreditCardPayment(
            "1234567812345678", 
            "Juan Pérez", 
            "12/25", 
            "123"
        );
        
        debitCardStrategy = new DebitCardPayment(
            "8765432187654321", 
            "María García", 
            "Bancolombia"
        );
        
        // Crear contexto de pago con estrategia inicial (tarjeta de crédito)
        paymentContext = new PaymentContext(creditCardStrategy);
    }
    
    @Test
    public void testPaymentContextInitializationWithCreditCard() {
        // Assert
        assertNotNull(paymentContext, "El contexto de pago no debería ser nulo");
        assertEquals("Credit Card", paymentContext.getCurrentPaymentType(),
            "El tipo de pago inicial debería ser tarjeta de crédito");
    }
    
    @Test
    public void testPaymentExecutionWithCreditCardStrategy() {
        // Arrange
        double amount = 150.75;
        String paymentDetails = "Compra en McDonalds";
        
        // Act
        String result = paymentContext.executePayment(amount, paymentDetails);
        
        // Assert
        assertNotNull(result, "El resultado del pago no debería ser nulo");
        assertTrue(result.contains("Pago de $150.75 procesado exitosamente"),
            "El resultado debería incluir el monto del pago");
        assertTrue(result.contains("Tarjeta de Crédito"),
            "El resultado debería indicar el tipo de tarjeta");
        assertTrue(result.contains("Juan Pérez"),
            "El resultado debería incluir el nombre del titular");
    }
    
    @Test
    public void testPaymentExecutionWithDebitCardStrategy() {
        // Arrange
        double amount = 89.99;
        String paymentDetails = "Suscripción Premium";
        
        // Cambiar a estrategia de tarjeta de débito
        paymentContext.setPaymentStrategy(debitCardStrategy);
        
        // Act
        String result = paymentContext.executePayment(amount, paymentDetails);
        
        // Assert
        assertNotNull(result, "El resultado del pago no debería ser nulo");
        assertTrue(result.contains("Pago de $89.99 procesado exitosamente"),
            "El resultado debería incluir el monto del pago");
        assertTrue(result.contains("Tarjeta de Débito"),
            "El resultado debería indicar el tipo de tarjeta");
        assertTrue(result.contains("María García"),
            "El resultado debería incluir el nombre del titular");
        assertTrue(result.contains("Bancolombia"),
            "El resultado debería incluir el nombre del banco");
    }
    
    @Test
    public void testPaymentStrategySwitchingAtRuntime() {
        // Arrange
        double amount1 = 150.75;
        double amount2 = 89.99;
        String paymentDetails = "Compra diversa";
        
        // Act - Ejecutar pago con tarjeta de crédito
        String result1 = paymentContext.executePayment(amount1, paymentDetails);
        
        // Cambiar a tarjeta de débito
        paymentContext.setPaymentStrategy(debitCardStrategy);
        
        // Verificar que el tipo de pago cambió
        assertEquals("Debit Card", paymentContext.getCurrentPaymentType(),
            "El tipo de pago debería cambiar a tarjeta de débito");
        
        // Ejecutar pago con tarjeta de débito
        String result2 = paymentContext.executePayment(amount2, paymentDetails);
        
        // Assert
        assertTrue(result1.contains("Tarjeta de Crédito"),
            "El primer pago debería ser con tarjeta de crédito");
        assertTrue(result2.contains("Tarjeta de Débito"),
            "El segundo pago debería ser con tarjeta de débito");
    }
    
    @Test
    public void testMultipleStrategySwitches() {
        // Arrange
        double amount = 50.0;
        String paymentDetails = "Pago múltiple";
        
        // Act - Cambiar entre estrategias múltiples veces y ejecutar pagos
        paymentContext.setPaymentStrategy(creditCardStrategy);
        String result1 = paymentContext.executePayment(amount, paymentDetails);
        
        paymentContext.setPaymentStrategy(debitCardStrategy);
        String result2 = paymentContext.executePayment(amount, paymentDetails);
        
        paymentContext.setPaymentStrategy(creditCardStrategy);
        String result3 = paymentContext.executePayment(amount, paymentDetails);
        
        // Assert
        assertTrue(result1.contains("Tarjeta de Crédito"), "Primer pago debería ser crédito");
        assertTrue(result2.contains("Tarjeta de Débito"), "Segundo pago debería ser débito");
        assertTrue(result3.contains("Tarjeta de Crédito"), "Tercer pago debería ser crédito");
        
        // Verificar que el tipo actual es el último establecido
        assertEquals("Credit Card", paymentContext.getCurrentPaymentType(),
            "El tipo de pago actual debería ser tarjeta de crédito");
    }
    
    @Test
    public void testPaymentContextWithNullStrategy() {
        // Arrange
        paymentContext.setPaymentStrategy(null);
        double amount = 100.0;
        String paymentDetails = "Pago con estrategia nula";
        
        // Act & Assert - Debería lanzar excepción
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            paymentContext.executePayment(amount, paymentDetails);
        }, "Debería lanzar IllegalStateException cuando no hay estrategia establecida");
        
        assertTrue(exception.getMessage().contains("No se ha establecido una estrategia de pago"),
            "El mensaje de excepción debería indicar el problema");
    }
    
    @Test
    public void testPaymentContextCurrentPaymentTypeWithNullStrategy() {
        // Arrange
        paymentContext.setPaymentStrategy(null);
        
        // Act
        String currentType = paymentContext.getCurrentPaymentType();
        
        // Assert
        assertEquals("No strategy set", currentType,
            "Debería retornar 'No strategy set' cuando no hay estrategia establecida");
    }
    
    @Test
    public void testPaymentContextStrategyReplacement() {
        // Arrange
        double amount = 75.50;
        String paymentDetails = "Reemplazo de estrategia";
        
        // Ejecutar pago con estrategia inicial (crédito)
        String initialResult = paymentContext.executePayment(amount, paymentDetails);
        
        // Reemplazar con nueva estrategia de crédito (diferentes datos)
        PaymentStrategy newCreditCard = new CreditCardPayment(
            "9999888877776666", 
            "Carlos Rodríguez", 
            "06/26", 
            "456"
        );
        
        paymentContext.setPaymentStrategy(newCreditCard);
        
        // Ejecutar pago con nueva estrategia
        String newResult = paymentContext.executePayment(amount, paymentDetails);
        
        // Assert
        assertTrue(initialResult.contains("Juan Pérez"), "Pago inicial con titular original");
        assertTrue(newResult.contains("Carlos Rodríguez"), "Pago posterior con nuevo titular");
        assertTrue(newResult.contains("9999-8888-7777-6666"), "Pago posterior con nueva tarjeta");
    }
    
    @Test
    public void testPaymentContextWithDifferentAmounts() {
        // Arrange
        double[] amounts = {1.0, 10.0, 100.0, 1000.0, 10000.0};
        String paymentDetails = "Pago con diferentes montos";
        
        // Act & Assert - Probar con diferentes montos
        for (double amount : amounts) {
            String result = paymentContext.executePayment(amount, paymentDetails);
            
            assertNotNull(result, "El resultado no debería ser nulo para monto: " + amount);
            assertTrue(result.contains(String.format("$%.2f", amount)),
                "El resultado debería incluir el monto formateado: " + amount);
            assertTrue(result.contains("procesado exitosamente"),
                "El resultado debería indicar éxito para monto: " + amount);
        }
    }
    
    @Test
    public void testPaymentContextStrategyPersistence() {
        // Arrange
        paymentContext.setPaymentStrategy(debitCardStrategy);
        
        // Act - Ejecutar múltiples pagos sin cambiar estrategia
        String result1 = paymentContext.executePayment(50.0, "Pago 1");
        String result2 = paymentContext.executePayment(75.0, "Pago 2");
        String result3 = paymentContext.executePayment(100.0, "Pago 3");
        
        // Assert - Todos deberían usar la misma estrategia (débito)
        assertTrue(result1.contains("Tarjeta de Débito"), "Primer pago debería ser débito");
        assertTrue(result2.contains("Tarjeta de Débito"), "Segundo pago debería ser débito");
        assertTrue(result3.contains("Tarjeta de Débito"), "Tercer pago debería ser débito");
        
        // El tipo actual debería mantenerse como débito
        assertEquals("Debit Card", paymentContext.getCurrentPaymentType(),
            "El tipo de pago debería mantenerse como tarjeta de débito");
    }
}