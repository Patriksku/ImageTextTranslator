package org.imagetextapp.gui;

public class TextGenerationBoxItem {

    private String key;
    private String value;

    public TextGenerationBoxItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
