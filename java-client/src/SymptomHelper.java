// File: SymptomHelper.java
import java.util.*;

/**
 * Utilities for symptom normalization, suggestions and simple validation.
 */
public class SymptomHelper {
    private static final Set<String> KNOWN_SYMPTOMS = new HashSet<>(Arrays.asList(
            "fever", "cough", "headache", "nausea", "vomiting", "diarrhea",
            "fatigue", "chills", "sore throat", "shortness of breath",
            "chest pain", "dizziness", "rash", "itching", "weight loss"
    ));

    /**
     * Normalize raw input: lowercase, trim, collapse spaces.
     */
    public static String normalize(String raw) {
        if (raw == null) return "";
        return raw.trim().replaceAll("\\s*,\\s*", ", ").replaceAll("\\s+", " ").toLowerCase();
    }

    /**
     * Splits the normalized string into symptom tokens.
     */
    public static List<String> splitSymptoms(String normalized) {
        if (normalized == null || normalized.isEmpty()) return Collections.emptyList();
        String[] parts = normalized.split("\\s*,\\s*");
        List<String> out = new ArrayList<>();
        for (String p : parts) {
            String s = p.trim();
            if (!s.isEmpty()) out.add(s);
        }
        return out;
    }

    /**
     * Suggest known symptoms that are close to the provided token using Levenshtein distance.
     */
    public static List<String> suggest(String token, int maxSuggestions) {
        if (token == null || token.isEmpty()) return Collections.emptyList();
        TreeMap<Integer, List<String>> buckets = new TreeMap<>();
        for (String known : KNOWN_SYMPTOMS) {
            int d = levenshtein(token.toLowerCase(), known.toLowerCase());
            buckets.computeIfAbsent(d, k -> new ArrayList<>()).add(known);
        }
        List<String> suggestions = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> e : buckets.entrySet()) {
            for (String s : e.getValue()) {
                suggestions.add(s);
                if (suggestions.size() >= maxSuggestions) return suggestions;
            }
        }
        return suggestions;
    }

    // Classic Levenshtein distance
    private static int levenshtein(String a, String b) {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int prev = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cur = costs[j];
                int add = costs[j - 1] + 1;
                int del = costs[j] + 1;
                int sub = prev + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1);
                int newv = Math.min(Math.min(add, del), sub);
                prev = cur;
                costs[j] = newv;
            }
        }
        return costs[b.length()];
    }
}
