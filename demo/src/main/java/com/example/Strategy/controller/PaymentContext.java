package com.example.Strategy.controller;

import com.example.Strategy.model.PaymentStrategy;

public class PaymentContext {
    private PaymentStrategy paymentStrategy;
    
    /**
     * Constructor que recibe una estrategia de pago
     * @param paymentStrategy Estrategia de pago a utilizar
     */
    public PaymentContext(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    /**
     * Establece una nueva estrategia de pago
     * @param paymentStrategy Nueva estrategia de pago
     */
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    /**
     * Ejecuta el pago utilizando la estrategia actual
     * @param amount Monto del pago
     * @param paymentDetails Detalles adicionales del pago
     * @return Resultado del procesamiento del pago
     */
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
    
    /**
     * Obtiene el tipo de método de pago actual
     * @return Tipo de método de pago
     */
    public String getCurrentPaymentType() {
        return paymentStrategy != null ? paymentStrategy.getPaymentType() : "No strategy set";
    }
}
