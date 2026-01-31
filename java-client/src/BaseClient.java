// File: BaseClient.java
public abstract class BaseClient {
    protected String apiUrl;

    public BaseClient(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    // Every client must implement this method
    public abstract String sendSymptoms(String symptoms) throws Exception;
}
