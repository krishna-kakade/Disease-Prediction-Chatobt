import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

// File: ApiClient.java
public class ApiClient extends BaseClient {

    public ApiClient() {
        super("http://127.0.0.1:5000/api/predict"); // set API URL
    }

    @Override
    public String sendSymptoms(String symptoms) throws IOException {
        // Use URI first, then convert to URL
        URI uri;
        try {
            uri = new URI(apiUrl);
        } catch (Exception e) {
            throw new IOException("Invalid backend URL", e);
        }
        URL url = uri.toURL();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; utf-8");

        String payload = "{\"symptoms\": " + toJsonString(symptoms) + "}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder resp = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                resp.append(line);
            }
            return resp.toString();
        } finally {
            conn.disconnect();
        }
    }

    // Simple helper to escape JSON string
    private static String toJsonString(String s) {
        String escaped = s.replace("\\", "\\\\")
                          .replace("\"", "\\\"")
                          .replace("\n", "\\n")
                          .replace("\r", "\\r");
        return "\"" + escaped + "\"";
    }
}
