package org.imagetextapp.apis.detectlanguage;

/**
 * Object representation of the JSON response from the Detect Language API.
 */
public class DetectLanguageObject {

    private String language = "";
    private boolean isReliable = false;
    private float confidence = 0f;
    private boolean errorOnProcessing = false;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isReliable() {
        return isReliable;
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
