// File: ChatLogger.java
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple logger that appends chat messages to a local file.
 */
public class ChatLogger {
    private static final String FILE_NAME = "chat_history.txt";
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Append a single log entry with the user (You/Bot/System) and message text.
     */
    public static void log(String who, String message) {
        String time = LocalDateTime.now().format(fmt);
        String line = String.format("[%s] %s: %s%n", time, who, message.replace("\n", " | "));
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(line);
        } catch (IOException e) {
            // For the UI, we don't throw — just print to console
            System.err.println("ChatLogger error: " + e.getMessage());
        }
    }
}
