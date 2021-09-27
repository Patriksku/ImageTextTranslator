package org.imagetextapp.utility;

import org.imagetextapp.apis.detectlanguage.DetectLanguageObject;
import org.imagetextapp.apis.ocr.OCRObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.http.HttpResponse;

/**
 * Parses JSON responses from APIs and returns them.
 */
public class JsonParser {

    /**
     *
     * @param response JSON response from the OCR API.
     * @return object representation of the response.
     */
    public OCRObject parseOCRResponse(HttpResponse<String> response) {
        StringManager stringManager = new StringManager();
        OCRObject jsonToOCR = new OCRObject();

        String jsonString = response.body();
        JSONObject parsedResObject = new JSONObject(jsonString);

        // If OCR server response indicates that an error occurred.
        if (parsedResObject.getBoolean("IsErroredOnProcessing")) {

            System.out.println("An error occurred while trying to process the OCR request. Description: ");
            JSONArray errorArray = parsedResObject.getJSONArray("ErrorMessage");
            System.out.println(errorArray.getString(0));

            jsonToOCR.setErrorOnProcessing(true);
            jsonToOCR.setErrorMessage(errorArray.getString(0));
        }

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
        }
        return jsonToOCR;
    }

    /**
     *
     * @param response JSON response from the Detect Language API.
     * @return object representation of the response.
     */
    public DetectLanguageObject parseDetectLanguageResponse(HttpResponse<String> response) {
        DetectLanguageObject jsonToDetectLanguage = new DetectLanguageObject();

        String jsonString = response.body();
        JSONObject parsedResObject = new JSONObject(jsonString);
        JSONObject dataObject = parsedResObject.getJSONObject("data");
        JSONArray detectionsArray = dataObject.getJSONArray("detections");

        // If Detect Language server response indicates that an error occurred.
        if (detectionsArray.isEmpty()) {
            System.out.println("An error occurred while trying to process the Detect Language request.");
            jsonToDetectLanguage.setErrorOnProcessing(true);
        }

        else {
            // Set values from returned "detections" array from response.
            jsonToDetectLanguage.setLanguage(detectionsArray.getJSONObject(0).getString("language"));
            jsonToDetectLanguage.setReliable(detectionsArray.getJSONObject(0).getBoolean("isReliable"));
            jsonToDetectLanguage.setConfidence(detectionsArray.getJSONObject(0).getFloat("confidence"));
        }

        return jsonToDetectLanguage;
    }

    /**
     *
     * @param response JSON response from Google Translate API.
     * @return translated text.
     */
    public String parseTranslateResponse(HttpResponse<String> response) {
        String jsonString = response.body();
        JSONObject parsedResObject = new JSONObject(jsonString);
        JSONObject dataObject;
        JSONArray translationsArray;
        try {
            dataObject = parsedResObject.getJSONObject("data");
            translationsArray = dataObject.getJSONArray("translations");
        } catch (JSONException e) {
            System.out.println(parsedResObject.getString("message"));
            return parsedResObject.getString("message");
        }

        // If Translate server response indicates that an error occurred.
        if (translationsArray.getJSONObject(0).getString("translatedText").equals("")) {
            System.out.println("An error occurred while trying to process the Translate request.");
            System.out.println("Make sure that the text is not empty.");
            return "";
        }

        return translationsArray.getJSONObject(0).getString("translatedText");
    }
}
