package com.example.Singleton.model;

public class DatabaseConnection {
    
    private static volatile DatabaseConnection instance;
    
    private String connectionString;
    private boolean isConnected;
    private int connectionCount;
    
    private DatabaseConnection() {
        this.connectionString = "jdbc:mysql://localhost:3306/bankdb";
        this.isConnected = false;
        this.connectionCount = 0;
        System.out.println("DatabaseConnection: Constructor llamado (solo una vez)");
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    public void connect() {
        if (!isConnected) {
            isConnected = true;
            connectionCount++;
            System.out.println("CONEXIÓN ESTABLECIDA: " + connectionString);
            System.out.println("Conexión número: " + connectionCount);
        } else {
            System.out.println("Ya está conectado a: " + connectionString);
        }
    }
    
    public void disconnect() {
        if (isConnected) {
            isConnected = false;
            System.out.println("CONEXIÓN CERRADA: " + connectionString);
        } else {
            System.out.println("No hay conexión activa");
        }
    }
    
    public void executeQuery(String query) {
        if (isConnected) {
            System.out.println("EJECUTANDO CONSULTA: " + query);
            System.out.println("Resultado: Consulta ejecutada exitosamente");
        } else {
            System.out.println("Error: No hay conexión activa. Conéctese primero.");
        }
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public String getConnectionString() {
        return connectionString;
    }
    
    public int getConnectionCount() {
        return connectionCount;
    }
    
    @Override
    public String toString() {
        return "DatabaseConnection{" +
               "connectionString='" + connectionString + '\'' +
               ", isConnected=" + isConnected +
               ", connectionCount=" + connectionCount +
               '}';
    }
}
