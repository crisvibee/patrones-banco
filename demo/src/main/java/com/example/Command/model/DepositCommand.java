package com.example.Command.model;

import com.example.Observer.model.ConcreteSubject;

public class DepositCommand implements Command {
    private ConcreteSubject account;
    private double amount;
    private double previousBalance;
    private boolean executed;
    
    /**
     * Constructor del comando de depósito
     * @param account Cuenta bancaria objetivo
     * @param amount Monto a depositar
     */
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
            // Simular deshacer el depósito (en una implementación real necesitaríamos más lógica)
            System.out.println("Depósito de $" + amount + " deshecho. Balance restaurado a $" + previousBalance);
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
    
    /**
     * Obtiene el monto del depósito
     * @return Monto del depósito
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
}
