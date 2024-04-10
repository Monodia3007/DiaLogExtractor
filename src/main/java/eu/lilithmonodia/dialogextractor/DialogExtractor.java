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
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull Stage stage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);

        TextArea input = new TextArea();
        input.setWrapText(true);
        input.setPromptText("Paste Minecraft log here");
        HBox.setMargin(input, new Insets(10, 10, 10, 10));

        TextArea output = new TextArea();
        output.setWrapText(true);
        output.setEditable(false);
        HBox.setMargin(output, new Insets(10, 10, 10, 10));

        Button extract = new Button("Extract");
        extract.setOnAction(event -> {
            MinecraftLog minecraftLog = new MinecraftLog(input.getText());
            output.setText(minecraftLog.extractDialog().getLog());
        });

        root.getChildren().addAll(input, extract, output);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dialog Extractor");
        stage.show();
    }
}