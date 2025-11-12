package com.example.Singleton.model;

public class DatabaseConnection {
    // Instancia única (volatile para thread safety en entornos multi-hilo)
    private static volatile DatabaseConnection instance;
    
    // Información de conexión simulada
    private String connectionString;
    private boolean isConnected;
    private int connectionCount;
    
    /**
     * Constructor privado para prevenir instanciación directa
     */
    private DatabaseConnection() {
        this.connectionString = "jdbc:mysql://localhost:3306/bankdb";
        this.isConnected = false;
        this.connectionCount = 0;
        System.out.println("DatabaseConnection: Constructor llamado (solo una vez)");
    }
    
    /**
     * Método estático para obtener la instancia única (Double-Checked Locking)
     * @return Instancia única de DatabaseConnection
     */
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
    
    /**
     * Simula la conexión a la base de datos
     */
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
    
    /**
     * Simula la desconexión de la base de datos
     */
    public void disconnect() {
        if (isConnected) {
            isConnected = false;
            System.out.println("CONEXIÓN CERRADA: " + connectionString);
        } else {
            System.out.println("No hay conexión activa");
        }
    }
    
    /**
     * Ejecuta una consulta simulada
     * @param query Consulta SQL a ejecutar
     */
    public void executeQuery(String query) {
        if (isConnected) {
            System.out.println("EJECUTANDO CONSULTA: " + query);
            System.out.println("Resultado: Consulta ejecutada exitosamente");
        } else {
            System.out.println("Error: No hay conexión activa. Conéctese primero.");
        }
    }
    
    /**
     * Obtiene el estado de conexión
     * @return true si está conectado, false otherwise
     */
    public boolean isConnected() {
        return isConnected;
    }
    
    /**
     * Obtiene el string de conexión
     * @return String de conexión
     */
    public String getConnectionString() {
        return connectionString;
    }
    
    /**
     * Obtiene el contador de conexiones
     * @return Número total de conexiones establecidas
     */
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
