package org.imagetextapp.gui;

import org.imagetextapp.apis.ocr.OCRObject;
import org.imagetextapp.apis.translate.TranslateObject;

import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Responsible for storing data that is generated by application.
 * Used by the controller for data manipulation.
 */
public class Model {

    private String userFilePathInput = "";
    private String userMarkedTextAreaOpt = null;
    private String retainLayoutOpt = "";
    private String standardLayoutOpt = "";
    private final String createdByText = "Created by: @Patriksku";

    private File selectedFile;
    private OCRObject ocrObject;
    private TranslateObject translateObject;
    private Clip AUDIO_CLIP;

    private boolean TRANSLATED = false;

    public String getUserFilePathInput() {
        return userFilePathInput;
    }

    public void setUserFilePathInput(String userFilePathInput) {
        this.userFilePathInput = userFilePathInput;
    }

    public String getUserMarkedTextAreaOpt() {
        return userMarkedTextAreaOpt;
    }

    public void setUserMarkedTextAreaOpt(String userMarkedTextAreaOpt) {
        this.userMarkedTextAreaOpt = userMarkedTextAreaOpt;
    }

    public String getRetainLayoutOpt() {
        return retainLayoutOpt;
    }

    public void setRetainLayoutOpt(String retainLayoutOpt) {
        this.retainLayoutOpt = retainLayoutOpt;
    }

    public String getStandardLayoutOpt() {
        return standardLayoutOpt;
    }

    public void setStandardLayoutOpt(String standardLayoutOpt) {
        this.standardLayoutOpt = standardLayoutOpt;
    }

    public String getCreatedByText() {
        return createdByText;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public OCRObject getOcrObject() {
        return ocrObject;
    }

    public void setOcrObject(OCRObject ocrObject) {
        this.ocrObject = ocrObject;
    }

    public TranslateObject getTranslateObject() {
        return translateObject;
    }

    public void setTranslateObject(TranslateObject translateObject) {
        this.translateObject = translateObject;
    }

    public Clip getAUDIO_CLIP() {
        return AUDIO_CLIP;
    }

    public void setAUDIO_CLIP(Clip AUDIO_CLIP) {
        this.AUDIO_CLIP = AUDIO_CLIP;
    }

    public boolean isTRANSLATED() {
        return TRANSLATED;
    }

    public void setTRANSLATED(boolean TRANSLATED) {
        this.TRANSLATED = TRANSLATED;
    }

    public int getFILE_SIZE_LIMIT() {
        return 1024;
    }

}
