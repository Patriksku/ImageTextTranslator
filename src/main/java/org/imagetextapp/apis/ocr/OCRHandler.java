package org.imagetextapp.apis.ocr;

import org.imagetextapp.PropertiesReader;
import org.imagetextapp.apis.ocr.beans.OCRObject;
import org.imagetextapp.apis.tools.MultiPartBody;
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

        if (response != null && response.statusCode() == 200) {
            parseJsonToObject(response);

        } else {
            System.out.println("Something went wrong while trying to process the file. Make sure it " +
                    "is an image!");
        }
    }

    private void parseJsonToObject(HttpResponse<String> response) {
        OCRObject jsonToOCR = new OCRObject();

        String jsonString = response.body();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("ParsedResults");

        System.out.println("PRE LOOP");
        System.out.println("TextOrientation: " + jsonArray.getJSONObject(0).getString("TextOrientation"));
        System.out.println("FileParseExitCode: " + jsonArray.getJSONObject(0).getInt("FileParseExitCode"));
        System.out.println("-------------");

        for (int i = 0; i < jsonArray.length(); i++) {
            String item = jsonArray.getJSONObject(i).toString();
            System.out.println("SIZE: " + jsonArray.length());
            System.out.println("ITEM: " + item);
            System.out.println("-------");
            System.out.println("TextOrientation: " + jsonArray.getJSONObject(i).getString("TextOrientation"));
            System.out.println("FileParseExitCode: " + jsonArray.getJSONObject(i).getInt("FileParseExitCode"));
//            System.out.println("ParsedText: " + jsonArray.getJSONObject(i).getString("ParsedText"));
            if (item.equals("TextOrientation")) {
                System.out.println("hej");
            }
        }
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
