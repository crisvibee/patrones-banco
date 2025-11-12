package com.example.Strategy.model;

public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    
    public CreditCardPayment(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    @Override
    public String pay(double amount, String paymentDetails) {
        // Simular procesamiento de pago con tarjeta de crédito
        String maskedCardNumber = "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
        
        return String.format("Pago de $%.2f procesado exitosamente con Tarjeta de Crédito.\n" +
                           "Tarjeta: %s\n" +
                           "Titular: %s\n" +
                           "Detalles adicionales: %s",
                           amount, maskedCardNumber, cardHolderName, paymentDetails);
    }
    
    @Override
    public String getPaymentType() {
        return "Credit Card";
    }
    
    // Getters para información de la tarjeta (opcional)
    public String getCardNumber() {
        return cardNumber;
    }
    
    public String getCardHolderName() {
        return cardHolderName;
    }
    
    public String getExpiryDate() {
        return expiryDate;
    }
    
    public String getCvv() {
        return cvv;
    }
}
