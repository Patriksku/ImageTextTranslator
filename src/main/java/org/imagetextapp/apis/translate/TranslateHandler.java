package org.imagetextapp.apis.translate;

import org.imagetextapp.utility.ConnectionManager;
import org.imagetextapp.utility.JsonParser;
import org.imagetextapp.utility.StringManager;

import java.net.http.HttpResponse;

/**
 * Translates a string to a target language, with the help of Google Translate API:
 * https://english.api.rakuten.net/googlecloud/api/google-translate1/endpoints
 */
public class TranslateHandler {

    /**
     *
     * @param toTranslate text to be translated
     * @param target language
     * @param source language (optional)
     * @return Object representation of JSON response from Google Translate API.
     */
    public String translateText(String toTranslate, String target, String source) {
        StringManager stringManager = new StringManager();
        String query = stringManager.getAppXForm(toTranslate, target, source);

        System.out.println("Translate query: ");
        System.out.println(query);

        // Make the Translate request.
        ConnectionManager connectionManager = new ConnectionManager();
        HttpResponse<String> response = connectionManager.makeTranslateRequest(query);
        System.out.println("StatusCode of Translate request: " + response.statusCode());
        System.out.println("Body of Translate Request: " + response.body());

        // Parse response (translated text) to String.
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parseTranslateResponse(response);
    }

    /**
     *
     * @param toTranslate text to be translated
     * @param target language
     * @return Object representation of JSON response from Google Translate API.
     */
    public String translateText(String toTranslate, String target) {
        StringManager stringManager = new StringManager();
        String query = stringManager.getAppXForm(toTranslate, target);

        System.out.println("Translate query: ");
        System.out.println(query);

        // Make the Translate request.
        ConnectionManager connectionManager = new ConnectionManager();
        HttpResponse<String> response = connectionManager.makeTranslateRequest(query);
        System.out.println("StatusCode of Translate request: " + response.statusCode());
        System.out.println("Body of Translate Request: " + response.body());

        // Parse response (translated text) to String.
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parseTranslateResponse(response);
    }
}
