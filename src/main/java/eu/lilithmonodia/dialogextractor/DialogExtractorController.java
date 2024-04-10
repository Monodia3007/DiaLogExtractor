package eu.lilithmonodia.dialogextractor;

import eu.lilithmonodia.dialogextractor.data.MinecraftLog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * Controller class for the Dialog Extractor UI.
 */
public class DialogExtractorController {

    @FXML
    private Button extractButton;
    @FXML
    private TextArea originalContentArea;
    @FXML
    private TextArea processedContentArea;

    /**
     * Initializes the controller class.
     * Sets an action event handler for the extractButton.
     * The event handler calls the extractContent method when the button is clicked.
     */
    @FXML
    public void initialize() {
        this.extractButton.setOnAction(event -> extractContent());
    }

    /**
     * Extracts the content of a selected file.
     * <p>
     * This method prompts the user to choose a file using a file chooser dialog. If a file
     * is selected, the method will process the file and extract its content.
     *
     * @throws UnsupportedOperationException if the JavaFX runtime environment is not available
     */
    private void extractContent() {
        Window window = extractButton.getScene().getWindow();
        File file = chooseFile(window);
        if (file != null) {
            processFile(file, window);
        }
    }

    /**
     * Allows the user to choose a file using a file chooser dialog.
     *
     * @param window the parent window for the file chooser dialog.
     *
     * @return the selected file, or null if no file was selected.
     */
    private File chooseFile(Window window) {
        FileChooser fileChooser = new FileChooser();
        setupFileChooser(fileChooser, "Open Minecraft Log File", "GZ files (*.gz), Log files (*.log)", "*.gz", "*.log");
        return fileChooser.showOpenDialog(window);
    }

    /**
     * Processes a file and extracts information from it. The extracted information is then displayed
     * in the UI and optionally written to an output file.
     *
     * @param file   The file to process.
     * @param window The window where the UI is displayed.
     */
    private void processFile(@NotNull File file, Window window) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("The input is not a file.");
        }

        try {
            String content;
            switch (getFileExtension(file)) {
                case ".gz":
                    content = decompressGzip(file);
                    break;
                case ".log":
                    content = new String(Files.readAllBytes(file.toPath()), Charset.forName("windows-1252"));
                    break;
                default:
                    throw new IOException("Unsupported file extension");
            }

            MinecraftLog minecraftLog = new MinecraftLog(content);
            String outputText = minecraftLog.extractDialog().log();
            originalContentArea.setText(content);
            processedContentArea.setText(outputText);
            File outFile = chooseOutputFile(window);
            if (outFile != null) {
                writeToFile(outFile, outputText);
            }
        } catch (IOException e) {
            // Handle the exception properly (show a warning to the user, or something similar)
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the file extension from the given file.
     *
     * @param file The file to retrieve the extension from. Cannot be null.
     *
     * @return The file extension, including the dot, or an empty string if there is no extension.
     */
    private @NotNull String getFileExtension(@NotNull File file) {
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return ""; // No extension.
        }
        return fileName.substring(lastIndexOfDot); // Includes the ".".
    }

    /**
     * Chooses the output file using a file chooser dialog.
     *
     * @param window the parent window for the file chooser dialog
     * @return the chosen output file, or null if no file is selected
     */
    private File chooseOutputFile(Window window) {
        FileChooser outputFileChooser = new FileChooser();
        setupFileChooser(outputFileChooser, "Save As", "TXT files (*.txt)", "*.txt");
        return outputFileChooser.showSaveDialog(window);
    }

    /**
     * Decompresses a GZIP file.
     *
     * @param file The GZIP file to decompress.
     *
     * @return The contents of the decompressed file as a single string.
     *
     * @throws IOException If an I/O error occurs while decompressing the file.
     */
    private String decompressGzip(File file) throws IOException {
        try (InputStream fileIn = Files.newInputStream(file.toPath());
             GZIPInputStream gzipIn = new GZIPInputStream(fileIn);
             Reader reader = new InputStreamReader(gzipIn, "windows-1252");
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Sets up the file chooser with the specified dialog title, extension filter description, and extension filter extensions.
     *
     * @param fileChooser               The file chooser to set up.
     * @param dialogTitle               The title of the file chooser dialog.
     * @param extensionFilterDescription The description of the extension filter.
     * @param extensionFilterExtensions  The extensions for the file filter.
     */
    private void setupFileChooser(@NotNull FileChooser fileChooser, String dialogTitle, String extensionFilterDescription, String... extensionFilterExtensions) {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionFilterDescription, extensionFilterExtensions));
        fileChooser.setTitle(dialogTitle);
    }

    /**
     * Writes the given content to the specified file.
     *
     * @param file    the file to write the content to
     * @param content the content to write to the file
     * @throws IOException if an I/O error occurs while writing to the file
     */
    private void writeToFile(File file, String content) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }
}