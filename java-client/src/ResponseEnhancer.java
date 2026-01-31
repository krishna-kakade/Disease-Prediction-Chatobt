// File: ResponseEnhancer.java
/**
 * Small utilities that clean and improve backend text for display.
 */
public class ResponseEnhancer {

    /**
     * Trim, shorten overly long AI responses, and ensure they end with punctuation.
     */
    public static String enhance(String raw) {
        if (raw == null) return "";
        String s = raw.trim();

        // Remove excessive whitespace
        s = s.replaceAll("\\s{2,}", " ");

        // Limit length (e.g., 800 chars) but preserve sentences
        if (s.length() > 800) {
            int cut = s.lastIndexOf('.', 750);
            if (cut == -1) cut = Math.min(800, s.length());
            s = s.substring(0, cut).trim();
            if (!s.endsWith(".")) s += "...";
        }

        // Ensure ends with punctuation
        if (!s.endsWith(".") && !s.endsWith("!") && !s.endsWith("?")) s += ".";

        return s;
    }
}
