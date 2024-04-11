package eu.lilithmonodia.dialogextractor;

import eu.lilithmonodia.dialogextractor.data.MinecraftLog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * The DialogExtractorController class handles the processing of dialog extraction from an input file.
 * It provides methods for uploading a file, extracting content, and downloading the processed content to an output file.
 */
public class DialogExtractorController {
    private static final Logger LOGGER = Logger.getLogger(DialogExtractorController.class.getName());

    @FXML
    private Button uploadButton;
    @FXML
    private Button extractButton;
    @FXML
    private Button downloadButton;
    @FXML
    private TextArea originalContentArea;
    @FXML
    private TextArea processedContentArea;
    @FXML
    private TextField uploadFilePath;
    @FXML
    private TextField downloadFilePath;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        this.uploadButton.setOnAction(event -> uploadFile());
        this.extractButton.setOnAction(event -> extractContent());
        this.downloadButton.setOnAction(event -> downloadFile());
        this.downloadButton.setDisable(true);
    }

    /**
     * Uploads a file to a specified location.
     * <p>
     * This method retrieves the current window from the upload button's scene
     * and prompts the user to choose a file. If a file is selected, its absolute
     * path is displayed in the uploadFilePath text field and the file is processed.
     */
    private void uploadFile() {
        LOGGER.info("Attempting to upload file ...");
        Window window = uploadButton.getScene().getWindow();
        File file = chooseFile(window);
        if (file != null) {
            uploadFilePath.setText(file.getAbsolutePath());
            processFile(file);
        }
        LOGGER.info("File upload finished successfully.");
    }

    /**
     * Downloads a file by writing the content from a text area to a specified output file.
     * The file path of the downloaded file will be displayed in a text field.
     */
    private void downloadFile() {
        LOGGER.info("Attempting to download file ...");
        Window window = downloadButton.getScene().getWindow();
        File outFile = chooseOutputFile(window);
        String outputText = processedContentArea.getText();
        if (outFile != null && !outputText.isEmpty()) {
            downloadFilePath.setText(outFile.getAbsolutePath());
            writeToFile(outFile, outputText);
        }
        LOGGER.info("File download finished successfully.");
    }

    /**
     * Extracts content from the originalContentArea, processes it, and sets the processed content in the processedContentArea.
     * Sets the enabled/disabled state of the downloadButton depending on the extracted content.
     */
    private void extractContent() {
        LOGGER.info("Attempting to extract content ...");
        String content = originalContentArea.getText();
        MinecraftLog minecraftLog = new MinecraftLog(content);
        String outputText = minecraftLog.extractDialog().log();
        processedContentArea.setText(outputText);
        downloadButton.setDisable(outputText.isEmpty());
        LOGGER.info("Content extraction finished successfully.");
    }

    /**
     * Chooses a file from a file chooser dialog.
     *
     * @param window The window associated with the file chooser dialog.
     *
     * @return The chosen file, or null if no file was selected.
     */
    private File chooseFile(Window window) {
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
    private void processFile(@NotNull File file) {
        LOGGER.info("Attempting to process file ...");
        if (!file.isFile()) {
            throw new IllegalArgumentException("The input is not a file.");
        }

        try {
            String content = switch (getFileExtension(file)) {
                case ".gz" -> decompressGzip(file);
                case ".log" -> Files.readString(file.toPath(), Charset.forName("windows-1252"));
                default -> throw new IOException("Unsupported file extension");
            };

            originalContentArea.setText(content);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while processing the file.", e);
        }
        LOGGER.info("File processing finished successfully.");
    }

    /**
     * Retrieves the file extension of a given File object.
     *
     * @param file The File object for which to retrieve the file extension.
     *
     * @return The file extension of the given File object, or an empty string if no extension is present.
     */
    private @NotNull String getFileExtension(@NotNull File file) {
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
    private File chooseOutputFile(Window window) {
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
    private String decompressGzip(File file) throws IOException {
        try (InputStream fileIn = Files.newInputStream(file.toPath());
             GZIPInputStream gzipIn = new GZIPInputStream(fileIn);
             Reader reader = new InputStreamReader(gzipIn, "windows-1252");
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
    private void setupFileChooser(@NotNull FileChooser fileChooser, String dialogTitle, String extensionFilterDescription, String... extensionFilterExtensions) {
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
    private void writeToFile(File file, String content) {
        LOGGER.info("Attempting to write to file ...");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while writing to the file.", e);
        }
        LOGGER.info("File writing finished successfully.");
    }
}