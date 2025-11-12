package com.example.AbstractFactory.model;

public interface FinancialProductFactory {

    Account createAccount(String accountNumber, double balance, String type);

    Card createCard(String cardNumber, String holderName, String expirationDate, String cvv);

    CreditLine createCreditLine(String creditLineNumber, double creditLimit, double balance);

}
