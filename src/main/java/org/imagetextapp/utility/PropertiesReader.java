package org.imagetextapp.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Reads API authorization keys/values from an external secure file.
 * Can also read local strings to support a user's own API credentials:
 *
 *      - Change USER_API_IMPLEMENTATION to TRUE.
 *      - Specify all credentials in the local variables.
 *
 * From: https://itnext.io/how-to-store-passwords-and-api-keys-in-project-code-1eaf5cb235c9
 */
public class PropertiesReader {

    // Set this to true if you want to use your own credentials for the application.
    private static final boolean USER_API_IMPLEMENTATION = false;

    // Fill these with your own credentials if you want to use and/or test this application.
    private static final String OCR_APIKEY = "";
    private static final String DETECT_LANGUAGE_APIKEY = "";
    private static final String X_RAPIDAPI_KEY = "";
    private static final String X_RAPIDAPI_HOST = "";
    private static final String VOICERSS_APIKEY = "";

    /**
     * Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesReader.class);

    /**
     * Constant PROPERTIES.
     */
    private static final Properties PROPERTIES;

    /**
     * Constant PROP_FILE.
     */
    private static final String PROP_FILE = "keys.properties";

    /**
     * Default private constructor PropertiesReader.
     */
    private PropertiesReader() {
    }

    static {
        PROPERTIES = new Properties();
        final URL props = ClassLoader.getSystemResource(PROP_FILE);
        try {
            PROPERTIES.load(props.openStream());
        } catch (IOException ex) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(ex.getClass().getName() + "PropertiesReader method");
            }
        }
    }

    /**
     * Method getProperty.
     *
     * @param name String name file.
     * @return Return property
     */
    public static String getProperty(final String name) {
        if (!USER_API_IMPLEMENTATION) {
            return PROPERTIES.getProperty(name);
        }

        if (name.equals("OCR_APIKEY")) {
            return OCR_APIKEY;
        }
        if (name.equals("DETECT_LANGUAGE_APIKEY")) {
            return DETECT_LANGUAGE_APIKEY;
        }
        if (name.equals("X_RAPIDAPI_KEY")) {
            return X_RAPIDAPI_KEY;
        }
        if (name.equals("X_RAPIDAPI_HOST")) {
            return X_RAPIDAPI_HOST;
        }

        return VOICERSS_APIKEY;
    }
}
