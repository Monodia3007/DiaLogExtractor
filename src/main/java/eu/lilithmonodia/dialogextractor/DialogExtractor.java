package eu.lilithmonodia.dialogextractor;

import atlantafx.base.theme.PrimerDark;
import eu.lilithmonodia.dialogextractor.data.MinecraftLog;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class DialogExtractor extends Application {
    private static final String STYLESHEET = new PrimerDark().getUserAgentStylesheet();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull Stage stage) {
        Application.setUserAgentStylesheet(STYLESHEET);
        VBox root = initRootContainer();
        extractAndDialog(root, stage);
        showScene(stage, root);
    }

    private void extractAndDialog(@NotNull VBox root, Stage stage) {
        Button extractButton = new Button("Extract");
        TextArea originalContentArea = createTextArea("Original content will be displayed here...");
        TextArea processedContentArea = createTextArea("Processed content will appear here...");
        root.getChildren().addAll(extractButton, originalContentArea, processedContentArea);
        extractButton.setOnAction(event -> extractContent(stage, originalContentArea, processedContentArea));
    }

    private @NotNull TextArea createTextArea(String promptText) {
        TextArea textArea = new TextArea();
        textArea.setPromptText(promptText);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        return textArea;
    }

    private @NotNull VBox initRootContainer() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        return root;
    }

    private void showScene(@NotNull Stage stage, VBox root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dialog Extractor");
        stage.show();
    }

    private void extractContent(Stage stage, TextArea originalContentArea, TextArea processedContentArea) {
        File file = chooseFile(stage);
        if (file != null) {
            processFile(file, stage, originalContentArea, processedContentArea);
        }
    }

    private File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        setupFileChooser(fileChooser, "Open Minecraft Log File", "GZ files (*.gz)", "*.gz");
        return fileChooser.showOpenDialog(stage);
    }

    private void processFile(File file, Stage stage, @NotNull TextArea originalContentArea, @NotNull TextArea processedContentArea) {
        try {
            String content = decompressGzip(file);
            MinecraftLog minecraftLog = new MinecraftLog(content);
            String outputText = minecraftLog.extractDialog().log();
            originalContentArea.setText(content);  // Display original content in the TextArea
            processedContentArea.setText(outputText);  // Display processed content in the TextArea

            // Prompt user to choose output file
            File outFile = chooseOutputFile(stage);
            // Create output file or append if it already exists
            if (outFile != null) {
                writeToFile(outFile, outputText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File chooseOutputFile(Stage stage) {
        FileChooser outputFileChooser = new FileChooser();
        setupFileChooser(outputFileChooser, "Save As", "TXT files (*.txt)", "*.txt");
        return outputFileChooser.showSaveDialog(stage);
    }

    private @NotNull String decompressGzip(@NotNull File file) throws IOException {
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