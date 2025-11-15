package test.java.com.example.unit.Singleton;

import com.example.Singleton.controller.Client;
import com.example.Singleton.model.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Pruebas Unitarias - Singleton Controller (Client)")
public class ClientUnitTest {
    
    private Client client;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
    @BeforeEach
    public void setUp() {
        
        System.setOut(new PrintStream(outputStreamCaptor));
        
      
        client = new Client("TestClient");
    }
    
    @Test
    @DisplayName("Debería crear cliente con nombre correcto")
    public void testClientCreationWithName() {
        assertEquals("TestClient", client.getClientName(), "El nombre del cliente debería ser el especificado");
    }
    
    @Test
    @DisplayName("Debería obtener instancia Singleton de DatabaseConnection")
    public void testClientGetsSingletonInstance() {
        DatabaseConnection connection = client.getDatabaseConnection();
        assertNotNull(connection, "El cliente debería tener una instancia de DatabaseConnection");
        assertTrue(connection instanceof DatabaseConnection, "La conexión debería ser de tipo DatabaseConnection");
    }
    
    @Test
    @DisplayName("Debería realizar operaciones de base de datos correctamente")
    public void testPerformDatabaseOperations() {
     
        client.performDatabaseOperations();
     
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("TestClient operando"), "Debería mostrar mensaje de operación");
        assertTrue(output.contains("Conectando a la base de datos"), "Debería mostrar mensaje de conexión");
        assertTrue(output.contains("Ejecutando consultas"), "Debería mostrar mensaje de consultas");
        assertTrue(output.contains("Estado de conexión"), "Debería mostrar estado de conexión");
    }
    
    @Test
    @DisplayName("Debería cerrar conexión correctamente")
    public void testCloseConnection() {
    
        client.performDatabaseOperations();
        
     
        client.closeConnection();
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("intentando cerrar conexión"), "Debería mostrar mensaje de cierre de conexión");
    }
    
    @Test
    @DisplayName("Debería verificar estado de conexión activa")
    public void testIsConnectionActive() {
        // Asegurar que la conexión esté desconectada antes de la prueba
        DatabaseConnection.getInstance().disconnect();
        
        assertFalse(client.isConnectionActive(), "Inicialmente la conexión no debería estar activa");
      
        client.performDatabaseOperations();
        assertTrue(client.isConnectionActive(), "Después de operaciones la conexión debería estar activa");
    }
    
    @Test
    @DisplayName("Debería obtener información de conexión")
    public void testGetConnectionInfo() {
        String info = client.getConnectionInfo();
        assertNotNull(info, "La información de conexión no debería ser nula");
        assertTrue(info.contains("TestClient"), "La información debería incluir el nombre del cliente");
        assertTrue(info.contains("DatabaseConnection"), "La información debería incluir el tipo de conexión");
    }
    
    @Test
    @DisplayName("Debería mostrar mensaje de creación en consola")
    public void testClientCreationMessage() {
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("TestClient"), "Debería mostrar el nombre del cliente en la creación");
        assertTrue(output.contains("creado"), "Debería indicar que el cliente fue creado");
        assertTrue(output.contains("Instancia Singleton obtenida"), "Debería indicar que se obtuvo la instancia Singleton");
    }
    
    @Test
    @DisplayName("Múltiples clientes deberían compartir la misma instancia Singleton")
    public void testMultipleClientsShareSameSingleton() {
        Client client2 = new Client("TestClient2");
        Client client3 = new Client("TestClient3");
        
        DatabaseConnection conn1 = client.getDatabaseConnection();
        DatabaseConnection conn2 = client2.getDatabaseConnection();
        DatabaseConnection conn3 = client3.getDatabaseConnection();
        
    
        assertSame(conn1, conn2, "Clientes diferentes deberían compartir la misma instancia Singleton");
        assertSame(conn2, conn3, "Clientes diferentes deberían compartir la misma instancia Singleton");
        assertSame(conn1, conn3, "Clientes diferentes deberían compartir la misma instancia Singleton");
    }
    
    @Test
    @DisplayName("Debería mantener estado consistente después de múltiples operaciones")
    public void testConsistentStateAfterMultipleOperations() {
     
        client.performDatabaseOperations();
        boolean state1 = client.isConnectionActive();
        
        client.closeConnection();
        boolean state2 = client.isConnectionActive();
        
        client.performDatabaseOperations();
        boolean state3 = client.isConnectionActive();
      
        assertTrue(state1, "Después de operaciones, la conexión debería estar activa");
        assertFalse(state2, "Después de cerrar, la conexión no debería estar activa");
        assertTrue(state3, "Después de reconectar, la conexión debería estar activa nuevamente");
    }
}