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
    
    /**
     * Constructor del comando de transferencia
     * @param sourceAccount Cuenta de origen
     * @param destinationAccount Cuenta de destino
     * @param amount Monto a transferir
     */
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
            
            // Verificar si hay fondos suficientes en la cuenta de origen
            if (sourcePreviousBalance >= amount) {
                // Realizar la transferencia
                sourceAccount.transfer(amount, destinationAccount.getAccountNumber());
                
                // Simular el depósito en la cuenta destino (en una implementación real esto sería atómico)
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
            
            // Revertir la transferencia: devolver el dinero de destino a origen
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
    
    /**
     * Obtiene el monto de la transferencia
     * @return Monto de la transferencia
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Obtiene la cuenta de origen
     * @return Cuenta de origen
     */
    public ConcreteSubject getSourceAccount() {
        return sourceAccount;
    }
    
    /**
     * Obtiene la cuenta de destino
     * @return Cuenta de destino
     */
    public ConcreteSubject getDestinationAccount() {
        return destinationAccount;
    }
    
    /**
     * Verifica si el comando fue ejecutado
     * @return true si fue ejecutado
     */
    public boolean isExecuted() {
        return executed;
    }
    
    /**
     * Verifica si la transferencia fue exitosa
     * @return true si fue exitosa
     */
    public boolean wasSuccessful() {
        return wasSuccessful;
    }
}
