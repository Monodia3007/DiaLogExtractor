package eu.lilithmonodia.dialogextractor.utils;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides utility methods for logging actions.
 * <p>
 * The LogUtils class is not meant to be instantiated or extended, as it contains only static methods.
 */
public class LogUtils {
    /**
     * This class provides utility methods for logging actions.
     * <p>
     * The LogUtils class is not meant to be instantiated or extended, as it contains only static methods.
     */
    private LogUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Logs an action using the provided logger.
     *
     * @param logger The logger to use for logging.
     * @param action The action to log.
     */
    public static void logAction(@NotNull Logger logger, String action) {
        logger.log(Level.INFO, action);
    }
}
