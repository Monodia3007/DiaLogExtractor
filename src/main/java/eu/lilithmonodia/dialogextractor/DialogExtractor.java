package eu.lilithmonodia.dialogextractor;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;


/**
 * The DialogExtractor class is an application that extracts dialog from a Minecraft log.
 */
public class DialogExtractor extends Application {

    private static final String STYLESHEET = new PrimerDark().getUserAgentStylesheet();
    private static final Insets DEFAULT_MARGIN = new Insets(10, 10, 10, 10);

    /**
     * The main method is the entry point of the application.
     * It calls the `launch` method to start the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application and displays the Dialog Extractor UI.
     *
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(@NotNull Stage stage) {
        Application.setUserAgentStylesheet(STYLESHEET);
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        TextArea input = createTextArea(true, "Paste Minecraft log here");
        TextArea output = createTextArea(false, "");
        Button extract = new Button("Extract");
        extract.setOnAction(event -> {
            MinecraftLog minecraftLog = new MinecraftLog(input.getText());
            output.setText(minecraftLog.extractDialog().log());
        });
        root.getChildren().addAll(input, extract, output);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dialog Extractor");
        stage.show();
    }

    /**
     * Creates a TextArea with the specified editable and promptText properties.
     *
     * @param isEditable The flag indicating whether the TextArea should be editable.
     * @param promptText The prompt text to display when the TextArea is empty.
     *
     * @return A new instance of TextArea with the specified properties.
     */
    private @NotNull TextArea createTextArea(Boolean isEditable, String promptText) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(isEditable);
        textArea.setPromptText(promptText);
        HBox.setMargin(textArea, DEFAULT_MARGIN);
        return textArea;
    }
}