package eu.lilithmonodia.dialogextractor.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a Minecraft log.
 */
public record MinecraftLog(String log) {
    private static final Logger LOGGER = Logger.getLogger(MinecraftLog.class.getName());

    /**
     * Regular expression pattern for matching color codes in Minecraft logs.
     * Color codes are represented by a section symbol followed by a single character.
     */
    public static final String COLOUR_CODE_REGEX = "§.";

    private static final String DIALOG_PREFIX = "[CHAT]";

    /**
     * Extracts the dialog from the Minecraft log.
     *
     * @return The extracted dialog as a MinecraftLog object.
     */
    @Contract(" -> new")
    public @NotNull MinecraftLog extractDialog() {
        LOGGER.info("Extracting dialog from Minecraft log...");
        List<String> extractedDialogs = new ArrayList<>();
        cleanAndExtractDialog(extractedDialogs);
        LOGGER.info("Dialog extraction completed successfully.");
        return new MinecraftLog(String.join("\n", extractedDialogs));
    }

    /**
     * Removes unwanted characters and extracts dialog from the Minecraft log.
     *
     * @param dialogList The list to store the extracted dialog lines.
     */
    private void cleanAndExtractDialog(List<String> dialogList) {
        LOGGER.info("Starting the dialogue cleaning and extraction process...");
        String[] lines = this.log.split("\n");
        for (String line : lines) {
            if (line.contains(DIALOG_PREFIX)) {
                LOGGER.info("Processing a line with chat dialog...");
                String cleanedLine = cleanChatLine(line);
                if (!"Shaders Reloaded!".equals(cleanedLine)) {
                    dialogList.add(cleanedLine);
                    LOGGER.info("Line added to the dialog list.");
                }
            }
        }
        LOGGER.info("Dialogue cleaning and extraction process completed.");
    }

    /**
     * Removes unwanted characters from a chat line based on a given pattern.
     *
     * @param rawChatLine The raw chat line to be cleaned.
     *
     * @return The cleaned chat line.
     */
    private @NotNull String cleanChatLine(@NotNull String rawChatLine) {
        LOGGER.info("Starting chat line cleaning process...");
        int chatIndex = rawChatLine.indexOf(DIALOG_PREFIX);
        String cleanedLine = (chatIndex != -1)
                ? rawChatLine.substring(chatIndex + DIALOG_PREFIX.length())
                : rawChatLine;
        cleanedLine = cleanedLine.replaceAll(COLOUR_CODE_REGEX, "").trim();
        LOGGER.log(Level.INFO, "Chat line cleaned: {0}", cleanedLine);
        return cleanedLine;
    }
}