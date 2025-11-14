package com.example.AbstractFactory.model;

public abstract class Card {

    protected String cardNumber;
    protected String holderName;
    protected String expirationDate;
    protected String cvv;

    public Card(String cardNumber, String holderName, String expirationDate, String cvv) {
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public abstract void activate();
    public abstract void deactivate();
    public abstract void getDetails();

}
