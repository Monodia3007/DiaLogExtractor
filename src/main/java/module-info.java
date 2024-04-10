module eu.lilithmonodia.dialogextractor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens eu.lilithmonodia.dialogextractor to javafx.fxml;
    exports eu.lilithmonodia.dialogextractor;
}