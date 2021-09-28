package org.imagetextapp.apis.translate;

/**
 * Object representation of the JSON response from the Google Translate API.
 */
public class TranslateObject {

    private String text = "";
    private String message = "";
    private boolean errorOnProcessing = false;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isErrorOnProcessing() {
        return errorOnProcessing;
    }

    public void setErrorOnProcessing(boolean errorOnProcessing) {
        this.errorOnProcessing = errorOnProcessing;
    }
}
