package org.imagetextapp.apis.ocr;

import org.imagetextapp.utility.*;

import java.net.http.HttpResponse;
import java.nio.file.Path;

/**
 * Generates any text that exists in the source image. Uses the OCR Api:
 * https://ocr.space/ocrapi
 */
public class OCRHandler {

    /**
     *
     * @param localFile Path to a local file (image).
     * @param language The language of the text of the local file (image).
     * @param identifyLanguage If the server should try to identify the language to generate text.
     */
    public OCRObject uploadLocalImage(Path localFile, String language, boolean identifyLanguage) {
        MultiPartBody multiPartBody;

        if (identifyLanguage) {
            multiPartBody = new MultiPartBody()
                    .addPart("file", localFile)
                    .addPart("language", language)
                    .addPart("detectOrientation", "true")
                    .addPart("scale", "true")
                    .addPart("OCREngine", "2");

        } else {
            multiPartBody = new MultiPartBody()
                    .addPart("file", localFile)
                    .addPart("language", language)
                    .addPart("detectOrientation", "true")
                    .addPart("scale", "true");
        }

        // Make the OCR request.
        ConnectionManager connectionManager = new ConnectionManager();
        HttpResponse<String> response = connectionManager.makeOCRRequest(multiPartBody);

        // Parse response to object.
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parseOCRResponse(response);
    }

    /**
     *
     * @param urlFile URL to a file (image).
     * @param language The language of the text of the file (image).
     * @param identifyLanguage If the server should try to identify the language to generate text.
     */
    public OCRObject uploadURLImage(String urlFile, String language, boolean identifyLanguage) {
        MultiPartBody multiPartBody;

        if (identifyLanguage) {
            multiPartBody = new MultiPartBody()
                    .addPart("url", urlFile)
                    .addPart("language", language)
                    .addPart("detectOrientation", "true")
                    .addPart("scale", "true")
                    .addPart("OCREngine", "2");

        } else {
            multiPartBody = new MultiPartBody()
                    .addPart("url", urlFile)
                    .addPart("language", language)
                    .addPart("detectOrientation", "true")
                    .addPart("scale", "true");
        }

        // Make the OCR request.
        ConnectionManager connectionManager = new ConnectionManager();
        HttpResponse<String> response = connectionManager.makeOCRRequest(multiPartBody);

        // Parse response to object.
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parseOCRResponse(response);
    }
}
