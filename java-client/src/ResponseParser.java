// File: ResponseParser.java
/**
 * Very small JSON extractor for the backend responses we expect.
 * We avoid external JSON libraries: this does simple, robust string extraction.
 *
 * Expected backend format (example):
 * {"status":"success","message":"Based on the symptoms..."}
 */
public class ResponseParser {

    /**
     * Extracts the "message" value from a JSON string. If message not found,
     * returns the original string as a fallback.
     */
    public static String parseMessage(String json) {
        if (json == null || json.isEmpty()) return "Empty response from backend";
        String key = "\"message\"";
        int i = json.indexOf(key);
        if (i == -1) return fallbackClean(json);

        int colon = json.indexOf(":", i + key.length());
        if (colon == -1) return fallbackClean(json);

        int start = findNextQuote(json, colon + 1);
        if (start == -1) return fallbackClean(json);

        int end = findClosingQuote(json, start + 1);
        if (end == -1) end = json.length();

        String raw = json.substring(start + 1, end);
        return unescape(raw);
    }

    /**
     * Extracts "status" if present (e.g., "success" / "error"). Returns null if not known.
     */
    public static String parseStatus(String json) {
        if (json == null || json.isEmpty()) return null;
        String key = "\"status\"";
        int i = json.indexOf(key);
        if (i == -1) return null;
        int colon = json.indexOf(":", i + key.length());
        if (colon == -1) return null;
        int start = findNextQuote(json, colon + 1);
        if (start == -1) return null;
        int end = findClosingQuote(json, start + 1);
        if (end == -1) end = json.length();
        return unescape(json.substring(start + 1, end));
    }

    private static int findNextQuote(String s, int from) {
        for (int i = from; i < s.length(); i++) {
            if (s.charAt(i) == '"') return i;
        }
        return -1;
    }

    private static int findClosingQuote(String s, int from) {
        for (int i = from; i < s.length(); i++) {
            if (s.charAt(i) == '"' && s.charAt(i - 1) != '\\') return i;
        }
        return -1;
    }

    // Replace common escape sequences
    private static String unescape(String s) {
        return s.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    // Fallback: remove outer braces to produce readable output
    private static String fallbackClean(String json) {
        String trimmed = json.trim();
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
        }
        return trimmed;
    }
}
