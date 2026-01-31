// File: MockClient.java
public class MockClient extends BaseClient {

    public MockClient() {
        super("mock");
    }

    @Override
    public String sendSymptoms(String symptoms) {
        // Simulated, structured JSON response for testing and demos
        String safe = symptoms == null ? "" : symptoms.replace("\"", "\\\"");
        String message = "(Mock) Based on the symptoms \"" + safe + "\", the likely possibilities are: \n"
                + "- Viral infection (e.g., common cold / flu)\n"
                + "- Dehydration or food-related illness\n"
                + "Advice: rest, hydrate, and see a doctor if symptoms worsen.";
        return "{\"status\":\"success\",\"message\":\"" + message.replace("\n", "\\n").replace("\"", "\\\"") + "\"}";
    }
}
