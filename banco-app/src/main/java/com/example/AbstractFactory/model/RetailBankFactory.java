package com.example.AbstractFactory.model;

public class RetailBankFactory implements FinancialProductFactory {

    @Override
    public Account createAccount(String accountNumber, double balance, String type) {
        if ("CHECKING".equalsIgnoreCase(type)) {
            return new CheckingAccount(accountNumber, balance, type);
        } else if ("SAVINGS".equalsIgnoreCase(type)) {
            return new SavingsAccount(accountNumber, balance, type);
        } else {
            throw new IllegalArgumentException("Tipo de cuenta no válido: " + type);
        }
    }

    @Override
    public Card createCard(String cardNumber, String holderName, String expirationDate, String cvv) {
        return new DebitCard(cardNumber, holderName, expirationDate, cvv);
    }

    @Override
    public CreditLine createCreditLine(String creditLineNumber, double creditLimit, double balance) {
        return new PersonalCreditLine(creditLineNumber, creditLimit, balance);
    }


}
