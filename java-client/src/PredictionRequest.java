public class PredictionRequest {
    private String symptoms;
    public PredictionRequest(String symptoms) { this.symptoms = symptoms; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String s){ this.symptoms = s; }
}