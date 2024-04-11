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