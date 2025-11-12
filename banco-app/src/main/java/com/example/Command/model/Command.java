package com.example.Command.model;


public interface Command {
    /**
     * Ejecuta la operación del comando
     */
    void execute();
    
    /**
     * Deshace la operación del comando (si es posible)
     */
    void undo();
    
    /**
     * Obtiene el nombre del comando
     * @return Nombre del comando
     */
    String getCommandName();
    
    /**
     * Obtiene información detallada del comando
     * @return Información del comando
     */
    String getCommandDetails();
}
