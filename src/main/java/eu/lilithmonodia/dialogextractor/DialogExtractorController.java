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
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

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

    @FXML
    public void initialize() {
        this.uploadButton.setOnAction(event -> uploadFile());
        this.extractButton.setOnAction(event -> extractContent());
        this.downloadButton.setOnAction(event -> downloadFile());
        this.downloadButton.setDisable(true);
    }

    private void uploadFile() {
        Window window = uploadButton.getScene().getWindow();
        File file = chooseFile(window);
        if (file != null) {
            uploadFilePath.setText(file.getAbsolutePath());
            processFile(file);
        }
    }

    private void downloadFile() {
        Window window = downloadButton.getScene().getWindow();
        File outFile = chooseOutputFile(window);
        String outputText = processedContentArea.getText();
        if (outFile != null && !outputText.isEmpty()) {
            downloadFilePath.setText(outFile.getAbsolutePath());
            writeToFile(outFile, outputText);
        }
    }

    private void extractContent() {
        String content = originalContentArea.getText();
        MinecraftLog minecraftLog = new MinecraftLog(content);
        String outputText = minecraftLog.extractDialog().log();
        processedContentArea.setText(outputText);
        downloadButton.setDisable(outputText.isEmpty());
    }

    private File chooseFile(Window window) {
        FileChooser fileChooser = new FileChooser();
        setupFileChooser(fileChooser, "Open Minecraft Log File", "GZ files (*.gz), Log files (*.log)", "*.gz", "*.log");
        return fileChooser.showOpenDialog(window);
    }

    private void processFile(@NotNull File file) {
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
            LOGGER.severe("An error occurred while processing the file: " + e.getMessage());
        }
    }

    private @NotNull String getFileExtension(@NotNull File file) {
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        }
        return fileName.substring(lastIndexOfDot);
    }

    private File chooseOutputFile(Window window) {
        FileChooser outputFileChooser = new FileChooser();
        setupFileChooser(outputFileChooser, "Save As", "TXT files (*.txt)", "*.txt");
        return outputFileChooser.showSaveDialog(window);
    }

    private String decompressGzip(File file) throws IOException {
        try (InputStream fileIn = Files.newInputStream(file.toPath());
             GZIPInputStream gzipIn = new GZIPInputStream(fileIn);
             Reader reader = new InputStreamReader(gzipIn, "windows-1252");
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    private void setupFileChooser(@NotNull FileChooser fileChooser, String dialogTitle, String extensionFilterDescription, String... extensionFilterExtensions) {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionFilterDescription, extensionFilterExtensions));
        fileChooser.setTitle(dialogTitle);
    }

    private void writeToFile(File file, String content) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            LOGGER.severe("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}