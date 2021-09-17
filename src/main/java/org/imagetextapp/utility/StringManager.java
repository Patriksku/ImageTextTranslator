package org.imagetextapp.utility;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Performs various operations on strings.
 */
public class StringManager {

    /**
     *
     * @param toClean string.
     * @return clean version without special characters.
     */
    public String getCleanString(String toClean) {
        return toClean.replace("\r\n", " ").replace("\n", " ");
    }

    public void saveTextToFile(String txt, String fileName) {
        String savePath = "C:\\Users\\fourseven\\Desktop\\" + fileName;
        try {
            Files.writeString(Paths.get(savePath), txt);
            System.out.println("Filename successfully saved: " + fileName);
        } catch (IOException io) {
            System.out.println("An IOException occurred while trying to save " + fileName);
        }
    }

    /**
     *
     * @param toEncode string.
     * @return url encoded string.
     */
    public String getTextURLEncoded(String toEncode) {
        return URLEncoder.encode(toEncode, StandardCharsets.UTF_8);
    }

    /**
     *
     * @param url url to be parameterized.
     * @param query value.
     * @return parameterized url.
     */
    public String getParameterizedURL(String url, String query) {
        return url + "?q=" + query;
    }

    /**
     *
     * @param toTranslate string.
     * @param target language.
     * @param source language.
     * @return string in required "application/x-www-form-urlencoded" with correct format.
     */
    public String getAppXForm(String toTranslate, String target, String source) {
        return "q=" + toTranslate + "&" +
                "target=" + target + "&" +
                "source=" + source;
    }

    /**
     *
     * @param toTranslate string.
     * @param target language.
     * @return string in required "application/x-www-form-urlencoded" with correct format.
     */
    public String getAppXForm(String toTranslate, String target) {
        return "q=" + toTranslate + "&" +
                "target=" + target;
    }



    public static void main (String[] args) {
        String unClean = "ANNA KARENINA\r\n\" But then, while she was here in the house with us, I\r\ndid not permit myself any liberties. And the worst of\r\nall is that she is already.... All this must needs happen\r\njust to spite me. A1! ar! all But what, what is to be\r\ndone ?\r\nThere was no answer except that common answer\r\nwhich life gives to all the most complicated and unsolva-\r\nble questions, —this answer: You must live according\r\nto circumstances, in other words, forget yourself. But\r\nas you cannot forget yourself in sleep —at least till\r\nnight, as you cannot return to that music which the\r\nwater-bottle woman sang, therefore you must forget\r\nyourself in the dream of life !\r\n\"We shall see by and by,\" said Stepan Arkadyevitch\r\nto himself, and rising he put on his gray dressing-gown\r\nwith blue silk lining, tied the tassels into a knot, and\r\ntook a full breath into his ample lungs. Then with his\r\nusual firm step, his legs spread somewhat apart and\r\neasily bearing the solid weight of his body, he went\r\nover to the window, lifted the curtain, and loudly rang\r\nIt was instantly answered by his oldi friend\r\nthe bell.\r\nand valet Matve, who came in bringing his clothes,\r\nboots, and a telegram. Behind Matve came the barber\r\nwith the shaving utensils.\r\n\" Are there any papers from the court-house ? \" asked\r\nStepan Arkadyevitch, taking the telegram and taking\r\nhis seat in front of the mirror.\r\n.... \" On the breakfast-table,\" replied Matve, looking\r\ninquiringly and with sympathy at his master, and after\r\nan instant's pause, added with a sly smile, \" They have\r\ncome from the boss of the livery-stable.\"\r\nStepan Arkadyevitch made no reply and only looked\r\nat Matve in the mirror. By the look which they inter-\r\nchanged it could be seen how they understood each\r\nother. The look of Stepan Arkadyevitch seemed to\r\nask, \" Why did you say that? Don't you know?\"\r\nMatve thrust his hands in his jacket pockets, kicked\r\nout his leg, and silently, good-naturedly, almost smiling,\r\nlooked back to his master : —--\r\n\" I ordered him to come on Sunday, and till then that\r\n";

        StringManager stringManager = new StringManager();
        System.out.println(stringManager.getCleanString(unClean));

        // or getCleanString
        stringManager.saveTextToFile(unClean, "test.txt");
    }
}
