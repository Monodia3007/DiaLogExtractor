package eu.lilithmonodia.dialogextractor.utils;

import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static eu.lilithmonodia.dialogextractor.utils.LogUtils.logAction;
import static eu.lilithmonodia.dialogextractor.utils.LogUtils.logError;

/**
 * FileUtils class provides utility methods for file operations.
 * This class cannot be instantiated or extended as it contains only static methods.
 */
public class FileUtils {
    private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);

    /**
     * The FileUtils class provides utility methods for file-related operations.
     * <p>
     * This class cannot be instantiated or extended, as it contains only static methods.
     */
    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Allows the user to choose a file using a file chooser dialog.
     *
     * @param window       The parent window of the file chooser dialog.
     * @param isSaveDialog A flag indicating whether the file chooser should be a save dialog or an open dialog.
     *                     If true, a save dialog will be shown. If false, an open dialog will be shown.
     * @return The chosen file, or null if no file was chosen.
     */
    public static File chooseFile(Window window, boolean isSaveDialog) {
        FileChooser fileChooser = new FileChooser();
        if (isSaveDialog) {
            setupFileChooser(fileChooser, "Save As", "TXT files (*.txt)", "*.txt");
            return fileChooser.showSaveDialog(window);
        }
        setupFileChooser(fileChooser, "Open Minecraft Log File", "GZ files (*.gz), Log files (*.log)", "*.gz", "*.log");
        return fileChooser.showOpenDialog(window);
    }

    /**
     * Processes a file and sets its content in a TextArea.
     *
     * @param file                The file to process.
     * @param originalContentArea The TextArea in which to set the processed file content.
     * @throws IllegalArgumentException If the input is not a file.
     */
    public static void processFile(@NotNull File file, @NotNull TextArea originalContentArea, Charset charset) {
        logAction(LOGGER, "Attempting to process file ...");
        if (!Files.isRegularFile(file.toPath())) {
            throw new IllegalArgumentException("The input is not a file.");
        }
        try {
            var content = switch (getFileExtension(file)) {
                case ".gz" -> decompressGzip(file, charset);
                case ".log" -> Files.readString(file.toPath(), charset);
                default -> throw new IOException("Unsupported file extension");
            };
            originalContentArea.setText(content);
        } catch (IOException e) {
            logError(LOGGER, "An error occurred while processing the file.", e);
        }
        logAction(LOGGER, "File processing finished successfully.");
    }

    /**
     * Returns the file extension of the given file.
     *
     * @param file The file for which to retrieve the extension.
     * @return The file extension or an empty string if the file has no extension.
     */
    private static @NotNull String getFileExtension(@NotNull File file) {
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        }
        return fileName.substring(lastIndexOfDot);
    }

    /**
     * Decompresses a GZIP-compressed file and returns its content as a string.
     *
     * @param file The GZIP-compressed file to decompress.
     * @return The decompressed content of the file.
     * @throws IOException If an I/O error occurs while decompressing the file.
     */
    private static String decompressGzip(File file, Charset charset) throws IOException {
        try (InputStream fileIn = Files.newInputStream(file.toPath());
             GZIPInputStream gzipIn = new GZIPInputStream(fileIn);
             Reader reader = new InputStreamReader(gzipIn, charset);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Sets up the file chooser with the provided parameters.
     *
     * @param fileChooser                The file chooser to set up.
     * @param dialogTitle                The title of the file chooser dialog.
     * @param extensionFilterDescription The description of the file extension filter.
     * @param extensionFilterExtensions  The file extension filter.
     */
    private static void setupFileChooser(@NotNull FileChooser fileChooser, String dialogTitle, String extensionFilterDescription, String... extensionFilterExtensions) {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionFilterDescription, extensionFilterExtensions));
        fileChooser.setTitle(dialogTitle);
    }

    /**
     * Writes the specified content to the given file.
     *
     * @param file    The file to write to.
     * @param content The content to write to the file.
     */
    public static void writeToFile(File file, String content) {
        logAction(LOGGER, "Attempting to write to file ...");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            logError(LOGGER, "An error occurred while writing to the file.", e);
        }
        logAction(LOGGER, "File writing finished successfully.");
    }
}