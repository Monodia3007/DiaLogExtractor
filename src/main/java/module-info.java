/**
 * <h2>Module Information - Dialogue Information Extraction:</h2>
 * <p>This module is primarily tasked with extracting dialogue information. It interfaces with various other modules for the necessary functionalities and features:</p>
 * <ul>
 *     <li><b>javafx.controls:</b> Provides the main JavaFX framework for UI controls.</li>
 *     <li><b>javafx.fxml:</b> Offers support for JavaFX FXML files.</li>
 *     <li><b>atlantafx.base:</b> Provides additional UI controls and functionalities.</li>
 *     <li><b>org.jetbrains.annotations:</b> Delivers annotations for code analysis.</li>
 *     <li><b>java.logging:</b> Facilitates logging capabilities.</li>
 *     <li><b>java.desktop:</b> Grants access to the desktop features.</li>
 * </ul>
 * <h3>Package Accessibility: </h3>
 * <ul>
 *     <li>The {@code eu.lilithmonodia.dialogextractor} package is opened for runtime access to {@code javafx.fxml}.</li>
 *     <li>The {@code eu.lilithmonodia.dialogextractor.data} package is also opened for runtime access to {@code javafx.fxml}.</li>
 * </ul>
 * <h3>Exported Packages:</h3>
 * <ul>
 *     <li>The following packages are exported for external usage: {@code eu.lilithmonodia.dialogextractor} and {@code eu.lilithmonodia.dialogextractor.data}.</li>
 * </ul>
 */
module eu.lilithmonodia.dialogextractor {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires org.jetbrains.annotations;
    requires java.logging;
    requires java.desktop;

    opens eu.lilithmonodia.dialogextractor to javafx.fxml;
    exports eu.lilithmonodia.dialogextractor;
    exports eu.lilithmonodia.dialogextractor.data;
    opens eu.lilithmonodia.dialogextractor.data to javafx.fxml;
}