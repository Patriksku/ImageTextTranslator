package org.imagetextapp.apis.detectlanguage;

import org.imagetextapp.utility.ConnectionManager;
import org.imagetextapp.utility.JsonParser;
import org.imagetextapp.utility.StringManager;

import java.net.http.HttpResponse;

/**
 * Identifies the language of a string with the help of the Detect Language API:
 * https://detectlanguage.com/documentation
 */
public class DetectLanguageHandler {

    /**
     *
     * @param parsedText text to be identified for the language it's written in.
     * @return Object representation of JSON response from Detect Language API.
     */
    public DetectLanguageObject identifyLanguage(String parsedText) {
        StringManager stringManager = new StringManager();
        String query = stringManager.getTextURLEncoded(parsedText);

        // Make the DetectLanguage request.
        ConnectionManager connectionManager = new ConnectionManager();
        HttpResponse<String> response = connectionManager.makeDetectLanguageRequest(query);
        System.out.println("StatusCode of DetectLanguage request: " + response.statusCode());
        System.out.println("Body of DetectLanguage Request: " + response.body());

        // Parse response to object.
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parseDetectLanguageResponse(response);
    }
}
