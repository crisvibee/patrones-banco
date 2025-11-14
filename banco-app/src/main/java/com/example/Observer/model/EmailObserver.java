package com.example.Observer.model;

public class EmailObserver implements Observer {
    private String emailAddress;
    private String customerName;

    public EmailObserver(String emailAddress, String customerName) {
        this.emailAddress = emailAddress;
        this.customerName = customerName;
    }

    @Override
    public void update(String message) {
        System.out.println("[EMAIL NOTIFICATION] Enviando a: " + emailAddress);
        System.out.println("   Estimado/a " + customerName + ",");
        System.out.println("   " + message);
        System.out.println("   Atentamente, Bancolombia");
        System.out.println("   ---");
    }

    @Override
    public String getObserverType() {
        return "EmailObserver (" + emailAddress + ")";
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
