package eu.lilithmonodia.dialogextractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftLog {
    private String log;

    public MinecraftLog(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public MinecraftLog extractDialog() {
        List<String> extractedDialogs = new ArrayList<>();
        Pattern pattern = Pattern.compile("^\\[\\d{2}:\\d{2}:\\d{2}] \\[Render thread/INFO]: \\[System] \\[CHAT].*$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(this.log);
        while (matcher.find()) {
            String extractedDialog = matcher.group();
            Pattern chatPattern = Pattern.compile("\\[\\d{2}:\\d{2}:\\d{2}] \\[Render thread/INFO]: \\[System] \\[CHAT]");
            Matcher chatMatcher = chatPattern.matcher(extractedDialog);
            String cleanChat = chatMatcher.replaceAll("");
            cleanChat = cleanChat.replaceAll("§.", ""); // Removes "§" and the following character
            extractedDialogs.add(cleanChat.trim());
        }
        return new MinecraftLog(String.join("\n", extractedDialogs));
    }
}