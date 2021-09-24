package org.imagetextapp.apis.detectlanguage;

public class DetectLanguageMapper {


    public String getLanguage(String languageCode) {
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
}
