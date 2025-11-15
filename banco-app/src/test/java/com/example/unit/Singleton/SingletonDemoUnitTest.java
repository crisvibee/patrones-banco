package test.java.com.example.unit.Singleton;

import com.example.Singleton.view.SingletonDemo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias - SingletonDemo")
public class SingletonDemoUnitTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("Debería ejecutar la demostración sin errores")
    void testSingletonDemoExecution() {
        SingletonDemo.main(new String[]{});
        
        String output = outputStream.toString();
        assertNotNull(output);
        assertTrue(output.length() > 0);
    }

    @Test
    @DisplayName("Debería mostrar mensaje de conexión Singleton")
    void testShowsSingletonConnectionMessage() {
        SingletonDemo.main(new String[]{});
        
        String output = outputStream.toString();
        assertTrue(output.contains("CONEXION BASE DE DATOS SINGLETON"));
    }

    @Test
    @DisplayName("Debería verificar instancia compartida")
    void testVerifiesSharedInstance() {
        SingletonDemo.main(new String[]{});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Misma instancia"));
    }

    @Test
    @DisplayName("Debería realizar operaciones con clientes")
    void testPerformsClientOperations() {
        SingletonDemo.main(new String[]{});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Realizando operaciones"));
    }

    @Test
    @DisplayName("Debería manejar cierre de conexión")
    void testHandlesConnectionClosure() {
        SingletonDemo.main(new String[]{});
        
        String output = outputStream.toString();
        assertTrue(output.contains("cerrar conexión"));
    }

    @Test
    @DisplayName("Debería intentar reconexión")
    void testAttemptsReconnection() {
        SingletonDemo.main(new String[]{});
        
        String output = outputStream.toString();
        assertTrue(output.contains("Intentando reconectar"));
    }

    @Test
    @DisplayName("Debería ejecutarse sin excepciones")
    void testRunsWithoutExceptions() {
        assertDoesNotThrow(() -> {
            SingletonDemo.main(new String[]{});
        });
    }
}