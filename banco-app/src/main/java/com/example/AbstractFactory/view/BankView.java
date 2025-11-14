package com.example.AbstractFactory.view;

import java.util.Scanner;

import com.example.AbstractFactory.model.Account;
import com.example.AbstractFactory.model.Card;
import com.example.AbstractFactory.model.CorporateBankFactory;
import com.example.AbstractFactory.model.CreditLine;
import com.example.AbstractFactory.model.FinancialProductFactory;
import com.example.AbstractFactory.model.RetailBankFactory;

public class BankView {

    public static int nAccounts = 1;
    public static int nCards = 1;
    public static int nCreditLines = 1;

    public static void main(String[] args) {
        
        Scanner reader = new Scanner(System.in);
        
        FinancialProductFactory factory;
        
        Account[] accounts = new Account[nAccounts];
        Card[] cards = new Card[nCards];
        CreditLine[] creditLines = new CreditLine[nCreditLines];

        System.out.println("¿Desea abrir Savings Account (1) o Checking Account (2)?");
        String eleccion = reader.next();

        if (eleccion.equals("1")) {
            factory = new RetailBankFactory();
        } else if (eleccion.equals("2")) {
            factory = new CorporateBankFactory();
        } else {
            System.out.println("Elección inválida");
            return;
        }

        for (int i = 0; i < nAccounts; i++) {
            accounts[i] = factory.createAccount("1234567890", 1000.0, "Savings");
        }

        for (int i = 0; i < nCards; i++) {
            cards[i] = factory.createCard("1234567890", "Christopher Taipe", "12/24", "123");
        }

        for (int i = 0; i < nCreditLines; i++) {
            creditLines[i] = factory.createCreditLine("1234567890", 10000.0, 0.0);
        }

        for (int i = 0; i < nAccounts; i++) {
            accounts[i].open();
        }

        for (int i = 0; i < nCards; i++) {
            cards[i].activate();
        }

        for (int i = 0; i < nCreditLines; i++) {
            creditLines[i].approve();
        }
        

    }

}