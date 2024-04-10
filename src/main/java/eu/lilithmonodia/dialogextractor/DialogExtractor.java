package eu.lilithmonodia.dialogextractor;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * The DialogExtractor class extends the Application class and serves as the entry point for the application.
 * It displays the main scene of the Dialog Extractor application.
 */
public class DialogExtractor extends Application {
    private static final Logger LOGGER = Logger.getLogger(DialogExtractor.class.getName());
    private static final String STYLESHEET = new PrimerDark().getUserAgentStylesheet();

    /**
     * The main method is the entry point of the application.
     * It launches the DialogExtractor application
     * by calling the launch method with the provided command line arguments.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application by setting the user agent stylesheet, loading the FXML file for the main scene,
     * setting the scene, setting the title of the stage, and displaying the stage.
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(@NotNull Stage stage) {
        Application.setUserAgentStylesheet(STYLESHEET);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogExtractor.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Dialog Extractor");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            LOGGER.severe("Error loading FXML file: " + e.getMessage());
        }
    }
}