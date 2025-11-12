package com.example.AbstractFactory.model;

public class CreditCard extends Card {

    public CreditCard(String cardNumber, String holderName, String expirationDate, String cvv) {
        super(cardNumber, holderName, expirationDate, cvv);
    }

    @Override
    public void activate() {
        System.out.println("Credit card activated");
    }

    @Override
    public void deactivate() {
        System.out.println("Credit card deactivated");
    }

    @Override
    public void getDetails() {
        System.out.println("Credit card details: " + cardNumber + ", " + holderName + ", " + expirationDate + ", " + cvv);
    }

}
