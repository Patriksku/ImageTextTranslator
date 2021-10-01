# ImageTextTranslator

**ImageTextTranslator** is a hobby project with the purpose of providing some neat functionalities that are based on text-containing images. You can for instance feed the application with a photo that you have taken of a book page and generate that text on your computer. Once you have generated your text, you can also choose to translate the text to another language. The text-to-speech functionality allows for playing a piece of audio that reads out the text in different languages and voices. Finally, it is also possible to save the generated text as a .txt file, or to save the audio as a .wav file on your computer.


# Functionality

- **Generate** text output from text-containing images (local images or from a URL).
- **Identify** the language of the text.
- **Save** the generated text as .txt files locally.
- **Translation** of text in different languages.
- **Text-to-speech** with voice and language options.
- **Save** the generated audio as .wav files locally.

## Language support
The application supports **16 different languages**:
- Croatian
- Czech
- Danish
- Dutch
- English
- Finnish
- French
- German
- Hungarian
- Italian
- Polish
- Portuguese
- Slovenian
- Spanish
- Swedish
- Turkish

# APIs used

OCR API: https://ocr.space/ocrapi
Detect Language API: https://detectlanguage.com/documentation
Google Translate API: https://english.api.rakuten.net/datascraper/api/google-translate20/endpoints
Voice RSS (Text-to-speech) API: http://www.voicerss.org/api/

# User Implementation

As there are request limits for each API, the user will have to input his/her own credentials/keys for each API for the application to work. This can be done easily by navigating to the **PropertiesReader.java** class in the **Utility** package:
- Change USER_API_IMPLEMENTATION to true.
- Input your API credentials into the Strings bellow.
- Use/test the application!

# Screenshots

Generate Text from an image (URL):
![Text generation from an image](https://i.imgur.com/c0iuxxf.png)

Translation from English to Swedish:
![Language translation](https://i.imgur.com/MiZtGdD.jpg)

Generating Text-to-speech in Polish with the "Julia" voice:
![Text-to-speech options](https://i.imgur.com/LU26ruH.png)

# About
The application is written in Java. It uses threads for handling GUI-components. The implemented design pattern for the GUI component is MVC.
