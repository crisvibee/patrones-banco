package com.example.Strategy.controller;

import com.example.Strategy.model.PaymentStrategy;

public class PaymentContext {
    private PaymentStrategy paymentStrategy;
    
    public PaymentContext(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    public String executePayment(double amount, String paymentDetails) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("No se ha establecido una estrategia de pago");
        }
        
        System.out.println("=== PROCESANDO PAGO ===");
        System.out.println("Método: " + paymentStrategy.getPaymentType());
        System.out.println("Monto: $" + amount);
        
        String result = paymentStrategy.pay(amount, paymentDetails);
        
        System.out.println("=== PAGO COMPLETADO ===\n");
        return result;
    }
    
    public String getCurrentPaymentType() {
        return paymentStrategy != null ? paymentStrategy.getPaymentType() : "No strategy set";
    }
}
