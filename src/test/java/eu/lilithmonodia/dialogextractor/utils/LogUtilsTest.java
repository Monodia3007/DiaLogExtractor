package eu.lilithmonodia.dialogextractor.utils;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * The LogUtilsTest class contains unit tests for the LogUtils class.
 * It tests the logAction and logError methods.
 */
class LogUtilsTest {

    /**
     * Test case for the logAction method of LogUtils class.
     * It creates a mock Logger object and calls the logAction method with a test message.
     * It then verifies if the info method of the Logger class was called with the correct parameters.
     */
    @Test
    void testLogAction() {
        Logger mockLogger = mock(Logger.class);
        LogUtils.logAction(mockLogger, "Test INFO");

        verify(mockLogger).info("Test INFO");
    }

    /**
     * Test case for the logError method of LogUtils class.
     * It creates a mock Logger object and an Exception, then calls the logError method with a test message and the exception.
     * It then verifies if the error method of the Logger class was called with the correct parameters.
     */
    @Test
    void testLogError() {
        Logger mockLogger = mock(Logger.class);
        Exception exception = new Exception("Test Exception");

        LogUtils.logError(mockLogger, "Test ERROR", exception);

        verify(mockLogger).error("Test ERROR", exception);
    }

    /**
     * Test case for the constructor of the LogUtils class.
     * It checks if the constructor is private and if it throws an exception when accessed.
     */
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