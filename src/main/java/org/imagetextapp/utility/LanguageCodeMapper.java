package org.imagetextapp.utility;

/**
 * Converts language codes to full language names or to other language codes as specified by APIs.
 */
public class LanguageCodeMapper {

    /**
     *
     * @param languageCode identifier from DetectLanguage API.
     * @return full language for the specified language code.
     */
    public String getFullDetectedLanguage(String languageCode) {
        switch (languageCode) {

            case "ar" -> {
                return "Arabic";
            }

            case "bg" -> {
                return "Bulgarian";
            }

            case "zh" -> {
                return "Chinese Simplified";
            }

            case "zh-Hant" -> {
                return "Chinese Traditional";
            }

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

            case "el" -> {
                return "Greek";
            }

            case "hu" -> {
                return "Hungarian";
            }

            case "ko" -> {
                return "Korean";
            }

            case "it" -> {
                return "Italian";
            }

            case "ja" -> {
                return "Japanese";
            }

            case "pl" -> {
                return "Polish";
            }

            case "pt" -> {
                return "Portuguese";
            }

            case "ru" -> {
                return "Russian";
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
     *
     * @param detectLanguageCode identifier from DetectLanguage API.
     * @return specified language code as represented by VoiceRSS API.
     */
    public String getDetectLanguageToVoiceCode(String detectLanguageCode) {
        switch (detectLanguageCode) {

            case "ar" -> {
                // Arabic (Saudi Arabia) as standard language.
                return "ar-sa";
            }

            case "bg" -> {
                return "bg-bg";
            }

            case "zh" -> {
                // Chinese (China) as standard "Simplified" Chinese.
                return "zh-cn";
            }

            case "zh-Hant" -> {
                // Chinese (Taiwan) as standard "Traditional" Chinese.
                return "zh-tw";
            }

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

            case "el" -> {
                return "el-gr";
            }

            case "hu" -> {
                return "hu-hu";
            }

            case "ko" -> {
                return "ko-kr";
            }

            case "it" -> {
                return "it-it";
            }

            case "ja" -> {
                return "ja-jp";
            }

            case "pl" -> {
                return "pl-pl";
            }

            case "pt" -> {
                // Portuguese (Brazil) as standard language.
                return "pt-br";
            }

            case "ru" -> {
                return "ru-ru";
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
     *
     * @param detectLanguageCode identifier from DetectLanguage API.
     * @return specified language code as represented by Google Translate API.
     */
    public String getDetectLanguageToTranslateCode(String detectLanguageCode) {
        switch (detectLanguageCode) {

            case "ar" -> {
                return "ar";
            }

            case "bg" -> {
                return "bg";
            }

            case "zh" -> {
                return "zh-CN";
            }

            case "zh-Hant" -> {
                return "zh-TW";
            }

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

            case "el" -> {
                return "el";
            }

            case "hu" -> {
                return "hu";
            }

            case "ko" -> {
                return "ko";
            }

            case "it" -> {
                return "it";
            }

            case "ja" -> {
                return "ja";
            }

            case "pl" -> {
                return "pl";
            }

            case "pt" -> {
                return "pt";
            }

            case "ru" -> {
                return "ru";
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
