package org.imagetextapp.apis.ocr;

import org.imagetextapp.PropertiesReader;
import org.imagetextapp.apis.ocr.beans.OCRObject;
import org.imagetextapp.apis.tools.MultiPartBody;
import org.imagetextapp.apis.tools.StringManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class OCRHandler {

    private OCRObject ocrObject;

    public void uploadLocalImage(Path localFile, String language) {
        MultiPartBody multiPartBody = new MultiPartBody()
                .addPart("file", localFile)
                .addPart("language", language)
                .addPart("detectOrientation", "true")
                .addPart("scale", "true");

        HttpResponse<String> response = makeRequest(multiPartBody);

        System.out.println(response.body());

        if (response.statusCode() == 200) {
            parseJsonToObject(response);

        } else {
            System.out.println("Something went wrong while trying to process the file. Make sure it " +
                    "is an image!");
        }
    }

    public void uploadURLImage(String urlFile, String language) {
        MultiPartBody multiPartBody = new MultiPartBody()
                .addPart("url", urlFile)
                .addPart("language", language)
                .addPart("detectOrientation", "true")
                .addPart("scale", "true");

        HttpResponse<String> response = makeRequest(multiPartBody);

        System.out.println(response.body());

        if (response.statusCode() == 200) {
            parseJsonToObject(response);

        } else {
            System.out.println("Something went wrong while trying to process the file. Make sure it " +
                    "is an image!");
        }
    }

    private void parseJsonToObject(HttpResponse<String> response) {
        StringManager stringManager = new StringManager();
        OCRObject jsonToOCR = new OCRObject();

        String jsonString = response.body();
        JSONObject parsedResObject = new JSONObject(jsonString);

        // If server indicates that an error occurred, print errorMessage.
        if (parsedResObject.getBoolean("IsErroredOnProcessing")) {
            System.out.println("An error occurred while trying to proccess your request. Description: ");

            JSONArray errorArray = parsedResObject.getJSONArray("ErrorMessage");
            System.out.println(errorArray.getString(0));
        }
        // Else proceed as normal.
        else {
            JSONArray parsedResArray = parsedResObject.getJSONArray("ParsedResults");

            // Set values from returned "ParsedResults" array from response.
            jsonToOCR.setTextOrientation(parsedResArray.getJSONObject(0).getInt("TextOrientation"));
            jsonToOCR.setFileParseExitCode(parsedResArray.getJSONObject(0).getInt("FileParseExitCode"));
            jsonToOCR.setParsedText(parsedResArray.getJSONObject(0).getString("ParsedText"));
            jsonToOCR.setParsedTextClean(stringManager.getCleanString(jsonToOCR.getParsedText()));
            jsonToOCR.setErrorMessage(parsedResArray.getJSONObject(0).getString("ErrorMessage"));
            jsonToOCR.setErrorDetails(parsedResArray.getJSONObject(0).getString("ErrorDetails"));

            // Set values from returned parameters from response body.
            jsonToOCR.setOcrExitCode(parsedResObject.getInt("OCRExitCode"));
            jsonToOCR.setErrorOnProcessing(parsedResObject.getBoolean("IsErroredOnProcessing"));
            jsonToOCR.setProcessingTime(parsedResObject.getInt("ProcessingTimeInMilliseconds"));

            this.ocrObject = jsonToOCR;

            // Contents of ocrObject
            System.out.println(this.ocrObject);
        }
    }

    public OCRObject getOcrObject() {
        if (ocrObject != null) {
            return this.ocrObject;
        }
        System.out.println("ocrObject is null (not instantiated) but was returned anyways.");
        return this.ocrObject;
    }

    private HttpResponse<String> makeRequest(MultiPartBody multiPartBody) {
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
            System.out.println("An error occurred while trying to generate text for this image.");
            e.printStackTrace();
        }
        return response;
    }
}
