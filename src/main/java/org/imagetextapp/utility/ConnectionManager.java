package org.imagetextapp.utility;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Makes the connections for the various APIs that are used.
 */
public class ConnectionManager {

    /**
     *
     * @param multiPartBody multipart/form-data encoding for the file request to OCR API.
     * @return response from the OCR API.
     */
    public HttpResponse<String> makeOCRRequest(MultiPartBody multiPartBody) {
        String OCR_URL = "https://api.ocr.space/parse/image";
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "multipart/form-data; boundary=" + multiPartBody.getBoundary())
                .header("apikey", PropertiesReader.getProperty("OCR_APIKEY"))
                .uri(URI.create(OCR_URL))
                .POST(multiPartBody.build())
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred while trying to make an OCR request.");
            e.printStackTrace();
        }
        return response;
    }

    /**
     *
     * @param query text to be identified by Detect Language API in the query.
     * @return response from the Detect Language API.
     */
    public HttpResponse<String> makeDetectLanguageRequest(String query) {
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
            System.out.println("An error occurred while trying to make a DetectLanguage request.");
            e.printStackTrace();
        }
        return response;
    }

    /**
     *
     * @param query text together with required fields for the Translate API.
     * @return response from the Translate API.
     */
    public HttpResponse<String> makeTranslateRequest(String query) {
        final String TRANSLATE_URL = "https://google-translate1.p.rapidapi.com/language/translate/v2";

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept-Encoding", "application/gzip")
                .header("x-rapidapi-key", PropertiesReader.getProperty("X-RAPIDAPI-KEY"))
                .header("x-rapidapi-host", PropertiesReader.getProperty("X-RAPIDAPI-HOST"))
                .header("useQueryString", "true")
                .uri(URI.create(TRANSLATE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(query))
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred while trying to make an OCR request.");
            e.printStackTrace();
        }
        return response;
    }
}