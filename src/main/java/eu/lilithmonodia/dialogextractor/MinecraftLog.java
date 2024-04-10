package eu.lilithmonodia.dialogextractor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Minecraft log.
 */
public record MinecraftLog(String log) {

    public static final String COLOUR_CODE_REGEX = "§.";

    /**
     * Extracts the dialog from the Minecraft log.
     *
     * @return The extracted dialog as a MinecraftLog object.
     */
    @Contract(" -> new")
    public @NotNull MinecraftLog extractDialog() {

        Pattern dialogPattern = Pattern.compile("^\\[\\d*:\\d*:\\d*] \\[Render thread/INFO]: \\[System] \\[CHAT].*$", Pattern.MULTILINE);
        Pattern chatPattern = Pattern.compile("\\[\\d*:\\d*:\\d*] \\[Render thread/INFO]: \\[System] \\[CHAT]");

        List<String> extractedDialogs = new ArrayList<>();
        cleanAndExtractDialog(chatPattern, dialogPattern, extractedDialogs);
        return new MinecraftLog(String.join("\n", extractedDialogs));
    }

    /**
     * Removes unwanted characters and extracts dialog from the Minecraft log.
     *
     * @param chatPattern   The pattern used to match the chat line in the log.
     * @param dialogPattern The pattern used to match the dialog in the log.
     * @param dialogList    The list to store the extracted dialog lines.
     */
    private void cleanAndExtractDialog(Pattern chatPattern, @NotNull Pattern dialogPattern, List<String> dialogList) {
        Matcher matcher = dialogPattern.matcher(this.log);
        while (matcher.find()) {
            dialogList.add(cleanChatLine(chatPattern, matcher.group()));
        }
    }

    /**
     * Removes unwanted characters from a chat line based on a given pattern.
     *
     * @param chatPattern The pattern used to match the chat line in the log.
     * @param rawChatLine The raw chat line to be cleaned.
     *
     * @return The cleaned chat line.
     */
    private @NotNull String cleanChatLine(@NotNull Pattern chatPattern, String rawChatLine) {
        return chatPattern.matcher(rawChatLine)
                .replaceAll("")
                .replaceAll(COLOUR_CODE_REGEX, "")
                .trim();
    }
}