package org.imagetextapp.gui;

/**
 * Object representation of a JComboBox item with key-value mapping.
 */
public class BoxItem {

    private final String key;
    private final String value;

    public BoxItem(String key, String value) {
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
