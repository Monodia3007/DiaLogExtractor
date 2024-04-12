package eu.lilithmonodia.dialogextractor.data;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static eu.lilithmonodia.dialogextractor.utils.LogUtils.logAction;

/**
 * Represents a Minecraft log, which contains chat messages and other events.
 */
public record MinecraftLog(String log) {

    /**
     * The regular expression pattern used to match and remove color codes in Minecraft chat messages.
     */
    public static final String COLOUR_CODE_REGEX = "§.";
    private static final Logger LOGGER = Logger.getLogger(MinecraftLog.class.getName());
    private static final String DIALOG_PREFIX = "[CHAT]";

    /**
     * Extracts dialogues from the Minecraft log by cleaning and removing color codes.
     *
     * @return A {@link MinecraftLog} object containing the extracted dialogues.
     */
    @NotNull
    public MinecraftLog extractDialogue() {
        logAction(LOGGER, "Extracting dialogue from Minecraft log...");

        List<String> extractedDialogs = cleanAndExtractDialogue();

        logAction(LOGGER, "Dialogue extraction completed successfully.");
        return new MinecraftLog(String.join("\n", extractedDialogs));
    }

    /**
     * Cleans and extracts dialogues from the Minecraft log.
     *
     * @return A list of cleaned dialogues without the dialogue prefix and color codes.
     */
    private List<String> cleanAndExtractDialogue() {
        logAction(LOGGER, "Cleaning and extracting dialogues from Minecraft log...");

        List<String> extractedDialogs = Arrays.stream(log.split("\n"))
                .filter(line -> line.contains(DIALOG_PREFIX))
                .map(this::cleanChatLine)
                .filter(cleanedLine -> !"Shaders Reloaded!".equals(cleanedLine))
                .toList();

        logAction(LOGGER, "Dialogues cleaned and extracted successfully.");

        return extractedDialogs;
    }

    /**
     * Cleans a chat line by removing the dialogue prefix and color codes.
     *
     * @param rawChatLine The raw chat line to be cleaned.
     *
     * @return The cleaned chat line without the dialogue prefix and color codes.
     */
    private @NotNull String cleanChatLine(@NotNull String rawChatLine) {
        int chatIndex = rawChatLine.indexOf(DIALOG_PREFIX);
        String cleanedLine = (chatIndex != -1)
                ? rawChatLine.substring(chatIndex + DIALOG_PREFIX.length())
                : rawChatLine;

        cleanedLine = cleanedLine.replaceAll(COLOUR_CODE_REGEX, "").trim();

        return cleanedLine;
    }
}