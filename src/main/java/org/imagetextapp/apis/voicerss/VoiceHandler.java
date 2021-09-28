package org.imagetextapp.apis.voicerss;

import org.imagetextapp.utility.ConnectionManager;
import org.imagetextapp.utility.MultiPartBody;
import org.imagetextapp.utility.PropertiesReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Generate voice file (.wav) that reads out the specified text in the specified language.
 * Uses the Voice RSS API:
 * http://www.voicerss.org/api/
 */
public class VoiceHandler {

    /**
     *
     * @param language of the text
     * @param speechVoice name of the reader
     * @param text to be read
     */
    public boolean voiceFormData(String language, String speechVoice, String text) {
        MultiPartBody multiPartBody;

        multiPartBody = new MultiPartBody()
                .addPart("key", PropertiesReader.getProperty("VOICERSS_APIKEY"))
                .addPart("src", text)
                .addPart("hl", language)
                .addPart("v", speechVoice)
                .addPart("b64", "true");

        ConnectionManager connectionManager = new ConnectionManager();
        HttpResponse<String> response = connectionManager.makeGetVoiceRequestForm(multiPartBody);

        // Remove "base64"-identifier in the beginning of the response and keep only the actual Base64 encoding.
        String separatorSymbol = ",";
        String encoded64BaseAudio = response.body().split(separatorSymbol)[1];
        byte[] decodedVoiceBytes = Base64.getDecoder().decode(encoded64BaseAudio.getBytes(StandardCharsets.UTF_8));

        // Write decoded bytes from the returned voice to a .wav file.
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("generated_voice/voice.wav");
            fos.write(decodedVoiceBytes, 0, decodedVoiceBytes.length);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            System.out.println("An error occurred while trying to decode Base64 voice to .wav file.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
