package eu.lilithmonodia.dialogextractor;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DialogExtractor extends Application {
    private static final String STYLESHEET = new PrimerDark().getUserAgentStylesheet();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull Stage stage) {
        Application.setUserAgentStylesheet(STYLESHEET);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogExtractor.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Dialog Extractor");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}