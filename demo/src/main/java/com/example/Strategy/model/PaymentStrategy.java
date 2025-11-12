package com.example.Strategy.model;

public interface PaymentStrategy {
    /**
     * Método para procesar un pago
     * @param amount Monto del pago
     * @param paymentDetails Detalles específicos del pago
     * @return Mensaje de confirmación del pago
     */
    String pay(double amount, String paymentDetails);
    
    /**
     * Obtiene el tipo de método de pago
     * @return Tipo de pago (ej: "Credit Card", "Debit Card")
     */
    String getPaymentType();
}
