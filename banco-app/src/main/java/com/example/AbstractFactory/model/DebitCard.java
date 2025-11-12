package com.example.AbstractFactory.model;

public class DebitCard extends Card {

    public DebitCard(String cardNumber, String holderName, String expirationDate, String cvv) {
        super(cardNumber, holderName, expirationDate, cvv);
    }

    @Override
    public void activate() {
        System.out.println("Debit card activated");
    }

    @Override
    public void deactivate() {
        System.out.println("Debit card deactivated");
    }

    @Override
    public void getDetails() {
        System.out.println("Debit card details: " + cardNumber + ", " + holderName + ", " + expirationDate + ", " + cvv);
    }
}
