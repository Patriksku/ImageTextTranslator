package org.imagetextapp.utility;

/**
 * Converts language codes to full language names or to other language codes as specified by the different APIs.
 */
public class LanguageCodeMapper {

    /**
     * Converts the DetectLanguage API's language code to its full language.
     * @param languageCode identifier from DetectLanguage API.
     * @return full language for the specified language code.
     */
    public String getFullDetectedLanguage(String languageCode) {
        switch (languageCode) {

            case "hr" -> {
                return "Croatian";
            }

            case "cs" -> {
                return "Czech";
            }

            case "da" -> {
                return "Danish";
            }

            case "nl" -> {
                return "Dutch";
            }

            case "en" -> {
                return "English";
            }

            case "fi" -> {
                return "Finnish";
            }

            case "fr" -> {
                return "French";
            }

            case "de" -> {
                return "German";
            }

            case "hu" -> {
                return "Hungarian";
            }

            case "it" -> {
                return "Italian";
            }

            case "pl" -> {
                return "Polish";
            }

            case "pt" -> {
                return "Portuguese";
            }

            case "sl" -> {
                return "Slovenian";
            }

            case "es" -> {
                return "Spanish";
            }

            case "sv" -> {
                return "Swedish";
            }

            case "tr" -> {
                return "Turkish";
            }

            default -> {
                return "Unsupported language.";
            }
        }
    }

    /**
     * Converts DetectLanguage API's language code to VoiceRSS API's language code.
     * @param detectLanguageCode identifier from DetectLanguage API.
     * @return specified language code as represented by VoiceRSS API.
     */
    public String getDetectLanguageToVoiceCode(String detectLanguageCode) {
        switch (detectLanguageCode) {

            case "hr" -> {
                return "hr-hr";
            }

            case "cs" -> {
                return "cs-cz";
            }

            case "da" -> {
                return "da-dk";
            }

            case "nl" -> {
                // Dutch (Netherlands) as standard language.
                return "nl-nl";
            }

            case "en" -> {
                // English (United States) as standard language.
                return "en-us";
            }

            case "fi" -> {
                return "fi-fi";
            }

            case "fr" -> {
                // French (France) as standard language.
                return "fr-fr";
            }

            case "de" -> {
                // German (Germany) as standard language.
                return "de-de";
            }

            case "hu" -> {
                return "hu-hu";
            }

            case "it" -> {
                return "it-it";
            }

            case "pl" -> {
                return "pl-pl";
            }

            case "pt" -> {
                // Portuguese (Brazil) as standard language.
                return "pt-br";
            }

            case "sl" -> {
                return "sl-si";
            }

            case "es" -> {
                // Spanish (Spain) as standard language.
                return "es-es";
            }

            case "sv" -> {
                return "sv-se";
            }

            case "tr" -> {
                return "tr-tr";
            }

            default -> {
                return "Unsupported Code Conversion.";
            }

        }
    }

    /**
     * Converts DetectLanguage API's language code to Google Translate API's language code.
     * @param detectLanguageCode identifier from DetectLanguage API.
     * @return specified language code as represented by Google Translate API.
     */
    public String getDetectLanguageToTranslateCode(String detectLanguageCode) {
        switch (detectLanguageCode) {

            case "hr" -> {
                return "hr";
            }

            case "cs" -> {
                return "cs";
            }

            case "da" -> {
                return "da";
            }

            case "nl" -> {
                return "nl";
            }

            case "en" -> {
                return "en";
            }

            case "fi" -> {
                return "fi";
            }

            case "fr" -> {
                return "fr";
            }

            case "de" -> {
                return "de";
            }

            case "hu" -> {
                return "hu";
            }

            case "it" -> {
                return "it";
            }

            case "pl" -> {
                return "pl";
            }

            case "pt" -> {
                return "pt";
            }

            case "sl" -> {
                return "sl";
            }

            case "es" -> {
                return "es";
            }

            case "sv" -> {
                return "sv";
            }

            case "tr" -> {
                return "tr";
            }

            default -> {
                return "Unsupported Code Conversion.";
            }
        }
    }
}
