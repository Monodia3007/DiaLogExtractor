# DiaLogExtractor

This Java application is a dialog extractor from Minecraft logs. This log analyzer provides a graphical user interface
to easily extract the dialogs logged during gameplay. It is based on the JavaFX library and designed for readability,
usability, and efficient log processing.

## Usage

Upon launching the application, you are presented with a simple, intuitive interface featuring three primary actions: *
*Upload File**, **Extract**, and **Download File**.

### Upload File

The application can process .gz or .log file types. This is the first step in the dialog extraction process. By pressing
the *Upload File* button, it prompts you to choose a file. After selecting the file, it loads and processes the file,
and its content is displayed in the original content area.

### Extract Dialog

The Extract button removes unwanted characters and extracts dialog from Minecraft's log files using highly effective
pattern matching techniques. These extracted dialogs are displayed in the *Processed Content* field. The cleaned dialog
will be presented in the processed content area.

### Download File

Upon extraction of dialogues from the original log file, Dialog Extractor will activate the *Download File* button. By
pressing this button, it allows you to save the extracted dialogs in a .txt file format to your preferred location.

## Implementation

The dialog extraction is powered by the `MinecraftLog` class. Dialogs are extracted based on a specific regular
expression pattern. The `extractDialog()` method receives the raw chat line, and then it removes unwanted character
based on a given set of rules in `cleanChatLine()`, therefore, presenting you a readable and cleaned chat log.

The application package mainly consists of four java files:

- `Launcher.java`: This class starts the application and invokes the main method of the DiaLogExtractor class.
- `DiaLogExtractor.java`: This class serves as the entry point for the application and extends the Application class
  from JavaFx library. It setups the user interface and starts the application.
- `DiaLogExtractorController.java`: This class is responsible for handling the processing of dialog extraction from an
  input file. It provides methods for uploading a file, extracting content, and downloading the processed content to an
  output file.
- `MinecraftLog.java`: This class uses the methods used to process and extract dialogs from the Minecraft logs.

The application also contains a `DiaLogExtractor.fxml` file which is responsible for the application's user interface.

## Note

This readme includes a brief overview of the DiaLogExtractor Java application. For a more comprehensive understanding of
the application, please review the individual Java files and the associated Javadoc documentation.