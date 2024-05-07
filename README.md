# DiaLogExtractor

DiaLogExtractor is a Java application that enables Minecraft enthusiasts to efficiently extract dialog out from
Minecraft log files. With a simple and easy-to-understand graphical user interface, the application utilizes advanced
pattern-matching techniques to extract and add high readability to Minecraft's log files. It is based on the JavaFX
library and is specifically designed for usability, readability, and efficient log processing.

## Installation

Go to the release section of the repository and download the latest version of the application. Launch the .msi or .dmg
file and follow the installation instructions. Once the installation is complete, you can launch the application from
the desktop shortcut or the application folder. or you can download the .jar file and run it using the
command `java -jar DiaLogExtractor-<version>-<OS>.jar`.

## Usage

DiaLogExtractor provides a user-friendly GUI (Graphical User Interface) with three primary functionalities, which are:

1. **Upload File**
2. **Extract**
3. **Download File**

### Upload File

Press the *Upload File* button to upload the log file (.gz or .log file types are supported). You can also use the
drag-and-drop feature to upload the log file. Prior to the upload, you can select the encoding based on your preference.
Post successful upload, the log data will be presented in the original content area.

### Extract Dialog

Upon clicking the Extract button,
the application eliminates unwanted characters from the log file using advanced pattern-matching algorithms,
and readable dialogues are extracted.
These dialogues will be presented in the Processed Content area.

### Download File

After the dialogues have been successfully extracted from the logs, you can download these in a .txt format file.
Choose the preferred saving location, and the extracted dialogues will be saved into a text file.

## Implementation

The dialog extraction process is powered by the `MinecraftLog` class.
Dialogs are identified and extracted
using specifically defined regex patterns that interface with the internal Minecraft chat logging system.

DiaLogExtractor manages these tasks with the help of four Java files:

- `Launcher.java`: The class which launches the application.
- `DiaLogExtractor.java`: The entry point of the application. This sets up the user interface and starts the
  application.
- `DiaLogExtractorController.java`: Responsible for processing the dialog extraction from files. Contains methods for
  uploading files, extracting content, and saving the extracted content into an output file.
- `MinecraftLog.java`: Contains the methods responsible to process and extract dialogs from the Minecraft logs.
- `FileUtils.java`: Contains utility methods to handle file operations.
- `LogUtils.java`: Contains utility methods to handle log operations.

An additional `DiaLogExtractor.fxml` file is used to manage the application's user interface (GUI).

## Note

This readme includes a brief overview of the DiaLogExtractor Java application. For a more comprehensive understanding of
the application, please review the individual Java files and the associated Javadoc documentation.

## Contributions

Contributions, bug reports, and feature requests are always welcome and appreciated.
Feel free to open an issue if you have any.

## License

DiaLogExtractor is released under the MIT License.