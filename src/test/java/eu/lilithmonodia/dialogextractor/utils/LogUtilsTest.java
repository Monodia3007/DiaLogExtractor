package eu.lilithmonodia.dialogextractor.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LogUtilsTest {

    @Test
    void testLogAction() {
        Logger mockLogger = mock(Logger.class);
        LogUtils.logAction(mockLogger, "Test");

        verify(mockLogger).log(Level.INFO, "Test");
    }

    @Test
    void testLogError() {
        Logger mockLogger = mock(Logger.class);
        Exception exception = new Exception("Exception");

        LogUtils.logError(mockLogger, "Error", exception);

        verify(mockLogger).log(Level.SEVERE, "Error", exception);
    }

    @Test
    void utilityClassTest() throws Exception {
        Constructor<LogUtils> constructor = LogUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        // This will raise the "IllegalStateException"
        assertThrows(InvocationTargetException.class, constructor::newInstance);

        constructor.setAccessible(false);
    }
}