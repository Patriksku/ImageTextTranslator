package org.imagetextapp.gui;

import org.imagetextapp.apis.detectlanguage.DetectLanguageHandler;
import org.imagetextapp.apis.detectlanguage.DetectLanguageObject;
import org.imagetextapp.apis.ocr.OCRHandler;
import org.imagetextapp.apis.ocr.OCRObject;
import org.imagetextapp.apis.translate.TranslateHandler;
import org.imagetextapp.apis.translate.TranslateObject;
import org.imagetextapp.apis.voicerss.VoiceHandler;
import org.imagetextapp.utility.LanguageCodeMapper;
import org.imagetextapp.utility.MimeManager;
import org.imagetextapp.utility.StringManager;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Controller for the GUI. Responsible for invoking specified methods based on a user's input.
 * Stores data in Model, and communicates with GUI-elements through View.
 */
public class Controller {

    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Initializes the application.
     */
    public void initializeApplication() {
        initializeView();
        initializeController();
    }

    /**
     * Initializes all default GUI-elements.
     */
    private void initializeView() {
        initStandardEnabling();
        fillDropDowns();
        handleSelectVoiceBox();
    }

    /**
     * Initializes listeners for user actions in the (GUI) application.
     */
    private void initializeController() {
        enableListeners();
        enableButtonGroups();
    }

    /**
     * Enables necessary listeners for various GUI-elements.
     */
    private void enableListeners() {
        view.getFileTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                model.setUserFilePathInput(view.getFileTextField().getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                model.setUserFilePathInput(view.getFileTextField().getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                model.setUserFilePathInput(view.getFileTextField().getText());
            }
        });

        view.getFileButton().addActionListener(e ->
                fileButtonAction()
        );

        view.getFileLinkRadioButton().addActionListener(e ->
                view.getFileButton().setEnabled(false)
        );

        view.getLocalFileRadioButton().addActionListener(e ->
                view.getFileButton().setEnabled(true)
        );

        view.getUnknownCheckBox().addActionListener(e ->
                view.getTextGenerationLanguageBox().setEnabled(!view.getUnknownCheckBox().isSelected())
        );

        view.getStandardLayoutRadioButton().addActionListener(e ->
                view.getTextArea().setText(model.getOcrObject().getParsedTextClean())
        );

        view.getRetainLayoutRadioButton().addActionListener(e ->
                view.getTextArea().setText(model.getOcrObject().getParsedText())
        );

        view.getStandardLayoutRadioButtonOpt().addActionListener(e ->
                view.getTextAreaOpt().setText(model.getStandardLayoutOpt())
        );

        view.getRetainLayoutRadioButtonOpt().addActionListener(e ->
                view.getTextAreaOpt().setText(model.getRetainLayoutOpt())
        );

        view.getGenerateTextButton().addActionListener(e ->
                generateTextButtonAction()
        );

        view.getCopyTextButton().addActionListener(e ->
                copyToClipboard(view.getCopyTextButton())
        );

        view.getSaveTextButton().addActionListener(e ->
                saveLocalTxtFile(view.getSaveTextButton())
        );

        view.getSaveTextOptButton().addActionListener(e ->
                saveLocalTxtFile(view.getSaveTextOptButton()));

        view.getSaveVoiceButton().addActionListener(e ->
                saveLocalVoiceFile());

        view.getUnknownOptCheckBox().addActionListener(e ->
                view.getTranslateFromBox().setEnabled(!view.getUnknownOptCheckBox().isSelected())
        );

        view.getIdentifyLanguageButton().addActionListener(e ->
                identifyLanguage()
        );

        view.getTranslateTextButton().addActionListener(e ->
                translateLanguage()
        );

        view.getTextLanguageBox().addActionListener(e ->
                handleSelectVoiceBox()
        );

        view.getCopyTextOptButton().addActionListener(e ->
                copyToClipboard(view.getCopyTextOptButton()));

        view.getGenerateVoiceButton().addActionListener(e ->
                generateVoice());

        view.getPlayVoiceButton().addActionListener(e ->
                playVoice());

        view.getStopVoiceButton().addActionListener(e ->
                stopVoice());

        view.getTextAreaOpt().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

                model.setUserMarkedTextAreaOpt(view.getTextAreaOpt().getSelectedText());

