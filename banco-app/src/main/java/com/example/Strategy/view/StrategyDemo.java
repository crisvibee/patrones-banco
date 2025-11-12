package com.example.Strategy.view;

import com.example.Strategy.controller.PaymentContext;
import com.example.Strategy.model.CreditCardPayment;
import com.example.Strategy.model.DebitCardPayment;
import com.example.Strategy.model.PaymentStrategy;

public class StrategyDemo {
    
    public static void main(String[] args) {
        System.out.println("SISTEMA DE PAGOS STRATEGY\n");
        
        // Crear estrategias de pago
        PaymentStrategy creditCard = new CreditCardPayment(
            "1234567812345678", 
            "Juan Pérez", 
            "12/25", 
            "123"
        );
        
        PaymentStrategy debitCard = new DebitCardPayment(
            "8765432187654321", 
            "María García", 
            "Bancolombia"
        );
        
        // Crear contexto de pago
        PaymentContext paymentContext = new PaymentContext(creditCard);
        
        System.out.println("Método de pago: " + paymentContext.getCurrentPaymentType());
        System.out.println();
        
        // Procesar pagos con tarjeta de crédito
        System.out.println("1. PAGO CON TARJETA DE CRÉDITO:");
        String result1 = paymentContext.executePayment(150.75, "Compra en McDonalds");
        System.out.println(result1);
        
        // Cambiar a tarjeta de débito
        System.out.println("2. CAMBIANDO A TARJETA DE DÉBITO:");
        paymentContext.setPaymentStrategy(debitCard);
        System.out.println("Nuevo método de pago: " + paymentContext.getCurrentPaymentType());
        System.out.println();
        
        // Procesar pago con tarjeta de débito
        System.out.println("3. PAGO CON TARJETA DE DÉBITO:");
        String result2 = paymentContext.executePayment(89.99, "Suscripción Premium");
        System.out.println(result2);
        
        // Volver a tarjeta de crédito para otro pago
        System.out.println("4. VOLVIENDO A TARJETA DE CRÉDITO:");
        paymentContext.setPaymentStrategy(creditCard);
        System.out.println("Método de pago actual: " + paymentContext.getCurrentPaymentType());
        System.out.println();
        
        // Procesar otro pago con tarjeta de crédito
        System.out.println("5. PAGO FINAL CON TARJETA DE CRÉDITO:");
        String result3 = paymentContext.executePayment(299.50, "Compra de Electrodomésticos");
        System.out.println(result3);
        
    }
}