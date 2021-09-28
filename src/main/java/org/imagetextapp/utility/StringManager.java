package org.imagetextapp.utility;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Performs various operations on strings.
 */
public class StringManager {

    /**
     *
     * @param toClean string.
     * @return clean version without special characters (new lines).
     */
    public String getCleanString(String toClean) {
        return toClean.replace("\r\n", " ").replace("\n", " ");
    }

    /**
     *
     * @param toClean string.
     * @return clean version without new rows.
     */
    public String removeExtraNewLines(String toClean) {
        return toClean.replace("\n", "");
    }

    /**
     *
     * @param toEncode string.
     * @return url encoded string.
     */
    public String getTextURLEncoded(String toEncode) {
        return URLEncoder.encode(toEncode, StandardCharsets.UTF_8);
    }

    /**
     *
     * @param url to be parameterized.
     * @param query value.
     * @return parameterized url.
     */
    public String getDetectLanguageParameterizedURL(String url, String query) {
        return url + "?q=" + query;
    }

    /**
     *
     * @param toTranslate string.
     * @param target language.
     * @param source language.
     * @return string in required "application/x-www-form-urlencoded" with correct format.
     */
    public String getAppXForm(String toTranslate, String target, String source) {
        return "text=" + toTranslate + "&" +
                "tl=" + target + "&" +
                "sl=" + source;
    }

    /**
     *
     * @param toTranslate string.
     * @param target language.
     * @return string in required "application/x-www-form-urlencoded" with correct format.
     */
    public String getAppXForm(String toTranslate, String target) {
        return "text=" + toTranslate + "&" +
                "tl=" + target;
    }
}