                if (view.getTextAreaOpt().getSelectedText() != null) {
                    // If selected text only contains whitespaces.
                    if (view.getTextAreaOpt().getSelectedText().trim().isEmpty()) {
                        model.setUserMarkedTextAreaOpt(null);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        view.getCreatedBy().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/Patriksku/ImageTextTranslator"));
                } catch (IOException | URISyntaxException Exception) {
                    Exception.printStackTrace();
                    System.out.println("Unable to open Github-link from the 'About'-tab");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                view.getCreatedBy().setText("<html><a href=''>" + model.getCreatedByText() + "</a></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                view.getCreatedBy().setText(model.getCreatedByText());
            }
        });
    }

    /**
     * Groups together radio buttons for correct GUI logic.
     */
    private void enableButtonGroups() {
        ButtonGroup localLinkGroup = new ButtonGroup();
        localLinkGroup.add(view.getLocalFileRadioButton());
        localLinkGroup.add(view.getFileLinkRadioButton());

        ButtonGroup generatedTextOptionsGroup = new ButtonGroup();
        generatedTextOptionsGroup.add(view.getStandardLayoutRadioButton());
        generatedTextOptionsGroup.add(view.getRetainLayoutRadioButton());

        ButtonGroup translationVoiceOptionsGroup = new ButtonGroup();
        translationVoiceOptionsGroup.add(view.getStandardLayoutRadioButtonOpt());
        translationVoiceOptionsGroup.add(view.getRetainLayoutRadioButtonOpt());
    }

    /**
     * Sets necessary GUI-elements to be disabled by standard, until some
     * user interaction enables the buttons and their corresponding functionalities.
     */
    private void initStandardEnabling() {
        // Text generation tab
        view.getCopyTextButton().setEnabled(false);
        view.getSaveTextButton().setEnabled(false);

        // Translation tab
        view.getIdentifyLanguageButton().setEnabled(false);
        view.getTranslateTextButton().setEnabled(false);

        view.getGenerateVoiceButton().setEnabled(false);
        view.getPlayVoiceButton().setEnabled(false);
        view.getStopVoiceButton().setEnabled(false);
        view.getSaveVoiceButton().setEnabled(false);

        view.getCopyTextOptButton().setEnabled(false);
        view.getSaveTextOptButton().setEnabled(false);
    }

    /**
     * Fills JComboBoxes with correct items needed for the functionality of the application.
     */
    private void fillDropDowns() {
        // Fill textGenerationLanguageBox with all supported languages.
        // OCR API codes as values.
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Croatian", "hrv"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Czech", "cze"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Danish", "dan"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Dutch", "dut"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("English", "eng"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Finnish", "fin"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("French", "fre"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("German", "ger"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Hungarian", "hun"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Italian", "ita"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Polish", "pol"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Portuguese", "por"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Slovenian", "slv"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Spanish", "spa"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Swedish", "swe"));
        view.getTextGenerationLanguageBox().addItem(new BoxItem("Turkish", "tur"));

        // Set the standard value to "English".
        view.getTextGenerationLanguageBox().setSelectedIndex(4);

        // Fill translateBoxes with all supported languages.
        // Google Translate API codes as values.
        view.getTranslateFromBox().addItem(new BoxItem("Croatian", "hr"));
        view.getTranslateToBox().addItem(new BoxItem("Croatian", "hr"));
        view.getTranslateFromBox().addItem(new BoxItem("Czech", "cs"));
        view.getTranslateToBox().addItem(new BoxItem("Czech", "cs"));
        view.getTranslateFromBox().addItem(new BoxItem("Danish", "da"));
        view.getTranslateToBox().addItem(new BoxItem("Danish", "da"));
        view.getTranslateFromBox().addItem(new BoxItem("Dutch", "nl"));
        view.getTranslateToBox().addItem(new BoxItem("Dutch", "nl"));
        view.getTranslateFromBox().addItem(new BoxItem("English", "en"));
        view.getTranslateToBox().addItem(new BoxItem("English", "en"));
        view.getTranslateFromBox().addItem(new BoxItem("Finnish", "fi"));
        view.getTranslateToBox().addItem(new BoxItem("Finnish", "fi"));
        view.getTranslateFromBox().addItem(new BoxItem("French", "fr"));
        view.getTranslateToBox().addItem(new BoxItem("French", "fr"));
        view.getTranslateFromBox().addItem(new BoxItem("German", "de"));
        view.getTranslateToBox().addItem(new BoxItem("German", "de"));
        view.getTranslateFromBox().addItem(new BoxItem("Hungarian", "hu"));
        view.getTranslateToBox().addItem(new BoxItem("Hungarian", "hu"));
        view.getTranslateFromBox().addItem(new BoxItem("Italian", "it"));
        view.getTranslateToBox().addItem(new BoxItem("Italian", "it"));
        view.getTranslateFromBox().addItem(new BoxItem("Polish", "pl"));
        view.getTranslateToBox().addItem(new BoxItem("Polish", "pl"));
        view.getTranslateFromBox().addItem(new BoxItem("Portuguese", "pt"));
        view.getTranslateToBox().addItem(new BoxItem("Portuguese", "pt"));
        view.getTranslateFromBox().addItem(new BoxItem("Slovenian", "sl"));
        view.getTranslateToBox().addItem(new BoxItem("Slovenian", "sl"));
        view.getTranslateFromBox().addItem(new BoxItem("Spanish", "es"));
        view.getTranslateToBox().addItem(new BoxItem("Spanish", "es"));
        view.getTranslateFromBox().addItem(new BoxItem("Swedish", "sv"));
        view.getTranslateToBox().addItem(new BoxItem("Swedish", "sv"));
        view.getTranslateFromBox().addItem(new BoxItem("Turkish", "tr"));
        view.getTranslateToBox().addItem(new BoxItem("Turkish", "tr"));

        // Set the standard values to "English".
        view.getTranslateFromBox().setSelectedIndex(4);
        view.getTranslateToBox().setSelectedIndex(4);

        // Fill TextLanguageBoxes with all supported languages.
        // VoiceRSS API codes as values.
        view.getTextLanguageBox().addItem(new BoxItem("Croatian", "hr-hr"));
        view.getTextLanguageBox().addItem(new BoxItem("Czech", "cs-cz"));
        view.getTextLanguageBox().addItem(new BoxItem("Danish", "da-dk"));
        view.getTextLanguageBox().addItem(new BoxItem("Dutch (Belgium)", "nl-be"));
        view.getTextLanguageBox().addItem(new BoxItem("Dutch (Netherlands)", "nl-nl"));
        view.getTextLanguageBox().addItem(new BoxItem("English (Australia)", "en-au"));
        view.getTextLanguageBox().addItem(new BoxItem("English (Canada)", "en-ca"));
        view.getTextLanguageBox().addItem(new BoxItem("English (Great Britain)", "en-gb"));
        view.getTextLanguageBox().addItem(new BoxItem("English (India)", "en-in"));
        view.getTextLanguageBox().addItem(new BoxItem("English (Ireland)", "en-ie"));
        view.getTextLanguageBox().addItem(new BoxItem("English (United States)", "en-us"));
        view.getTextLanguageBox().addItem(new BoxItem("Finnish", "fi-fi"));
        view.getTextLanguageBox().addItem(new BoxItem("French (Canada)", "fr-ca"));
        view.getTextLanguageBox().addItem(new BoxItem("French (France)", "fr-fr"));
        view.getTextLanguageBox().addItem(new BoxItem("French (Switzerland)", "fr-ch"));
        view.getTextLanguageBox().addItem(new BoxItem("German (Austria)", "de-at"));
        view.getTextLanguageBox().addItem(new BoxItem("German (Germany)", "de-de"));
        view.getTextLanguageBox().addItem(new BoxItem("German (Switzerland)", "de-ch"));
        view.getTextLanguageBox().addItem(new BoxItem("Hungarian", "hu-hu"));
        view.getTextLanguageBox().addItem(new BoxItem("Italian", "it-it"));
        view.getTextLanguageBox().addItem(new BoxItem("Polish", "pl-pl"));
        view.getTextLanguageBox().addItem(new BoxItem("Portuguese (Brazil)", "pt-br"));
        view.getTextLanguageBox().addItem(new BoxItem("Portuguese (Portugal)", "pt-pt"));
        view.getTextLanguageBox().addItem(new BoxItem("Slovenian", "sl-si"));
        view.getTextLanguageBox().addItem(new BoxItem("Spanish (Mexico)", "es-mx"));
        view.getTextLanguageBox().addItem(new BoxItem("Spanish (Spain)", "es-es"));
        view.getTextLanguageBox().addItem(new BoxItem("Swedish", "sv-se"));
        view.getTextLanguageBox().addItem(new BoxItem("Turkish", "tr-tr"));

        // Set the standard value to "English - USA".
        view.getTextLanguageBox().setSelectedIndex(10);
    }

    /**
     * Handles the user-specified local file that is to be processed for text generation.
     */
    private void fileButtonAction() {
        JFileChooser fileChooser = new JFileChooser();
        int chosenFile = fileChooser.showOpenDialog(view);

        if (chosenFile == JFileChooser.APPROVE_OPTION) {
            model.setSelectedFile(fileChooser.getSelectedFile());

            try {
                String mime = Files.probeContentType(model.getSelectedFile().toPath());
                MimeManager mimeManager = new MimeManager();

                // If user has specified a valid image file for processing.
                if (mimeManager.validImageFile(mime)) {
                    view.getFileTextField().setText(model.getSelectedFile().toString());
                }
                else {
                    openUnsupportedFileDialog();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Initiates text generation for the file that the user has specified.
     */
    private void generateTextButtonAction() {

        // If generate text for local file is selected
        if (view.getLocalFileRadioButton().isSelected() && !view.getFileTextField().getText().equals("")) {
            generateTextForLocalFile();

            // If generate text for image link is selected
        } else if (view.getFileLinkRadioButton().isSelected() && !view.getFileTextField().getText().equals("")) {
            generateTextForImageLink();
        }
    }

    /**
     * Generates text based on the contents of the user-selected local file.
     */
    private void generateTextForLocalFile() {
        resetStatusLabel();
        Path path;

        // Verifies so that a valid file-directory has been specified by the user.
        try {
            path = Path.of(model.getUserFilePathInput());
        } catch (InvalidPathException IPE) {
            openInvalidFileDialog();
            return;
        }

        // Assigns a valid file-directory.
        model.setSelectedFile(path.toFile());
        long fileSizeBytes = model.getSelectedFile().length();
        long fileSizeKB = fileSizeBytes / 1024;

        // Checks if selected file is within the file size limit (1 MB).
        if (fileSizeKB > model.getFILE_SIZE_LIMIT()) {
            openTooLargeFileSizeDialog();
        } else if (fileSizeKB == 0) {
            openInvalidFileDialog();
        } else {
            try {
                String mime = Files.probeContentType(path);
                MimeManager mimeManager = new MimeManager();

                // Checks if the content type of the selected file is supported for text generation.
                if (mimeManager.validImageFile(mime)) {
                    BoxItem item = (BoxItem) view.getTextGenerationLanguageBox().getSelectedItem();
                    assert item != null;
                    String selectedLanguage = item.getValue();

                    setStatusLabel("Size of local file: " + fileSizeKB + "KB. Initializing text generation...");

                    // Thread for handling the server communication and safely updating GUI throughout the process.
                    SwingWorker<OCRObject, String> worker = new SwingWorker<>() {

                        @Override
                        protected OCRObject doInBackground() {
                            OCRHandler ocrHandler = new OCRHandler();
                            model.setOcrObject(ocrHandler.uploadLocalImage(path, selectedLanguage, view.getUnknownCheckBox().isSelected()));
                            publish();

                            return model.getOcrObject();
                        }

                        @Override
                        protected void process(List<String> chunks) {
                            setStatusLabel("Processing the file...");
                        }

                        @Override
                        protected void done() {
                            try {
                                OCRObject ocrObject = get();

                                // Resets any memory of text and/or alterations made in Translation & Options tab,
                                // As a new text-generation replaces all previous actions made in that tab.
                                model.setStandardLayoutOpt("");
                                model.setRetainLayoutOpt("");

                                provideGeneratedText(ocrObject);

                                if (ocrObject.isErrorOnProcessing()) {
                                    deactivateCopySaveButtons();
                                    openCustomOCRErrorDialog(ocrObject);
                                    resetStatusLabel();
                                } else {
                                    activateCopySaveButtons();
                                    enableTranslationTab();
                                    setStatusLabel("Success. Processing time: " + ocrObject.getProcessingTime() + " ms.");
                                    model.setTRANSLATED(false);
                                }

                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    worker.execute();

                } else {
                    openUnsupportedFileDialog();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Generates text based on the contents of the user-selected URL file.
     */
    private void generateTextForImageLink() {
        resetStatusLabel();
        BoxItem item = (BoxItem) view.getTextGenerationLanguageBox().getSelectedItem();
        assert item != null;
        String selectedLanguage = item.getValue();

        setStatusLabel("Initializing text generation for the provided url-link...");

        // Thread for handling the server communication and safely updating GUI throughout the process.
        SwingWorker<OCRObject, String> worker = new SwingWorker<>() {
            @Override
            protected OCRObject doInBackground() {
                OCRHandler ocrHandler = new OCRHandler();
                model.setOcrObject(ocrHandler.uploadURLImage(model.getUserFilePathInput(), selectedLanguage, view.getUnknownCheckBox().isSelected()));
                publish();

                return model.getOcrObject();
            }

            @Override
            protected void process(List<String> chunks) {
                setStatusLabel("Processing the file...");
            }

            @Override
            protected void done() {
                try {
                    OCRObject ocrObject = get();

                    // Resets any memory of text and/or alterations made in Translation & Options tab,
                    // As a new text-generation replaces all previous actions made in that tab.
                    model.setStandardLayoutOpt("");
                    model.setRetainLayoutOpt("");

                    provideGeneratedText(ocrObject);

                    if (ocrObject.isErrorOnProcessing()) {
                        deactivateCopySaveButtons();
                        openCustomOCRErrorDialog(ocrObject);
                        resetStatusLabel();
                    } else {
                        enableTranslationTab();
                        activateCopySaveButtons();
                        setStatusLabel("Success. Processing time: " + ocrObject.getProcessingTime() + " ms.");
                        model.setTRANSLATED(false);
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }



    /**
     * Fills the JTextArea GUI-element with the user-selected text layout.
     * @param ocrObject Object-representation containing the JSON response from OCR API.
     */
    private void provideGeneratedText(OCRObject ocrObject) {
        if (ocrObject != null) {

            // Initialize both layouts in Translation & Options tab.
            if (model.getStandardLayoutOpt().equals("")) {
                model.setStandardLayoutOpt(ocrObject.getParsedTextClean());
            }

            if (model.getRetainLayoutOpt().equals("")) {
                model.setRetainLayoutOpt(ocrObject.getParsedText());
            }

            if (view.getStandardLayoutRadioButton().isSelected()) {
                view.getTextArea().setText(ocrObject.getParsedTextClean());
                view.getTextAreaOpt().setText(ocrObject.getParsedTextClean());
            } else {
                view.getTextArea().setText(ocrObject.getParsedText());
                view.getTextAreaOpt().setText(ocrObject.getParsedText());
            }
        }
    }

    /**
     * Copies the contents of the JTextArea GUI-element in the current tab to the clipboard.
     */
    private void copyToClipboard(JButton button) {

        // If button is in text generation tab.
        if (button == view.getCopyTextButton()) {
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(view.getTextArea().getText()), null);
            setStatusLabel("Copied to clipboard.");

            // Otherwise, it is in translation & options tab.
        } else {
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(view.getTextAreaOpt().getText()), null);
            setStatusOptLabel("Copied to clipboard.");
        }
    }

    /**
     * Saves the contents of the JTextArea GUI-element to a .txt file locally, bounded by the
     * naming and directory choice of the user.
     */
    private void saveLocalTxtFile(JButton button) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(view);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (file == null) {
                return;
            }

            // Handle .txt extension of filename based on how the user named the file that is to be saved.
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            } else {
                file = new File(file.getParentFile(), file.getName());
            }

            try {
                // Quick-fix clone for displaying the text correctly while maintaining same structure
                // in saved file.
                JTextArea clone = new JTextArea();
                StringManager stringManager = new StringManager();

                // If save button in Generation-tab is used - save contents inside Text Generation tab to .txt file.
                if (button == view.getSaveTextButton()) {
                    clone.setText(stringManager.removeExtraNewLines(view.getTextArea().getText()));

                    // Else save contents inside Translation & Options tab to .txt file.
                } else {
                    // Special care for translated text ensures that it is saved with correct layout.
                    if (model.isTRANSLATED()) {
                        if (view.getRetainLayoutRadioButtonOpt().isSelected()) {
                            clone.setText(stringManager.removeExtraNewLines(model.getRetainLayoutOpt()));
                        } else {
                            clone.setText(view.getTextAreaOpt().getText());
                        }
                    } else {
                        clone.setText(stringManager.removeExtraNewLines(view.getTextAreaOpt().getText()));
                    }
                }
                clone.write(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                openSaveTextOKDialog();

            } catch (IOException e) {
                e.printStackTrace();
                openSaveTextFailedDialog();
            }
        }
    }

    /**
     * Saves the user generated voice file with the name and location that the user has specified.
     */
    private void saveLocalVoiceFile() {
        String pathToVoiceFile = "generated_voice/voice.wav";
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(view);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (file == null) {
                return;
            }

            // Handle .wav extension of filename based on how the user named the file that is to be saved.
            if (!file.getName().toLowerCase().endsWith(".wav")) {
                file = new File(file.getParentFile(), file.getName() + ".wav");
            } else {
                file = new File(file.getParentFile(), file.getName());
            }

            // Saves the file to specified directory.
            try {
                byte[] voiceBytes = Files.readAllBytes(Paths.get(pathToVoiceFile));
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(voiceBytes, 0, voiceBytes.length);
                fos.flush();
                fos.close();
                openSaveVoiceOKDialog();

            } catch (IOException e) {
                e.printStackTrace();
                openSaveVoiceFailedDialog();
            }
        }
    }

    /**
     * Identifies the language of the current text in the JTextArea of the "Translation & Options"-tab.
     */
    private void identifyLanguage() {
        // Verify that textAreaOpt is not empty.
        if (!view.getTextAreaOpt().getText().equals("")) {
            setStatusOptLabel("Initializing language identification...");

            // Thread for handling the server communication and safely updating GUI throughout the process.
            SwingWorker<DetectLanguageObject, String> worker = new SwingWorker<>() {

                @Override
                protected DetectLanguageObject doInBackground() {
                    DetectLanguageHandler detectLanguageHandler = new DetectLanguageHandler();
                    DetectLanguageObject detectLanguageObject = detectLanguageHandler.identifyLanguage(view.getTextAreaOpt().getText());
                    publish();
                    return detectLanguageObject;
                }

                @Override
                protected void process(List<String> chunks) {
                    setStatusOptLabel("Identifying the language...");
                }

                @Override
                protected void done() {
                    DetectLanguageObject detectLanguageObject;
                    try {
                        detectLanguageObject = get();
                        // If language detection errorred on processing.
                        if (detectLanguageObject.isErrorOnProcessing()) {
                            openLanguageIdentificationFailedDialog();
                            resetStatusOptLabel();
                        } else {
                            String languageResult = detectLanguageObject.getLanguage(true);

                            // If language is unsupported.
                            if (languageResult.equals("Unsupported language.")) {
                                setStatusOptLabel("This language is not currently supported for identification.");
                            } else {
                                setStatusOptLabel("Language identified as: " + languageResult + ". Confidence for correct " +
                                        "identification: " + detectLanguageObject.getReliable());

                                fillBoxesAsIdentifiedLanguage(detectLanguageObject.getLanguage(false));
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            };
            worker.execute();

        }
    }

    /**
     * Translates the language of the text in the JTextArea of the "Translation & Options"-tab.
     * The translation details - source (optional) and target language are specified by the user.
     */
    private void translateLanguage() {
        TranslateHandler translateHandler = new TranslateHandler();

        setStatusOptLabel("Translating text...");

        SwingWorker<TranslateObject, String> worker = new SwingWorker<>() {
            @Override
            protected TranslateObject doInBackground() {

                // If source language is unknown.
                if (view.getUnknownOptCheckBox().isSelected()) {
                    BoxItem translateTo = (BoxItem) view.getTranslateToBox().getSelectedItem();
                    assert translateTo != null;

                    return translateHandler.translateText(view.getTextAreaOpt().getText(), translateTo.getValue());

                    // Translate with specified source language as well.
                } else {
                    BoxItem translateFrom = (BoxItem) view.getTranslateFromBox().getSelectedItem();
                    BoxItem translateTo = (BoxItem) view.getTranslateToBox().getSelectedItem();
                    assert translateTo != null;
                    assert translateFrom != null;

                    return translateHandler.translateText(view.getTextAreaOpt().getText(), translateTo.getValue(), translateFrom.getValue());
                }
            }

            @Override
            protected void done() {
                try {
                    model.setTranslateObject(get());
                    createLayoutsForTranslation(model.getTranslateObject());
                    view.getTextAreaOpt().setText(model.getRetainLayoutOpt());
                    setStatusOptLabel("Text translated.");
                    model.setTRANSLATED(true);

                    if (model.getTranslateObject().isErrorOnProcessing()) {
                        openCustomTranslationErrorDialog(model.getTranslateObject());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    resetStatusOptLabel();
                }
            }
        };
        worker.execute();

    }

    /**
     * Generates voice that reads out the current text in the JTextArea of the "Translation & Options"-tab.
     * The generation details - language and voice, are specified by the user.
     */
    private void generateVoice() {
        setStatusOptLabel("Generating voice for text...");

        // If user has selected a specific part to be generated to voice.
        SwingWorker<Boolean, String> worker;
        if (model.getUserMarkedTextAreaOpt() != null) {
            worker = new SwingWorker<>() {

                @Override
                protected Boolean doInBackground() {
                    VoiceHandler voiceHandler = new VoiceHandler();
                    BoxItem selectedLanguage = (BoxItem) view.getTextLanguageBox().getSelectedItem();
                    BoxItem selectedVoice = (BoxItem) view.getSelectVoiceBox().getSelectedItem();
                    assert selectedLanguage != null;
                    assert selectedVoice != null;
                    return voiceHandler.voiceFormData(selectedLanguage.getValue(), selectedVoice.getValue(), model.getUserMarkedTextAreaOpt());
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            activateVoiceButtons();
                            setStatusOptLabel("Successfully generated voice for text.");
                        } else {
                            deactivateVoiceButtons();
                            openVoiceGenerationFailedDialog();
                            resetStatusOptLabel();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        resetStatusOptLabel();
                    }
                }
            };

            // If user has not selected a specific part to be generated to voice.
        } else {
            worker = new SwingWorker<>() {

                @Override
                protected Boolean doInBackground() {
                    VoiceHandler voiceHandler = new VoiceHandler();
                    BoxItem selectedLanguage = (BoxItem) view.getTextLanguageBox().getSelectedItem();
                    BoxItem selectedVoice = (BoxItem) view.getSelectVoiceBox().getSelectedItem();
                    assert selectedLanguage != null;
                    assert selectedVoice != null;
                    return voiceHandler.voiceFormData(selectedLanguage.getValue(), selectedVoice.getValue(), view.getTextAreaOpt().getText());
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            activateVoiceButtons();
                            setStatusOptLabel("Successfully generated voice for text.");
                        } else {
                            deactivateVoiceButtons();
                            openVoiceGenerationFailedDialog();
                            resetStatusOptLabel();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        worker.execute();

    }

    /**
     * Plays the sound file that reads out the text.
     */
    private void playVoice() {

        setStatusOptLabel("Playing voice...");

        SwingWorker<Clip, String> worker = new SwingWorker<>() {
            final String voiceFile = "generated_voice/voice.wav";
            File file;
            Clip clip = null;

            @Override
            protected Clip doInBackground() {
                try {
                    file = new File(voiceFile);
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);

                    model.setAUDIO_CLIP(clip);
                    assert clip != null;

                    clip.setFramePosition(0);
                    clip.start();

                    view.getPlayVoiceButton().setEnabled(false);
                    view.getStopVoiceButton().setEnabled(true);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
                return clip;
            }

            @Override
            protected void done() {
                setStatusOptLabel("Played voice.");
            }
        };

        worker.execute();
    }

    /**
     * Stops the sound file that is currently playing.
     */
    private void stopVoice() {
        setStatusOptLabel("Stopped Voice.");
        SwingWorker<String, String> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                view.getPlayVoiceButton().setEnabled(true);
                view.getStopVoiceButton().setEnabled(false);

                if (model.getAUDIO_CLIP() != null && model.getAUDIO_CLIP().isActive()) {
                    model.getAUDIO_CLIP().stop();
                    model.getAUDIO_CLIP().close();
                }
                return null;
            }
        };

        worker.execute();
    }

    /**
     * The "Translate From:" and "Text Language:" JComboBoxes are automatically selecting the identified
     * language for the user, with the purpose of increasing the ease-of-use of the application.
     * @param detectLanguageCode language code for Detect Language API.
     */
    private void fillBoxesAsIdentifiedLanguage(String detectLanguageCode) {
        LanguageCodeMapper languageCodeMapper = new LanguageCodeMapper();

        // Get the VoiceRSS language code of the detected language.
        String voiceCode = languageCodeMapper.getDetectLanguageToVoiceCode(detectLanguageCode);

        if (voiceCode.equals("Unsupported Code Conversion.")) {
            System.out.println("Unsupported Language Code Conversion from DetectLanguage code to VoiceRSS code.");
        } else {

            // Find and select the detected language in textLanguageBox.
            int size = view.getTextLanguageBox().getItemCount();
            for (int i = 0; i < size; i++) {
                BoxItem boxItem = view.getTextLanguageBox().getItemAt(i);
                if (boxItem.getValue().equals(voiceCode)) {
                    view.getTextLanguageBox().setSelectedIndex(i);
                    break;
                }
            }
        }

        // Get the Google Translate language code of the detected language.
        String translateCode = languageCodeMapper.getDetectLanguageToTranslateCode(detectLanguageCode);

        if (translateCode.equals("Unsupported Code Conversion.")) {
            System.out.println("Unsupported Language Code Conversion from DetectLanguage code to Google Translate code.");
        } else {

            // Find and select the detected language in translateFromBox.
            int size = view.getTranslateFromBox().getItemCount();
            for (int i = 0; i < size; i++) {
                BoxItem boxItem = view.getTranslateFromBox().getItemAt(i);
                if (boxItem.getValue().equals(translateCode)) {
                    view.getTranslateFromBox().setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * Creates text-layouts for the translated text in the "Translation & Options"-tab.
     * @param translateObject Object representation of the response from Google Translate API.
     */
    private void createLayoutsForTranslation(TranslateObject translateObject) {
        StringManager stringManager = new StringManager();
        model.setRetainLayoutOpt(translateObject.getText());
        model.setStandardLayoutOpt(stringManager.getCleanString(translateObject.getText()));
    }


    /**
     * Resetting and setting status labels in the GUI.
     */

    private void setStatusLabel(String status) {
        view.getStatusLabel().setText(status);
    }

    private void setStatusOptLabel(String status) {
        view.getStatusLabelOpt().setText(status);
    }

    private void resetStatusLabel() {
        view.getStatusLabel().setText("Status...");
    }

    private void resetStatusOptLabel() {
        view.getStatusLabelOpt().setText("Status...");
    }

    /**
     * Enables the functionalities of the "Translation & Options"-tab.
     */
    private void enableTranslationTab() {
        view.getIdentifyLanguageButton().setEnabled(true);
        view.getTranslateTextButton().setEnabled(true);

        view.getGenerateVoiceButton().setEnabled(true);

        view.getCopyTextOptButton().setEnabled(true);
        view.getSaveTextOptButton().setEnabled(true);
    }

    /**
     * Activate the "Copy to Clipboard" and "Save to .txt" functionalities when these are ready.
     */
    private void activateCopySaveButtons() {
        view.getCopyTextButton().setEnabled(true);
        view.getSaveTextButton().setEnabled(true);
    }

    /**
     * De-activates the "Copy to Clipboard" and "Save to .txt" functionalities when these are not ready.
     */
    private void deactivateCopySaveButtons() {
        view.getCopyTextButton().setEnabled(false);
        view.getSaveTextButton().setEnabled(false);
    }

    /**
     * Activates the buttons responsible for sound functionality.
     */
    private void activateVoiceButtons() {
        view.getPlayVoiceButton().setEnabled(true);
        view.getSaveVoiceButton().setEnabled(true);
    }

    /**
     * De-activates the buttons responsible for sound functionality.
     */
    private void deactivateVoiceButtons() {
        view.getPlayVoiceButton().setEnabled(false);
        view.getStopVoiceButton().setEnabled(false);
        view.getSaveVoiceButton().setEnabled(false);
    }

    /**
     * Handles the population of voices for each supported language.
     */
    private void handleSelectVoiceBox() {
        BoxItem boxItem = (BoxItem) view.getTextLanguageBox().getSelectedItem();
        assert boxItem != null;
        populateSelectVoiceBox(boxItem.getKey());
    }

    /**
     * Populates the available voices for the currently selected language.
     * @param lang Currently selected language.
     */
    private void populateSelectVoiceBox(String lang) {
        switch (lang) {
            case "Croatian" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Nikola (Male)", "Nikola"));
            }
            case "Czech" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Josef (Male)", "Josef"));
            }
            case "Danish" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Freja (Female)", "Freja"));
            }
            case "Dutch (Belgium)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Daan (Male)", "Daan"));
            }
            case "Dutch (Netherlands)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Lotte (Female)", "Lotte"));
                view.getSelectVoiceBox().addItem(new BoxItem("Bram (Male)", "Bram"));
            }
            case "English (Australia)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Zoe (Female)", "Zoe"));
                view.getSelectVoiceBox().addItem(new BoxItem("Isla (Female)", "Isla"));
                view.getSelectVoiceBox().addItem(new BoxItem("Evie (Female)", "Evie"));
                view.getSelectVoiceBox().addItem(new BoxItem("Jack (Male)", "Jack"));
            }
            case "English (Canada)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Rose (Female)", "Rose"));
                view.getSelectVoiceBox().addItem(new BoxItem("Clara (Female)", "Clara"));
                view.getSelectVoiceBox().addItem(new BoxItem("Emma (Female)", "Emma"));
                view.getSelectVoiceBox().addItem(new BoxItem("Mason (Male)", "Mason"));
            }
            case "English (Great Britain)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Alice (Female)", "Alice"));
                view.getSelectVoiceBox().addItem(new BoxItem("Nancy (Female)", "Nancy"));
                view.getSelectVoiceBox().addItem(new BoxItem("Lily (Female)", "Lily"));
                view.getSelectVoiceBox().addItem(new BoxItem("Harry (Male)", "Harry"));
            }
            case "English (India)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Eka (Female)", "Eka"));
                view.getSelectVoiceBox().addItem(new BoxItem("Jai (Female)", "Jai"));
                view.getSelectVoiceBox().addItem(new BoxItem("Ajit (Male)", "Ajit"));
            }
            case "English (Ireland)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Oran (Male)", "Oran"));
            }
            case "English (United States)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Linda (Female)", "Linda"));
                view.getSelectVoiceBox().addItem(new BoxItem("Amy (Female)", "Amy"));
                view.getSelectVoiceBox().addItem(new BoxItem("Mary (Female)", "Mary"));
                view.getSelectVoiceBox().addItem(new BoxItem("John (Male)", "John"));
                view.getSelectVoiceBox().addItem(new BoxItem("Mike (Male)", "Mike"));
            }
            case "Finnish" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Aada (Female)", "Aada"));
            }
            case "French (Canada)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Emile (Female)", "Emile"));
                view.getSelectVoiceBox().addItem(new BoxItem("Olivia (Female)", "Olivia"));
                view.getSelectVoiceBox().addItem(new BoxItem("Logan (Female)", "Logan"));
                view.getSelectVoiceBox().addItem(new BoxItem("Felix (Male)", "Felix"));
            }
            case "French (France)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Bette (Female)", "Bette"));
                view.getSelectVoiceBox().addItem(new BoxItem("Iva (Female)", "Iva"));
                view.getSelectVoiceBox().addItem(new BoxItem("Zola (Female)", "Zola"));
                view.getSelectVoiceBox().addItem(new BoxItem("Axel (Male)", "Axel"));
            }
            case "French (Switzerland)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Theo (Male)", "Theo"));
            }
            case "German (Austria)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Lukas (Male)", "Lukas"));
            }
            case "German (Germany)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Hanna (Female)", "Hanna"));
                view.getSelectVoiceBox().addItem(new BoxItem("Lina (Female)", "Lina"));
                view.getSelectVoiceBox().addItem(new BoxItem("Jonas (Male)", "Jonas"));
            }
            case "German (Switzerland)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Tim (Male)", "Tim"));
            }
            case "Hungarian" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Mate (Male)", "Mate"));
            }
            case "Italian" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Bria (Female)", "Bria"));
                view.getSelectVoiceBox().addItem(new BoxItem("Mia (Female)", "Mia"));
                view.getSelectVoiceBox().addItem(new BoxItem("Pietro (Male)", "Pietro"));
            }
            case "Polish" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Julia (Female)", "Julia"));
                view.getSelectVoiceBox().addItem(new BoxItem("Jan (Male)", "Jan"));
            }
            case "Portuguese (Brazil)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Marcia (Female)", "Marcia"));
                view.getSelectVoiceBox().addItem(new BoxItem("Ligia (Female)", "Ligia"));
                view.getSelectVoiceBox().addItem(new BoxItem("Yara (Female)", "Yara"));
                view.getSelectVoiceBox().addItem(new BoxItem("Dinis (Male)", "Dinis"));
            }
            case "Portuguese (Portugal)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Leonor (Female)", "Leonor"));
            }
            case "Slovenian" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Vid (Male)", "Vid"));
            }
            case "Spanish (Mexico)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Juana (Female)", "Juana"));
                view.getSelectVoiceBox().addItem(new BoxItem("Silvia (Female)", "Silvia"));
                view.getSelectVoiceBox().addItem(new BoxItem("Teresa (Female)", "Teresa"));
                view.getSelectVoiceBox().addItem(new BoxItem("Jose (Male)", "Jose"));
            }
            case "Spanish (Spain)" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Camila (Female)", "Camila"));
                view.getSelectVoiceBox().addItem(new BoxItem("Sofia (Female)", "Sofia"));
                view.getSelectVoiceBox().addItem(new BoxItem("Luna (Female)", "Luna"));
                view.getSelectVoiceBox().addItem(new BoxItem("Diego (Male)", "Diego"));
            }
            case "Swedish" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Molly (Female)", "Molly"));
                view.getSelectVoiceBox().addItem(new BoxItem("Hugo (Male)", "Hugo"));
            }
            case "Turkish" -> {
                view.getSelectVoiceBox().removeAllItems();
                view.getSelectVoiceBox().addItem(new BoxItem("Omer (Male)", "Omer"));
            }
            default -> {
            }
        }
    }


    /**
     * All dialog windows that are used in the program at different occasions.
     */

    private void openUnsupportedFileDialog() {
        JOptionPane.showMessageDialog(view, "Please select a valid file. " +
                "Supported files: \n" +
                "JPG, GIF, PDF, PNG, BMP or TIFF.", "Unsupported file type detected", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openInvalidFileDialog() {
        JOptionPane.showMessageDialog(view, "Please select a valid file. " +
                "Make sure that the file exists at the specified location.", "Invalid file detected", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openTooLargeFileSizeDialog() {
        JOptionPane.showMessageDialog(view, "This file exceeds the 1 MB (1024 KB) limit for " +
                "generating text from file. \nPlease choose a smaller file.", "Too large file", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openCustomOCRErrorDialog(OCRObject ocrObject) {
        JOptionPane.showMessageDialog(view, ocrObject.getErrorMessage() + "\n" + "OCR Exit Code: "
                + ocrObject.getOcrExitCode() + "\n" + ocrObject.getErrorDetails(), "OCR API: Processing error", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSaveTextOKDialog() {
        JOptionPane.showMessageDialog(view, "File (.txt) successfully saved to specified directory.",
                "Save .txt: Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSaveTextFailedDialog() {
        JOptionPane.showMessageDialog(view, "File (.txt) could not be saved. Try again without adding any extensions to the file name.",
                "Save .txt: Failed", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSaveVoiceOKDialog() {
        JOptionPane.showMessageDialog(view, "File (.wav) successfully saved to specified directory.",
                "Save .wav: Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSaveVoiceFailedDialog() {
        JOptionPane.showMessageDialog(view, "File (.wav) could not be saved. Try again without adding any extensions to the file name.",
                "Save .wav: Failed", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openLanguageIdentificationFailedDialog() {
        JOptionPane.showMessageDialog(view, "Could not identify the language of the text.\n",
                "Language Detection failed", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openCustomTranslationErrorDialog(TranslateObject translateObject) {
        JOptionPane.showMessageDialog(view, "Message Logged: \n"
                + translateObject.getMessage(),
                "Google Translate API: Message logged", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openVoiceGenerationFailedDialog() {
        JOptionPane.showMessageDialog(view, "Could not generate voice for the text.\n" +
                        "The voice generation service might be unavailble at this moment, or you might have made an " +
                        "invalid selection of the text to generate voice from.",
                "Voice Generation failed", JOptionPane.INFORMATION_MESSAGE);
    }
}
