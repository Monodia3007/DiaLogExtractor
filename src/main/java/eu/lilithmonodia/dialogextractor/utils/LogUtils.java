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
     * Logs an msg using the provided logger.
     *
     * @param logger The logger to use for logging.
     * @param msg The msg to log.
     */
    public static void logAction(@NotNull Logger logger, String msg) {
        logger.log(Level.INFO, msg);
    }

    /**
     * Logs an error message, along with the corresponding exception, using the provided logger.
     *
     * @param logger The logger to use for logging.
     * @param msg    The error message to log.
     * @param e      The exception associated with the error.
     */
    public static void logError(@NotNull Logger logger, String msg, Throwable e) {
        logger.log(Level.SEVERE, msg, e);
    }
}
