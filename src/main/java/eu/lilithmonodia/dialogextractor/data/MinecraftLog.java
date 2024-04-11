package eu.lilithmonodia.dialogextractor.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern DIALOG_PATTERN = Pattern.compile(".*\\[CHAT].*$", Pattern.MULTILINE);
    private static final Pattern CHAT_PATTERN = Pattern.compile(".*\\[CHAT] ");

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
        Matcher matcher = MinecraftLog.DIALOG_PATTERN.matcher(this.log);
        while (matcher.find()) {
            String cleanedLine = cleanChatLine(matcher.group());

            if ("Shaders Reloaded!".equals(cleanedLine)) {
                continue;
            }

            dialogList.add(cleanedLine);
        }

        LOGGER.log(Level.INFO, "Extracted dialog from Minecraft log -> dialog: [{0}]", dialogList);
    }

    /**
     * Removes unwanted characters from a chat line based on a given pattern.
     *
     * @param rawChatLine The raw chat line to be cleaned.
     *
     * @return The cleaned chat line.
     */
    private @NotNull String cleanChatLine(String rawChatLine) {
        String cleanedLine = MinecraftLog.CHAT_PATTERN.matcher(rawChatLine)
                .replaceAll("")
                .replaceAll(COLOUR_CODE_REGEX, "")
                .trim();

        LOGGER.log(Level.INFO, "Cleaned chat line -> rawChatLine: [{0}], cleanedLine: [{1}]", new Object[]{rawChatLine, cleanedLine});
        return cleanedLine;
    }
}