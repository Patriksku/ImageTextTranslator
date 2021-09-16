package org.imagetextapp.apis.ocr.beans;

public class OCRObject {

    private int textOrientation = 0;
    private int fileParseExitCode = 1;
    private String parsedText = "";
    private String parsedTextClean = "";
    private String errorMessage = "";
    private String errorDetails = "";

    private int ocrExitCode = 1;
    private boolean errorOnProcessing = false;
    private int processingTime = 0;

    public int getTextOrientation() {
        return textOrientation;
    }

    public void setTextOrientation(int textOrientation) {
        this.textOrientation = textOrientation;
    }

    public int getFileParseExitCode() {
        return fileParseExitCode;
    }

    public void setFileParseExitCode(int fileParseExitCode) {
        this.fileParseExitCode = fileParseExitCode;
    }

    public String getParsedText() {
        return parsedText;
    }

    public void setParsedText(String parsedText) {
        this.parsedText = parsedText;
    }

    public String getParsedTextClean() {
        return parsedTextClean;
    }

    public void setParsedTextClean(String parsedTextClean) {
        this.parsedTextClean = parsedTextClean;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public int getOcrExitCode() {
        return ocrExitCode;
    }

    public void setOcrExitCode(int ocrExitCode) {
        this.ocrExitCode = ocrExitCode;
    }

    public boolean isErrorOnProcessing() {
        return errorOnProcessing;
    }

    public void setErrorOnProcessing(boolean errorOnProcessing) {
        this.errorOnProcessing = errorOnProcessing;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    @Override
    public String toString() {
        return "OCRObject{" +
                "textOrientation=" + textOrientation + '\n' +
                ", fileParseExitCode=" + fileParseExitCode + '\n' +
                ", parsedText='" + parsedText + '\n' +
                ", parsedTextClean='" + parsedTextClean + '\n' +
                ", errorMessage='" + errorMessage + '\n' +
                ", errorDetails='" + errorDetails + '\n' +
                ", ocrExitCode=" + ocrExitCode + '\n' +
                ", errorOnProcessing=" + errorOnProcessing + '\n' +
                ", processingTime=" + processingTime + '\n' +
                '}';
    }
}
