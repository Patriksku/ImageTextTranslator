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

    private DetectLanguageObject detectLanguageObject = new DetectLanguageObject();

    /**
     *
     * @param parsedText text to be identified for the language it's written in.
     */
    public void identifyLanguage(String parsedText) {
        StringManager stringManager = new StringManager();
        String query = stringManager.getTextURLEncoded(parsedText);

        // Make the DetectLanguage request.
        ConnectionManager connectionManager = new ConnectionManager();
        HttpResponse<String> response = connectionManager.makeDetectLanguageRequest(query);
        System.out.println("StatusCode of DetectLanguage request: " + response.statusCode());
        System.out.println("Body of DetectLanguage Request: " + response.body());

        // Parse response to object.
        JsonParser jsonParser = new JsonParser();
        this.detectLanguageObject = jsonParser.parseDetectLanguageResponse(response);

        System.out.println("---");
        System.out.println(detectLanguageObject.toString());
    }

    public DetectLanguageObject getDetectLanguageObject() {
        if (detectLanguageObject.isErrorOnProcessing()) {
            System.out.println("Returning object from last errored Detect Language connection.");
            return this.detectLanguageObject;
        }
        return this.detectLanguageObject;
    }
}
