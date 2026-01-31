// File: HistoryManager.java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads last N lines from chat_history.txt to display at startup.
 */
public class HistoryManager {
    private static final String FILE_NAME = "chat_history.txt";

    /**
     * Returns last `maxLines` from the history file. If file doesn't exist, returns empty list.
     */
    public static List<String> loadLast(int maxLines) {
        File f = new File(FILE_NAME);
        List<String> lines = new ArrayList<>();
        if (!f.exists()) return lines;

        // Read all lines then return last N
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("HistoryManager load error: " + e.getMessage());
        }

        int start = Math.max(0, lines.size() - maxLines);
        return new ArrayList<>(lines.subList(start, lines.size()));
    }
}
