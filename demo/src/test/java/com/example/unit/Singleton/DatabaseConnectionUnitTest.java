package com.example.unit.Singleton;

import com.example.Singleton.model.DatabaseConnection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el patrón Singleton (DatabaseConnection)
 * Prueba la funcionalidad básica del Singleton de forma aislada
 */
public class DatabaseConnectionUnitTest {
    
    @Test
    public void testSingletonInstanceCreation() {
        // Arrange & Act - Obtener instancia por primera vez
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        
        // Assert
        assertNotNull(instance1, "La instancia Singleton no debería ser nula");
        assertTrue(instance1 instanceof DatabaseConnection, "La instancia debería ser de tipo DatabaseConnection");
    }
    
    @Test
    public void testSingletonReturnsSameInstance() {
        // Arrange & Act - Obtener múltiples instancias
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        DatabaseConnection instance3 = DatabaseConnection.getInstance();
        
        // Assert - Todas las referencias deberían apuntar a la misma instancia
        assertSame(instance1, instance2, "Todas las llamadas a getInstance() deberían retornar la misma instancia");
        assertSame(instance2, instance3, "Todas las llamadas a getInstance() deberían retornar la misma instancia");
        assertSame(instance1, instance3, "Todas las llamadas a getInstance() deberían retornar la misma instancia");
    }
    
    @Test
    public void testSingletonConnectionString() {
        // Arrange
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        // Act
        String connectionString = instance.getConnectionString();
        
        // Assert
        assertNotNull(connectionString, "El string de conexión no debería ser nulo");
        assertEquals("jdbc:mysql://localhost:3306/bankdb", connectionString, 
            "El string de conexión debería coincidir con el esperado");
    }
    
    @Test
    public void testSingletonInitialState() {
        // Arrange - Resetear el estado para esta prueba específica
        DatabaseConnection instance = DatabaseConnection.getInstance();
        instance.disconnect(); // Asegurar que está desconectado
        
        // Para pruebas unitarias, verificamos el estado inicial después de reset
        // El contador mantiene el historial, pero para esta prueba nos enfocamos en el estado actual
        assertFalse(instance.isConnected(), "La conexión debería estar desconectada después de reset");
        // Nota: El contador puede no ser 0 debido a conexiones previas en otras pruebas
    }
    
    @Test
    public void testSingletonConnectDisconnect() {
        // Arrange
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        // Act - Conectar
        instance.connect();
        
        // Assert - Estado después de conectar
        assertTrue(instance.isConnected(), "La conexión debería estar activa después de connect()");
        assertEquals(1, instance.getConnectionCount(), "El contador de conexiones debería incrementarse a 1");
        
        // Act - Desconectar
        instance.disconnect();
        
        // Assert - Estado después de desconectar
        assertFalse(instance.isConnected(), "La conexión debería estar inactiva después de disconnect()");
    }
    
    @Test
    public void testSingletonMultipleConnections() {
        // Arrange
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        // Asegurar que empezamos desde un estado conocido
        if (instance.isConnected()) {
            instance.disconnect();
        }
        
        // Reiniciar el contador para esta prueba específica
        // (Nota: En un Singleton real, esto no sería posible sin modificar la clase)
        // Para esta prueba, verificaremos el comportamiento relativo
        int initialCount = instance.getConnectionCount();
        
        // Act - Conectar y desconectar múltiples veces
        instance.connect();
        instance.disconnect();
        instance.connect();
        instance.disconnect();
        instance.connect();
        
        // Assert - Verificar que el contador se incrementó correctamente
        // (3 conexiones realizadas durante esta prueba)
        assertTrue(instance.isConnected(), "La conexión debería estar activa después de la última conexión");
        assertEquals(initialCount + 3, instance.getConnectionCount(), 
            "El contador debería incrementarse en 3 conexiones desde el estado inicial de esta prueba");
    }
    
    @Test
    public void testSingletonExecuteQueryWhenConnected() {
        // Arrange
        DatabaseConnection instance = DatabaseConnection.getInstance();
        instance.connect();
        
        // Act & Assert - Ejecutar consulta cuando está conectado
        // Este test verifica que el método no lanza excepciones
        assertDoesNotThrow(() -> instance.executeQuery("SELECT * FROM accounts"),
            "executeQuery() no debería lanzar excepciones cuando está conectado");
    }
    
    @Test
    public void testSingletonExecuteQueryWhenDisconnected() {
        // Arrange
        DatabaseConnection instance = DatabaseConnection.getInstance();
        instance.disconnect(); // Asegurar que está desconectado
        
        // Act & Assert - Ejecutar consulta cuando está desconectado
        // Este test verifica que el método no lanza excepciones
        assertDoesNotThrow(() -> instance.executeQuery("SELECT * FROM accounts"),
            "executeQuery() no debería lanzar excepciones cuando está desconectado");
    }
    
    @Test
    public void testSingletonToString() {
        // Arrange
        DatabaseConnection instance = DatabaseConnection.getInstance();
        instance.connect();
        
        // Act
        String toStringResult = instance.toString();
        
        // Assert
        assertNotNull(toStringResult, "toString() no debería retornar nulo");
        assertTrue(toStringResult.contains("DatabaseConnection"), 
            "toString() debería incluir el nombre de la clase");
        assertTrue(toStringResult.contains("jdbc:mysql://localhost:3306/bankdb"),
            "toString() debería incluir el string de conexión");
        assertTrue(toStringResult.contains("isConnected=true"),
            "toString() debería indicar que está conectado");
    }
    
    @Test
    public void testSingletonThreadSafety() {
        // Arrange
        final int NUM_THREADS = 10;
        final DatabaseConnection[] instances = new DatabaseConnection[NUM_THREADS];
        
        // Act - Crear múltiples hilos que obtengan la instancia
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = DatabaseConnection.getInstance();
            });
            threads[i].start();
        }
        
        // Esperar a que todos los hilos terminen
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Assert - Todas las instancias deberían ser la misma referencia
        DatabaseConnection firstInstance = instances[0];
        for (int i = 1; i < NUM_THREADS; i++) {
            assertSame(firstInstance, instances[i], 
                "Todas las instancias obtenidas en diferentes hilos deberían ser la misma");
        }
    }
}