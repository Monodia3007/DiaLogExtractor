package eu.lilithmonodia.dialogextractor.data;

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

    private static final Pattern DIALOG_PATTERN = Pattern.compile("^\\[\\d*:\\d*:\\d*] \\[Render thread/INFO]: \\[System] \\[CHAT].*$", Pattern.MULTILINE);
    private static final Pattern CHAT_PATTERN = Pattern.compile("\\[\\d*:\\d*:\\d*] \\[Render thread/INFO]: \\[System] \\[CHAT]");

    /**
     * Extracts the dialog from the Minecraft log.
     *
     * @return The extracted dialog as a MinecraftLog object.
     */
    @Contract(" -> new")
    public @NotNull MinecraftLog extractDialog() {
        List<String> extractedDialogs = new ArrayList<>();
        cleanAndExtractDialog(extractedDialogs);
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
            dialogList.add(cleanChatLine(matcher.group()));
        }
    }

    /**
     * Removes unwanted characters from a chat line based on a given pattern.
     *
     * @param rawChatLine The raw chat line to be cleaned.
     *
     * @return The cleaned chat line.
     */
    private @NotNull String cleanChatLine(String rawChatLine) {
        return MinecraftLog.CHAT_PATTERN.matcher(rawChatLine)
                .replaceAll("")
                .replaceAll(COLOUR_CODE_REGEX, "")
                .trim();
    }
}