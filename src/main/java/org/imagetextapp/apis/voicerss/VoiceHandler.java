package org.imagetextapp.apis.voicerss;

import org.imagetextapp.utility.ConnectionManager;
import org.imagetextapp.utility.MultiPartBody;
import org.imagetextapp.utility.PropertiesReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Generate voice file (.wav) that reads out the specified text, in the specified language.
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
        System.out.println("StatusCode of VoiceRSS request: " + response.statusCode());
//        System.out.println("Body of VoiceRSS Request: " + response.body());

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

    public static void main(String[] args) {
        VoiceHandler voiceHandler = new VoiceHandler();
        String text = "ANNA KARENINA \" But then, while she was here in the house with us, I did not permit myself any liberties. And the worst of all is that she is already.... All this must needs happen just to spite me. A1! ar! all But what, what is to be done ? There was no answer except that common answer which life gives to all the most complicated and unsolva- ble questions, —this answer: You must live according to circumstances, in other words, forget yourself. But as you cannot forget yourself in sleep —at least till night, as you cannot return to that music which the water-bottle woman sang, therefore you must forget yourself in the dream of life ! \"We shall see by and by,\" said Stepan Arkadyevitch to himself, and rising he put on his gray dressing-gown with blue silk lining, tied the tassels into a knot, and took a full breath into his ample lungs. Then with his usual firm step, his legs spread somewhat apart and easily bearing the solid weight of his body, he went over to the window, lifted the curtain, and loudly rang It was instantly answered by his oldi friend the bell. and valet Matve, who came in bringing his clothes, boots, and a telegram. Behind Matve came the barber with the shaving utensils. \" Are there any papers from the court-house ? \" asked Stepan Arkadyevitch, taking the telegram and taking his seat in front of the mirror. .... \" On the breakfast-table,\" replied Matve, looking inquiringly and with sympathy at his master, and after an instant's pause, added with a sly smile, \" They have come from the boss of the livery-stable.\" Stepan Arkadyevitch made no reply and only looked at Matve in the mirror. By the look which they inter- changed it could be seen how they understood each other. The look of Stepan Arkadyevitch seemed to ask, \" Why did you say that? Don't you know?\" Matve thrust his hands in his jacket pockets, kicked out his leg, and silently, good-naturedly, almost smiling, looked back to his master : —-- \" I ordered him to come on Sunday, and till then that";
        voiceHandler.voiceFormData("en-us", "Amy", text);
    }
}
