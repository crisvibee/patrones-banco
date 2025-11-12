package com.example.Command.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.example.Command.model.Command;


public class TransactionInvoker {
    private List<Command> commandHistory;
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;
    
    /**
     * Constructor del invocador de transacciones
     */
    public TransactionInvoker() {
        this.commandHistory = new ArrayList<>();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }
    
    /**
     * Ejecuta un comando y lo agrega al historial
     * @param command Comando a ejecutar
     */
    public void executeCommand(Command command) {
        System.out.println("\n=== Ejecutando comando a través del TransactionInvoker ===");
        System.out.println("Comando: " + command.getCommandDetails());
        
        command.execute();
        commandHistory.add(command);
        undoStack.push(command);
        redoStack.clear(); // Limpiar pila de rehacer al ejecutar nuevo comando
        
        System.out.println("Comando agregado al historial");
        System.out.println("Total de comandos en historial: " + commandHistory.size());
    }
    
    /**
     * Deshace el último comando ejecutado
     */
    public void undoLastCommand() {
        if (!undoStack.isEmpty()) {
            Command lastCommand = undoStack.pop();
            System.out.println("\n=== Deshaciendo último comando ===");
            System.out.println("Comando a deshacer: " + lastCommand.getCommandDetails());
            
            lastCommand.undo();
            redoStack.push(lastCommand);
            
            System.out.println("Comando deshecho y movido a pila de rehacer");
        } else {
            System.out.println("\nNo hay comandos para deshacer");
        }
    }
    
    /**
     * Rehace el último comando deshecho
     */
    public void redoLastCommand() {
        if (!redoStack.isEmpty()) {
            Command lastUndoneCommand = redoStack.pop();
            System.out.println("\n=== Rehaciendo último comando deshecho ===");
            System.out.println("Comando a rehacer: " + lastUndoneCommand.getCommandDetails());
            
            lastUndoneCommand.execute();
            undoStack.push(lastUndoneCommand);
            
            System.out.println("Comando rehecho y movido a pila de deshacer");
        } else {
            System.out.println("\nNo hay comandos para rehacer");
        }
    }
    
    /**
     * Muestra el historial completo de comandos ejecutados
     */
    public void showCommandHistory() {
        System.out.println("\n=== Historial de Comandos ===");
        if (commandHistory.isEmpty()) {
            System.out.println("No hay comandos en el historial");
        } else {
            for (int i = 0; i < commandHistory.size(); i++) {
                System.out.println((i + 1) + ". " + commandHistory.get(i).getCommandDetails());
            }
        }
    }
    
    /**
     * Muestra el estado actual de las pilas de deshacer y rehacer
     */
    public void showStackStatus() {
        System.out.println("\n=== Estado de Pilas ===");
        System.out.println("Pila de Deshacer: " + undoStack.size() + " comandos");
        System.out.println("Pila de Rehacer: " + redoStack.size() + " comandos");
        
        if (!undoStack.isEmpty()) {
            System.out.println("Último comando en pila de deshacer: " + 
                             undoStack.peek().getCommandDetails());
        }
        
        if (!redoStack.isEmpty()) {
            System.out.println("Último comando en pila de rehacer: " + 
                             redoStack.peek().getCommandDetails());
        }
    }
    
    /**
     * Obtiene el historial completo de comandos
     * @return Lista de comandos ejecutados
     */
    public List<Command> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }
    
    /**
     * Obtiene el número total de comandos ejecutados
     * @return Cantidad de comandos en historial
     */
    public int getTotalCommandsExecuted() {
        return commandHistory.size();
    }
    
    /**
     * Obtiene el número de comandos que pueden ser deshechos
     * @return Cantidad de comandos en pila de deshacer
     */
    public int getUndoableCommandsCount() {
        return undoStack.size();
    }
    
    /**
     * Obtiene el número de comandos que pueden ser rehechos
     * @return Cantidad de comandos en pila de rehacer
     */
    public int getRedoableCommandsCount() {
        return redoStack.size();
    }
    
    /**
     * Limpia todo el historial y pilas
     */
    public void clearAll() {
        commandHistory.clear();
        undoStack.clear();
        redoStack.clear();
        System.out.println("\nHistorial y pilas limpiados completamente");
    }
}
