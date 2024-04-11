package eu.lilithmonodia.dialogextractor.utils;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtils {
    private LogUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void logAction(@NotNull Logger logger, String action) {
        logger.log(Level.INFO, action);
    }
}
