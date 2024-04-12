package eu.lilithmonodia.dialogextractor.utils;

import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class FileUtils {
    private static final Charset WINDOWS_CHARSET = Charset.forName("windows-1252");
    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Chooses a file from a file chooser dialog.
     *
     * @param window The window associated with the file chooser dialog.
     *
     * @return The chosen file, or null if no file was selected.
     */
    public static File chooseFile(Window window) {
        FileChooser fileChooser = new FileChooser();
        setupFileChooser(fileChooser, "Open Minecraft Log File", "GZ files (*.gz), Log files (*.log)", "*.gz", "*.log");
        return fileChooser.showOpenDialog(window);
    }

    /**
     * Processes a file by decompressing a gzip file or reading a log file, and sets the content in
     * the originalContentArea text area.
     *
     * @param file The file to process.
     *
     * @throws IllegalArgumentException If the input is not a file.
     */
    public static void processFile(@NotNull File file, @NotNull TextArea originalContentArea) {
        LogUtils.logAction(LOGGER, "Attempting to process file ...");
        if (!Files.isRegularFile(file.toPath())) {
            throw new IllegalArgumentException("The input is not a file.");
        }
        try {
            var content = switch (getFileExtension(file)) {
                case ".gz" -> decompressGzip(file);
                case ".log" -> Files.readString(file.toPath(), WINDOWS_CHARSET);
                default -> throw new IOException("Unsupported file extension");
            };
            originalContentArea.setText(content);
        } catch (IOException e) {
            LogUtils.logError(LOGGER, "An error occurred while processing the file.", e);
        }
        LogUtils.logAction(LOGGER, "File processing finished successfully.");
    }

    /**
     * Retrieves the file extension of a given File object.
     *
     * @param file The File object for which to retrieve the file extension.
     *
     * @return The file extension of the given File object, or an empty string if no extension is present.
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
     * Chooses a file from a file chooser dialog.
     *
     * @param window The window associated with the file chooser dialog.
     *
     * @return The chosen file, or null if no file was selected.
     */
    public static File chooseOutputFile(Window window) {
        FileChooser outputFileChooser = new FileChooser();
        setupFileChooser(outputFileChooser, "Save As", "TXT files (*.txt)", "*.txt");
        return outputFileChooser.showSaveDialog(window);
    }

    /**
     * Decompresses a gzip file and returns the content as a string.
     *
     * @param file The file to decompress.
     *
     * @return The decompressed content of the file.
     *
     * @throws IOException If an I/O error occurs.
     */
    private static String decompressGzip(File file) throws IOException {
        try (InputStream fileIn = Files.newInputStream(file.toPath());
             GZIPInputStream gzipIn = new GZIPInputStream(fileIn);
             Reader reader = new InputStreamReader(gzipIn, WINDOWS_CHARSET);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Sets up the file chooser with the specified title, extension filter description, and extensions.
     *
     * @param fileChooser                The file chooser to set up.
     * @param dialogTitle                The title of the file chooser dialog.
     * @param extensionFilterDescription The description of the file extension filter.
     * @param extensionFilterExtensions  The file extensions to include in the file extension filter.
     */
    private static void setupFileChooser(@NotNull FileChooser fileChooser, String dialogTitle, String extensionFilterDescription, String... extensionFilterExtensions) {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionFilterDescription, extensionFilterExtensions));
        fileChooser.setTitle(dialogTitle);
    }

    /**
     * Writes the given content to the specified file.
     *
     * @param file    The file to write to.
     * @param content The content to write.
     */
    public static void writeToFile(File file, String content) {
        LogUtils.logAction(LOGGER, "Attempting to write to file ...");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            LogUtils.logError(LOGGER, "An error occurred while writing to the file.", e);
        }
        LogUtils.logAction(LOGGER, "File writing finished successfully.");
    }
}
