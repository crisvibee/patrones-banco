package com.example.unit.Observer;

import com.example.Observer.model.EmailObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el EmailObserver
 * Prueba la funcionalidad básica del observador de email de forma aislada
 */
public class EmailObserverUnitTest {
    
    private EmailObserver emailObserver;
    
    @BeforeEach
    public void setUp() {
        // Crear un observador de email para pruebas
        emailObserver = new EmailObserver("test@example.com", "Test User");
    }
    
    @Test
    public void testEmailObserverCreation() {
        // Verificar que el observador se crea correctamente
        assertNotNull(emailObserver, "El observador no debería ser null");
        assertEquals("test@example.com", emailObserver.getEmailAddress(), 
            "El email debería ser el configurado");
        assertEquals("Test User", emailObserver.getCustomerName(), 
            "El nombre debería ser el configurado");
    }
    
    @Test
    public void testGetObserverType() {
        // Verificar que el tipo de observador se devuelve correctamente
        String observerType = emailObserver.getObserverType();
        assertNotNull(observerType, "El tipo de observador no debería ser null");
        assertTrue(observerType.contains("test@example.com"), 
            "El tipo de observador debería contener el email");
        assertTrue(observerType.contains("EmailObserver"), 
            "El tipo de observador debería contener 'EmailObserver'");
    }
    
    @Test
    public void testUpdateMethod() {
        // Verificar que el método update funciona correctamente
        String testMessage = "Test notification message";
        
        // Esta prueba verifica que el método no lanza excepciones
        // El método update actualmente solo imprime en consola
        assertDoesNotThrow(() -> {
            emailObserver.update(testMessage);
        }, "El método update no debería lanzar excepciones");
    }
    
    @Test
    public void testSetters() {
        // Verificar que los setters funcionan correctamente
        emailObserver.setEmailAddress("new@example.com");
        emailObserver.setCustomerName("New User");
        
        assertEquals("new@example.com", emailObserver.getEmailAddress(), 
            "El email debería actualizarse correctamente");
        assertEquals("New User", emailObserver.getCustomerName(), 
            "El nombre debería actualizarse correctamente");
    }
    
    @Test
    public void testObserverTypeAfterEmailChange() {
        // Verificar que el tipo de observador se actualiza después de cambiar el email
        emailObserver.setEmailAddress("updated@example.com");
        
        String observerType = emailObserver.getObserverType();
        assertTrue(observerType.contains("updated@example.com"), 
            "El tipo de observador debería reflejar el email actualizado");
    }
}