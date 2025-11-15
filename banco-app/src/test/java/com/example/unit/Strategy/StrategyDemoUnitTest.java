package test.java.com.example.unit.Strategy;

import com.example.Strategy.view.StrategyDemo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class StrategyDemoUnitTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testMainMethodOutput() {
        StrategyDemo.main(new String[]{});
        String output = outContent.toString();
        
        assertTrue(output.contains("SISTEMA DE PAGOS STRATEGY"));
        assertTrue(output.contains("Método de pago:"));
        assertTrue(output.contains("PAGO CON TARJETA DE CRÉDITO:"));
        assertTrue(output.contains("procesado exitosamente"));
    }

    @Test
    void testMainMethodCompletesSuccessfully() {
        assertDoesNotThrow(() -> StrategyDemo.main(new String[]{}));
        assertFalse(outContent.toString().isEmpty());
    }
}
