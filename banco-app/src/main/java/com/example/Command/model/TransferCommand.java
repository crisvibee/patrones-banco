package com.example.Command.model;

import com.example.Observer.model.ConcreteSubject;


public class TransferCommand implements Command {
    private ConcreteSubject sourceAccount;
    private ConcreteSubject destinationAccount;
    private double amount;
    private double sourcePreviousBalance;
    private double destinationPreviousBalance;
    private boolean executed;
    private boolean wasSuccessful;
    
  
    public TransferCommand(ConcreteSubject sourceAccount, ConcreteSubject destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.sourcePreviousBalance = sourceAccount.getBalance();
        this.destinationPreviousBalance = destinationAccount.getBalance();
        this.executed = false;
        this.wasSuccessful = false;
    }
    
    @Override
    public void execute() {
        if (!executed) {
            System.out.println("Ejecutando comando: " + getCommandName());
            
           
            if (sourcePreviousBalance >= amount) {
              
                sourceAccount.transfer(amount, destinationAccount.getAccountNumber());
                
                
                destinationAccount.deposit(amount);
                
                wasSuccessful = true;
                System.out.println("Comando ejecutado exitosamente");
            } else {
                System.out.println("Error: Fondos insuficientes para transferir $" + amount);
                System.out.println("Saldo disponible en cuenta origen: $" + sourcePreviousBalance);
                wasSuccessful = false;
            }
            
            executed = true;
        } else {
            System.out.println("Comando ya fue ejecutado anteriormente");
        }
    }
    
    @Override
    public void undo() {
        if (executed && wasSuccessful) {
            System.out.println("Deshaciendo comando: " + getCommandName());
            
           
            destinationAccount.withdraw(amount);
            sourceAccount.deposit(amount);
            
            System.out.println("Transferencia de $" + amount + " deshecha");
            System.out.println("Balance cuenta origen: $" + sourceAccount.getBalance());
            System.out.println("Balance cuenta destino: $" + destinationAccount.getBalance());
            
            executed = false;
            wasSuccessful = false;
        } else if (executed && !wasSuccessful) {
            System.out.println("No es necesario deshacer: transferencia no se completó por fondos insuficientes");
        } else {
            System.out.println("No se puede deshacer: comando no ha sido ejecutado");
        }
    }
    
    @Override
    public String getCommandName() {
        return "Transferencia";
    }
    
    @Override
    public String getCommandDetails() {
        String status;
        if (!executed) {
            status = "Pendiente";
        } else if (wasSuccessful) {
            status = "Ejecutado exitosamente";
        } else {
            status = "Fallido (fondos insuficientes)";
        }
        
        return String.format("Transferencia: $%.2f de %s a %s | Estado: %s", 
                           amount, sourceAccount.getAccountNumber(), 
                           destinationAccount.getAccountNumber(), status);
    }
    
    public double getAmount() {
        return amount;
    }
    
    
    public ConcreteSubject getSourceAccount() {
        return sourceAccount;
    }
    
    
    public ConcreteSubject getDestinationAccount() {
        return destinationAccount;
    }
    
    
    public boolean isExecuted() {
        return executed;
    }
    
    
    public boolean wasSuccessful() {
        return wasSuccessful;
    }
}
