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


public class DialogExtractor extends Application {

    private static final String STYLESHEET = new PrimerDark().getUserAgentStylesheet();
    private static final Insets DEFAULT_MARGIN = new Insets(10, 10, 10, 10);

    public static void main(String[] args) {
        launch(args);
    }

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

    private @NotNull TextArea createTextArea(Boolean isEditable, String promptText) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(isEditable);
        textArea.setPromptText(promptText);
        HBox.setMargin(textArea, DEFAULT_MARGIN);
        return textArea;
    }
}