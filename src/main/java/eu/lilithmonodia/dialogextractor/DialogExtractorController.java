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

public class DialogExtractorController {

    @FXML
    private Button extractButton;

    @FXML
    private TextArea originalContentArea;

    @FXML
    private TextArea processedContentArea;

    @FXML
    public void initialize() {
        this.extractButton.setOnAction(event -> extractContent());
    }

    private void extractContent() {
        Window window = extractButton.getScene().getWindow();
        File file = chooseFile(window);
        if (file != null) {
            processFile(file, window);
        }
    }

    private File chooseFile(Window window) {
        FileChooser fileChooser = new FileChooser();
        setupFileChooser(fileChooser, "Open Minecraft Log File", "GZ files (*.gz), Log files (*.log)", "*.gz", "*.log");
        return fileChooser.showOpenDialog(window);
    }

    private void processFile(@NotNull File file, Window window) {
        try {
            String content;
            if (file.getName().endsWith(".gz")) {
                content = decompressGzip(file);
            } else if (file.getName().endsWith(".log")) {
                content = new String(Files.readAllBytes(file.toPath()), Charset.forName("windows-1252"));
            } else {
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
            e.printStackTrace();
        }
    }

    private File chooseOutputFile(Window window) {
        FileChooser outputFileChooser = new FileChooser();
        setupFileChooser(outputFileChooser, "Save As", "TXT files (*.txt)", "*.txt");
        return outputFileChooser.showSaveDialog(window);
    }

    private String decompressGzip(@NotNull File file) throws IOException {
        try {
            InputStream fileIn = Files.newInputStream(file.toPath());
            InputStream gzipIn = new GZIPInputStream(fileIn);
            InputStreamReader reader = new InputStreamReader(gzipIn, "windows-1252");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void setupFileChooser(@NotNull FileChooser fileChooser, String dialogTitle, String extensionFilterDescription, String... extensionFilterExtensions) {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionFilterDescription, extensionFilterExtensions));
        fileChooser.setTitle(dialogTitle);
    }

    private void writeToFile(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}