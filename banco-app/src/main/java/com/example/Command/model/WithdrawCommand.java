package com.example.Command.model;

import com.example.Observer.model.ConcreteSubject;


public class WithdrawCommand implements Command {
    private ConcreteSubject account;
    private double amount;
    private double previousBalance;
    private boolean executed;
    private boolean wasSuccessful;
    
    /**
     * Constructor del comando de retiro
     * @param account Cuenta bancaria objetivo
     * @param amount Monto a retirar
     */
    public WithdrawCommand(ConcreteSubject account, double amount) {
        this.account = account;
        this.amount = amount;
        this.previousBalance = account.getBalance();
        this.executed = false;
        this.wasSuccessful = false;
    }
    
    @Override
    public void execute() {
        if (!executed) {
            System.out.println("Ejecutando comando: " + getCommandName());
            
            // Verificar si hay fondos suficientes antes de ejecutar
            if (previousBalance >= amount) {
                account.withdraw(amount);
                wasSuccessful = true;
                System.out.println("Comando ejecutado exitosamente");
            } else {
                System.out.println("Error: Fondos insuficientes para retirar $" + amount);
                System.out.println("Saldo disponible: $" + previousBalance);
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
            // Revertir el retiro depositando el monto retirado
            account.deposit(amount);
            System.out.println("Retiro de $" + amount + " deshecho. Balance restaurado a $" + account.getBalance());
            executed = false;
            wasSuccessful = false;
        } else if (executed && !wasSuccessful) {
            System.out.println("No es necesario deshacer: retiro no se completó por fondos insuficientes");
        } else {
            System.out.println("No se puede deshacer: comando no ha sido ejecutado");
        }
    }
    
    @Override
    public String getCommandName() {
        return "Retiro";
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
        
        return String.format("Retiro: $%.2f de cuenta %s | Estado: %s", 
                           amount, account.getAccountNumber(), status);
    }
    
    /**
     * Obtiene el monto del retiro
     * @return Monto del retiro
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Obtiene la cuenta objetivo
     * @return Cuenta bancaria
     */
    public ConcreteSubject getAccount() {
        return account;
    }
    
    /**
     * Verifica si el comando fue ejecutado
     * @return true si fue ejecutado
     */
    public boolean isExecuted() {
        return executed;
    }
    
    /**
     * Verifica si el retiro fue exitoso
     * @return true si fue exitoso
     */
    public boolean wasSuccessful() {
        return wasSuccessful;
    }
}
