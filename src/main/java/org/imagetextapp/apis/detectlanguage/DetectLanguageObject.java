package org.imagetextapp.apis.detectlanguage;

/**
 * Object representation of the JSON response from the Detect Language API.
 */
public class DetectLanguageObject {

    private String language = "";
    private boolean isReliable = false;
    private float confidence = 0f;
    private boolean errorOnProcessing = false;
    private DetectLanguageMapper languageMapper = new DetectLanguageMapper();

    public String getLanguage() {
        return languageMapper.getLanguage(language);
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getReliable() {
        if (isReliable) {
            return "High.";
        }
        return "Low.";
    }

    public void setReliable(boolean reliable) {
        isReliable = reliable;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public boolean isErrorOnProcessing() {
        return errorOnProcessing;
    }

    public void setErrorOnProcessing(boolean errorOnProcessing) {
        this.errorOnProcessing = errorOnProcessing;
    }

    @Override
    public String toString() {
        return "DetectLanguageObject{" + '\n' +
                "language='" + language + '\n' +
                ", isReliable=" + isReliable + '\n' +
                ", confidence=" + confidence + '\n' +
                ", errorOnProcessing=" + errorOnProcessing +
                '}';
    }
}
