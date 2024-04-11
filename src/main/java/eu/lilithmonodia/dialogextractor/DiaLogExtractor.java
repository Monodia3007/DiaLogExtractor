package eu.lilithmonodia.dialogextractor;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The DiaLogExtractor class extends the Application class and serves as the entry point for the application.
 * It displays the main scene of the Dialog Extractor application.
 */
public class DiaLogExtractor extends Application {
    private static final Logger LOGGER = Logger.getLogger(DiaLogExtractor.class.getName());
    private static final String STYLESHEET = new PrimerDark().getUserAgentStylesheet();

    /**
     * The main method is the entry point of the application.
     * It launches the DiaLogExtractor application
     * by calling the launch method with the provided command line arguments.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Application launch started...");
        launch(args);
        LOGGER.info("Application launch completed.");
    }

    /**
     * Starts the application by setting the user agent stylesheet, loading the FXML file for the main scene,
     * setting the scene, setting the title of the stage, and displaying the stage.
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(@NotNull Stage stage) {
        LOGGER.info("Preparing to start the application...");

        Application.setUserAgentStylesheet(STYLESHEET);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DiaLogExtractor.fxml"));
            LOGGER.info("FXML file loaded successfully.");

            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
            LOGGER.info("Application icon loaded successfully.");

            stage.getIcons().add(icon);

            Parent root = loader.load();
            stage.setScene(new Scene(root));

            stage.setTitle("Dialog Extractor");
            LOGGER.info("Application title set successfully.");

            stage.setResizable(false);
            stage.show();
            LOGGER.info("Application started successfully.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading FXML file", e);
        }
    }
}