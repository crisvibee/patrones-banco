package com.example.e2e.Strategy;

import com.example.Strategy.controller.PaymentContext;
import com.example.Strategy.model.PaymentStrategy;
import com.example.Strategy.model.CreditCardPayment;
import com.example.Strategy.model.DebitCardPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentStrategyE2ETest {
    
    private PaymentContext paymentContext;
    private PaymentStrategy creditCardStrategy;
    private PaymentStrategy debitCardStrategy;
    
    @BeforeEach
    public void setUp() {
        
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
        
      
        paymentContext = new PaymentContext(creditCardStrategy);
    }
    
    @Test
    public void testCompletePaymentWorkflowWithCreditCard() {
        paymentContext.setPaymentStrategy(creditCardStrategy);
        
        double amount = 150.75;
        String paymentDetails = "Compra en tienda online";
        String result = paymentContext.executePayment(amount, paymentDetails);
        
        assertNotNull(result);
        assertTrue(result.contains("Tarjeta de Crédito"));
        assertTrue(result.contains("5678")); 
        assertTrue(result.contains("Juan Pérez"));
        assertTrue(result.contains(String.format("%.2f", amount)));
        assertTrue(result.contains("procesado exitosamente"));
        
        String currentPaymentType = paymentContext.getCurrentPaymentType();
        assertNotNull(currentPaymentType);
        assertTrue(currentPaymentType.contains("Credit Card"));
        
    
        CreditCardPayment creditCard = (CreditCardPayment) creditCardStrategy;
        assertEquals("1234567812345678", creditCard.getCardNumber());
        assertEquals("Juan Pérez", creditCard.getCardHolderName());
        assertEquals("123", creditCard.getCvv());
    }
    
    @Test
    public void testCompletePaymentWorkflowWithDebitCard() {
        paymentContext.setPaymentStrategy(debitCardStrategy);
        
        double amount = 89.50;
        String paymentDetails = "Pago de servicios";
        String result = paymentContext.executePayment(amount, paymentDetails);
        
        assertNotNull(result);
        assertTrue(result.contains("Tarjeta de Débito"));
        assertTrue(result.contains("4321")); 
        assertTrue(result.contains("María García"));
        assertTrue(result.contains(String.format("%.2f", amount)));
        assertTrue(result.contains("procesado exitosamente"));
        
        String currentPaymentType = paymentContext.getCurrentPaymentType();
        assertNotNull(currentPaymentType);
        assertTrue(currentPaymentType.contains("Debit Card"));
        

        DebitCardPayment debitCard = (DebitCardPayment) debitCardStrategy;
        assertEquals("8765432187654321", debitCard.getCardNumber());
        assertEquals("María García", debitCard.getAccountHolderName());
        assertEquals("Bancolombia", debitCard.getBankName());
    }
    
    @Test
    public void testDynamicStrategySwitchingInPaymentWorkflow() {
      
        paymentContext.setPaymentStrategy(creditCardStrategy);
        
        double creditAmount = 200.00;
        String creditDetails = "Compra grande";
        String creditResult = paymentContext.executePayment(creditAmount, creditDetails);
        
        assertNotNull(creditResult);
        assertTrue(creditResult.contains("Tarjeta de Crédito"));
        assertTrue(creditResult.contains("Juan Pérez"));
        
        paymentContext.setPaymentStrategy(debitCardStrategy);
        
        String currentPaymentType = paymentContext.getCurrentPaymentType();
        assertNotNull(currentPaymentType);
        assertTrue(currentPaymentType.contains("Debit Card"));
        
        double debitAmount = 75.25;
        String debitDetails = "Pago pequeño";
        String debitResult = paymentContext.executePayment(debitAmount, debitDetails);
        
        assertNotNull(debitResult);
        assertTrue(debitResult.contains("Tarjeta de Débito"));
        assertTrue(debitResult.contains("María García"));
        assertTrue(debitResult.contains(String.format("%.2f", debitAmount)));
        
        paymentContext.setPaymentStrategy(creditCardStrategy);
        
        double finalAmount = 50.00;
        String finalDetails = "Pago final";
        String finalResult = paymentContext.executePayment(finalAmount, finalDetails);
        
        assertNotNull(finalResult);
        assertTrue(finalResult.contains("Tarjeta de Crédito"));
        assertTrue(finalResult.contains("Juan Pérez"));
        assertTrue(finalResult.contains(String.format("%.2f", finalAmount)));
        
        String finalPaymentType = paymentContext.getCurrentPaymentType();
        assertNotNull(finalPaymentType);
        assertTrue(finalPaymentType.contains("Credit Card"));
    }
    
    @Test
    public void testMultiplePaymentScenariosWithDifferentAmounts() {
        paymentContext.setPaymentStrategy(creditCardStrategy);
        
        double[] amounts = {10.00, 100.00, 500.00, 1000.00, 0.50};
        String[] details = {"Café", "Ropa", "Electrónica", "Viaje", "Propina"};
        
        for (int i = 0; i < amounts.length; i++) {
            double amount = amounts[i];
            String result = paymentContext.executePayment(amount, details[i]);
            
            assertNotNull(result);
            assertTrue(result.contains("Tarjeta de Crédito"));
            assertTrue(result.contains("Juan Pérez"));
            assertTrue(result.contains(String.format("%.2f", amount)));
            assertTrue(result.contains("procesado exitosamente"));
            
            String currentPaymentType = paymentContext.getCurrentPaymentType();
            assertNotNull(currentPaymentType);
            assertTrue(currentPaymentType.contains("Credit Card"));
        }
        
        String finalPaymentType = paymentContext.getCurrentPaymentType();
        assertNotNull(finalPaymentType);
        assertTrue(finalPaymentType.contains("Credit Card"));
    }
    
    @Test
    public void testPaymentWorkflowWithStrategyPersistence() {
   
        paymentContext.setPaymentStrategy(debitCardStrategy);
        
        String persistedPaymentType = paymentContext.getCurrentPaymentType();
        assertNotNull(persistedPaymentType);
        assertTrue(persistedPaymentType.contains("Debit Card"));
        
        double amount1 = 25.00;
        String details1 = "Primer pago";
        String result1 = paymentContext.executePayment(amount1, details1);
        assertNotNull(result1);
        assertTrue(result1.contains("Tarjeta de Débito"));
        
        String strategyAfterPaymentType = paymentContext.getCurrentPaymentType();
        assertNotNull(strategyAfterPaymentType);
        assertTrue(strategyAfterPaymentType.contains("Debit Card"));
        
        double amount2 = 75.00;
        String details2 = "Segundo pago";
        String result2 = paymentContext.executePayment(amount2, details2);
        assertNotNull(result2);
        assertTrue(result2.contains("Tarjeta de Débito"));
        
        String finalPaymentType = paymentContext.getCurrentPaymentType();
        assertNotNull(finalPaymentType);
        assertTrue(finalPaymentType.contains("Debit Card"));
        
    
        DebitCardPayment debitCard = (DebitCardPayment) debitCardStrategy;
        assertEquals("8765432187654321", debitCard.getCardNumber());
        assertEquals("María García", debitCard.getAccountHolderName());
        assertEquals("Bancolombia", debitCard.getBankName());
    }
    
    @Test
    public void testConcurrentPaymentOperationsWithDifferentStrategies() throws InterruptedException {
        final int NUM_THREADS = 3;
        final int OPERATIONS_PER_THREAD = 5;
        
      
        PaymentStrategy[] strategies = {
            new CreditCardPayment("1111222233334444", "Hilo-1", "01/25", "111"),
            new DebitCardPayment("5555666677778888", "Hilo-2", "Banco Nacional"),
            new CreditCardPayment("9999888877776666", "Hilo-3", "03/27", "333")
        };
        
        Thread[] threads = new Thread[NUM_THREADS];
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadIndex = i;
            final PaymentStrategy strategy = strategies[threadIndex];
            
            threads[i] = new Thread(() -> {
                try {
                 
                    PaymentContext threadContext = new PaymentContext(strategy);
                    
                    for (int op = 0; op < OPERATIONS_PER_THREAD; op++) {
                        double amount = (op + 1) * 10.0;
                        String paymentDetails = "Operación " + (op + 1) + " del hilo " + (threadIndex + 1);
                        String result = threadContext.executePayment(amount, paymentDetails);
                        
                        assertNotNull(result);
                        assertTrue(result.contains(String.format("%.2f", amount)));
                        assertTrue(result.contains("procesado exitosamente"));
                        
                        String currentPaymentType = threadContext.getCurrentPaymentType();
                        assertNotNull(currentPaymentType);
                        assertTrue(currentPaymentType.contains(strategy.getPaymentType()));
                        
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        for (Thread thread : threads) {
            thread.start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
      
        for (int i = 0; i < NUM_THREADS; i++) {
            PaymentContext testContext = new PaymentContext(strategies[i]);
            
            String paymentType = testContext.getCurrentPaymentType();
            assertNotNull(paymentType);
            assertTrue(paymentType.contains(strategies[i].getPaymentType()));
            
            String testResult = testContext.executePayment(1.00, "Prueba final");
            assertNotNull(testResult);
            assertTrue(testResult.contains("procesado exitosamente"));
        }
    }

    @Test
    public void testPaymentContextConstructorWithStrategy() {
     
        PaymentContext context = new PaymentContext(creditCardStrategy);
        
        String paymentType = context.getCurrentPaymentType();
        assertNotNull(paymentType);
        assertTrue(paymentType.contains("Credit Card"));
    }

    @Test
    public void testPaymentContextDefaultState() {
       
        PaymentContext context = new PaymentContext(null);
        
        String paymentType = context.getCurrentPaymentType();
        assertNotNull(paymentType);
        assertEquals("No strategy set", paymentType);
    }

    @Test
    public void testExecutePaymentWithoutStrategy() {
    
        PaymentContext context = new PaymentContext(null);
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            context.executePayment(100.00, "Test payment");
        });
        
        assertEquals("No se ha establecido una estrategia de pago", exception.getMessage());
    }

    @Test
    public void testCreditCardPaymentGetters() {
      
        CreditCardPayment creditCard = (CreditCardPayment) creditCardStrategy;
        
        assertEquals("1234567812345678", creditCard.getCardNumber());
        assertEquals("Juan Pérez", creditCard.getCardHolderName());
        assertEquals("12/25", creditCard.getExpiryDate());
        assertEquals("123", creditCard.getCvv());
    }

    @Test
    public void testDebitCardPaymentGetters() {
        
        DebitCardPayment debitCard = (DebitCardPayment) debitCardStrategy;
        
        assertEquals("8765432187654321", debitCard.getCardNumber());
        assertEquals("María García", debitCard.getAccountHolderName());
        assertEquals("Bancolombia", debitCard.getBankName());
    }

    @Test
    public void testPaymentStrategyInterfaceMethods() {
       
        assertEquals("Credit Card", creditCardStrategy.getPaymentType());
        assertEquals("Debit Card", debitCardStrategy.getPaymentType());
        
    
        String creditResult = creditCardStrategy.pay(50.00, "Test");
        assertNotNull(creditResult);
        assertTrue(creditResult.contains("Tarjeta de Crédito"));
        
        String debitResult = debitCardStrategy.pay(50.00, "Test");
        assertNotNull(debitResult);
        assertTrue(debitResult.contains("Tarjeta de Débito"));
    }

    @Test
    public void testEdgeCaseAmounts() {
     
        paymentContext.setPaymentStrategy(creditCardStrategy);
        
    
        String result1 = paymentContext.executePayment(0.00, "Monto cero");
        assertNotNull(result1);
        assertTrue(result1.contains("0.00"));
        
     
        String result2 = paymentContext.executePayment(-10.00, "Monto negativo");
        assertNotNull(result2);
        assertTrue(result2.contains("-10.00"));
        
    
        String result3 = paymentContext.executePayment(999999.99, "Monto grande");
        assertNotNull(result3);
        assertTrue(result3.contains("999999.99"));
    }

    @Test
    public void testEmptyPaymentDetails() {
      
        paymentContext.setPaymentStrategy(creditCardStrategy);
        
        String result = paymentContext.executePayment(100.00, "");
        assertNotNull(result);
        assertTrue(result.contains("procesado exitosamente"));
        
        String result2 = paymentContext.executePayment(100.00, null);
        assertNotNull(result2);
        assertTrue(result2.contains("procesado exitosamente"));
    }
}