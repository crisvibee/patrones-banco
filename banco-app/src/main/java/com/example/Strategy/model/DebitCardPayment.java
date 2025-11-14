package com.example.Strategy.model;

public class DebitCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String accountHolderName;
    private String bankName;
    
    public DebitCardPayment(String cardNumber, String accountHolderName, String bankName) {
        this.cardNumber = cardNumber;
        this.accountHolderName = accountHolderName;
        this.bankName = bankName;
    }
    
    @Override
    public String pay(double amount, String paymentDetails) {
     
        String maskedCardNumber = "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
        
        return String.format("Pago de $%.2f procesado exitosamente con Tarjeta de Débito.\n" +
                           "Tarjeta: %s\n" +
                           "Titular: %s\n" +
                           "Banco: %s\n" +
                           "Detalles adicionales: %s",
                           amount, maskedCardNumber, accountHolderName, bankName, paymentDetails);
    }
    
    @Override
    public String getPaymentType() {
        return "Debit Card";
    }
    
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public String getAccountHolderName() {
        return accountHolderName;
    }
    
    public String getBankName() {
        return bankName;
    }
}
