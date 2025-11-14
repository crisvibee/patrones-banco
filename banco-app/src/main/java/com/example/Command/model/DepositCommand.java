package com.example.Command.model;

import com.example.Observer.model.ConcreteSubject;

public class DepositCommand implements Command {
    private ConcreteSubject account;
    private double amount;
    private double previousBalance;
    private boolean executed;
    
    
    public DepositCommand(ConcreteSubject account, double amount) {
        this.account = account;
        this.amount = amount;
        this.previousBalance = account.getBalance();
        this.executed = false;
    }
    
    @Override
    public void execute() {
        if (!executed) {
            System.out.println("Ejecutando comando: " + getCommandName());
            account.deposit(amount);
            executed = true;
            System.out.println("Comando ejecutado exitosamente");
        } else {
            System.out.println("Comando ya fue ejecutado anteriormente");
        }
    }
    
    @Override
    public void undo() {
        if (executed) {
            System.out.println("Deshaciendo comando: " + getCommandName());
            
            account.withdraw(amount);
            System.out.println("Depósito de $" + amount + " deshecho. Balance restaurado a $" + account.getBalance());
            executed = false;
        } else {
            System.out.println("No se puede deshacer: comando no ha sido ejecutado");
        }
    }
    
    @Override
    public String getCommandName() {
        return "Depósito";
    }
    
    @Override
    public String getCommandDetails() {
        return String.format("Depósito: $%.2f en cuenta %s | Estado: %s", 
                           amount, account.getAccountNumber(), 
                           executed ? "Ejecutado" : "Pendiente");
    }
    
    public double getAmount() {
        return amount;
    }
    
    public ConcreteSubject getAccount() {
        return account;
    }
    
    public boolean isExecuted() {
        return executed;
    }
}
