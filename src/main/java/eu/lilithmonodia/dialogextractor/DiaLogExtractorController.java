package eu.lilithmonodia.dialogextractor;

import eu.lilithmonodia.dialogextractor.data.MinecraftLog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.io.File;
import java.util.logging.Logger;

import static eu.lilithmonodia.dialogextractor.utils.FileUtils.*;

/**
 * The DialogExtractorController class handles the processing of dialog extraction from an input file.
 * It provides methods for uploading a file, extracting content, and downloading the processed content to an output file.
 */
public class DiaLogExtractorController {
    private static final Logger LOGGER = Logger.getLogger(DiaLogExtractorController.class.getName());

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
            processFile(file, originalContentArea);
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
}