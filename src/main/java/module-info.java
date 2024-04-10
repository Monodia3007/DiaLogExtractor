module eu.lilithmonodia.dialogextractor {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires org.jetbrains.annotations;

    opens eu.lilithmonodia.dialogextractor to javafx.fxml;
    exports eu.lilithmonodia.dialogextractor;
}