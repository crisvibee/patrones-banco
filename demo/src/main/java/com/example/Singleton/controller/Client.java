package com.example.Singleton.controller;

import com.example.Singleton.model.DatabaseConnection;

public class Client {
    private String clientName;
    private DatabaseConnection dbConnection;
    
    /**
     * Constructor del cliente
     * @param clientName Nombre del cliente
     */
    public Client(String clientName) {
        this.clientName = clientName;
        // Obtener la instancia Singleton
        this.dbConnection = DatabaseConnection.getInstance();
        System.out.println("Cliente '" + clientName + "' creado - Instancia Singleton obtenida");
    }
    
    /**
     * Realiza operaciones de base de datos usando la conexión Singleton
     */
    public void performDatabaseOperations() {
        System.out.println("\n--- " + clientName + " operando ---");
        
        // Conectar a la base de datos
        System.out.println("Conectando a la base de datos...");
        dbConnection.connect();
        
        // Ejecutar algunas consultas
        System.out.println("\nEjecutando consultas:");
        dbConnection.executeQuery("SELECT * FROM accounts WHERE client_name = '" + clientName + "'");
        dbConnection.executeQuery("UPDATE accounts SET balance = balance + 100 WHERE client_name = '" + clientName + "'");
        
        // Mostrar estado de la conexión
        System.out.println("\nEstado de conexión:");
        System.out.println("  Conectado: " + dbConnection.isConnected());
        System.out.println("  Total conexiones: " + dbConnection.getConnectionCount());
        System.out.println("  String conexión: " + dbConnection.getConnectionString());
        
        System.out.println("--- " + clientName + " terminó ---\n");
    }
    
    /**
     * Cierra la conexión (aunque es Singleton, muestra el comportamiento)
     */
    public void closeConnection() {
        System.out.println(clientName + " intentando cerrar conexión...");
        dbConnection.disconnect();
    }
    
    /**
     * Verifica si la conexión está activa
     * @return true si está conectado
     */
    public boolean isConnectionActive() {
        return dbConnection.isConnected();
    }
    
    /**
     * Obtiene información de la conexión
     * @return Información de la conexión
     */
    public String getConnectionInfo() {
        return "Cliente: " + clientName + ", " + dbConnection.toString();
    }
    
    /**
     * Obtiene el nombre del cliente
     * @return Nombre del cliente
     */
    public String getClientName() {
        return clientName;
    }
    
    /**
     * Obtiene la instancia de la conexión (para demostración)
     * @return Instancia Singleton
     */
    public DatabaseConnection getDatabaseConnection() {
        return dbConnection;
    }
}
