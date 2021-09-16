package org.imagetextapp.apis;

import org.imagetextapp.apis.tools.PropertiesReader;
import org.imagetextapp.apis.tools.StringManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DetectLanguageHandler {

    public void identifyLanguage(String parsedText) {
        StringManager stringManager = new StringManager();

        String query = stringManager.getTextURLEncoded(parsedText);

        HttpResponse<String> response = makeRequest(query);

        System.out.println(response.body());
        System.out.println(response.statusCode());
    }

    private HttpResponse<String> makeRequest(String query) {
        System.out.println("The following query is sent to DetectLanguageAPI: " + query);

        StringManager stringManager = new StringManager();
        String noParamsURL = "https://ws.detectlanguage.com/0.2/detect";

        String DETECT_LANGUAGE_URL = stringManager.getParameterizedURL(noParamsURL, query);
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + PropertiesReader.getProperty("DETECT_LANGUAGE_APIKEY"))
                .uri(URI.create(DETECT_LANGUAGE_URL))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred while trying to identify the language.");
            e.printStackTrace();
        }
        return response;
    }
}
