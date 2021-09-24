package org.imagetextapp.gui;

import org.imagetextapp.apis.detectlanguage.DetectLanguageHandler;
import org.imagetextapp.apis.detectlanguage.DetectLanguageObject;
import org.imagetextapp.apis.ocr.OCRHandler;
import org.imagetextapp.apis.ocr.OCRObject;
import org.imagetextapp.utility.MimeManager;
import org.imagetextapp.utility.StringManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Manages the GUI of the application and handles user interactions.
 */
public class GUI extends JFrame {
    private JTabbedPane tabbedPane1;
    private JTextArea textArea;
    private JLabel statusLabel;
    private JScrollPane scrollPane;
    private JPanel textGenerationPanel;
    private JTextField fileTextField;
    private JButton fileButton;
    private JRadioButton localFileRadioButton;
    private JRadioButton fileLinkRadioButton;
    private JComboBox<BoxItem> textGenerationLanguageBox;
    private JCheckBox unknownCheckBox;
    private JButton generateTextButton;
    private JPanel mainPanel;
    private JRadioButton standardLayoutRadioButton;
    private JRadioButton retainLayoutRadioButton;
    private JButton copyTextButton;
    private JButton saveTextButton;
    private JPanel translationVoicePanel;
    private JPanel aboutPanel;
    private JLabel statusLabelOpt;
    private JRadioButton retainLayoutRadioButtonOpt;
    private JRadioButton standardLayoutRadioButtonOpt;
    private JButton saveVoiceButton;
    private JButton copyTextOptButton;
    private JComboBox<BoxItem> translateFromBox;
    private JComboBox<BoxItem> translateToBox;
    private JCheckBox unknownOptCheckBox;
    private JTextArea textAreaOpt;
    private JComboBox<BoxItem> textLanguageBox;
    private JComboBox<BoxItem> selectVoiceBox;
    private JButton translateTextButton;
    private JButton identifyLanguageButton;
    private JLabel translateFromLabel;
    private JLabel translateToLabel;
    private JButton generateVoiceButton;
    private JButton playVoiceButton;
    private JButton stopVoiceButton;

    private String userFilePathInput = "";
    private File selectedFile;
    private OCRObject ocrObject;
    private static final int FILE_SIZE_LIMIT = 1024;

    /**
     * Constructor for initializing the GUI.
     * @param title of the GUI window.
     */
    public GUI(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);

        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        enableListeners();
        enableButtonGroups();
        initStandardEnabling();
        fillDropDowns();
    }

    /**
     * Enables necessary listeners for various GUI-elements.
     */
    private void enableListeners() {
        fileTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                userFilePathInput = fileTextField.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                userFilePathInput = fileTextField.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                userFilePathInput = fileTextField.getText();
            }
        });

        fileButton.addActionListener(e ->
                fileButtonAction()
        );

        fileLinkRadioButton.addActionListener(e ->
                fileButton.setVisible(false)
        );

        localFileRadioButton.addActionListener(e ->
                fileButton.setVisible(true)
        );

        unknownCheckBox.addActionListener(e ->
                textGenerationLanguageBox.setEnabled(!unknownCheckBox.isSelected())
        );

        standardLayoutRadioButton.addActionListener(e ->
                provideGeneratedText(ocrObject)
        );

        retainLayoutRadioButton.addActionListener(e ->
                provideGeneratedText(ocrObject)
        );

        generateTextButton.addActionListener(e ->
                generateTextButtonAction()
        );

        copyTextButton.addActionListener(e ->
                copyToClipboard(copyTextButton)
        );

        saveTextButton.addActionListener(e ->
                saveLocalTxtFile()
        );

        unknownOptCheckBox.addActionListener(e ->
                translateFromBox.setEnabled(!unknownOptCheckBox.isSelected())
        );

        identifyLanguageButton.addActionListener(e ->
                identifyLanguage()
        );

        textLanguageBox.addActionListener(e ->
                handleSelectVoiceBox()
        );

        copyTextOptButton.addActionListener(e ->
                copyToClipboard(copyTextOptButton));
    }

    /**
     * Groups together radio buttons for correct GUI logic.
     */
    private void enableButtonGroups() {
        ButtonGroup localLinkGroup = new ButtonGroup();
        localLinkGroup.add(localFileRadioButton);
        localLinkGroup.add(fileLinkRadioButton);

        ButtonGroup generatedTextOptionsGroup = new ButtonGroup();
        generatedTextOptionsGroup.add(standardLayoutRadioButton);
        generatedTextOptionsGroup.add(retainLayoutRadioButton);
    }

    /**
     * Sets necessary GUI-elements to be disabled by standard, until some
     * user interaction enables the buttons and their corresponding functionalities.
     */
    private void initStandardEnabling() {
        // Text generation tab
        copyTextButton.setEnabled(false);
        saveTextButton.setEnabled(false);

        // Translation tab
        identifyLanguageButton.setEnabled(false);
        translateTextButton.setEnabled(false);

        generateVoiceButton.setEnabled(false);
        playVoiceButton.setEnabled(false);
        stopVoiceButton.setEnabled(false);

        copyTextOptButton.setEnabled(false);
        saveVoiceButton.setEnabled(false);
    }

    private void enableTranslationTab() {
        identifyLanguageButton.setEnabled(true);
        translateTextButton.setEnabled(true);

        generateVoiceButton.setEnabled(true);
        playVoiceButton.setEnabled(true);
        stopVoiceButton.setEnabled(true);

        copyTextOptButton.setEnabled(true);
        saveVoiceButton.setEnabled(true);
    }

    /**
     * Fills JComboBoxes with correct items needed for the functionality of the application.
     */
    private void fillDropDowns() {
        // Fill textGenerationLanguageBox with all supported languages.
        textGenerationLanguageBox.addItem(new BoxItem("Arabic", "ara"));
        textGenerationLanguageBox.addItem(new BoxItem("Bulgarian", "bul"));
        textGenerationLanguageBox.addItem(new BoxItem("Chinese (Simplified)", "chs"));
        textGenerationLanguageBox.addItem(new BoxItem("Chinese (Traditional)", "cht"));
        textGenerationLanguageBox.addItem(new BoxItem("Croatian", "hrv"));
        textGenerationLanguageBox.addItem(new BoxItem("Czech", "cze"));
        textGenerationLanguageBox.addItem(new BoxItem("Danish", "dan"));
        textGenerationLanguageBox.addItem(new BoxItem("Dutch", "dut"));
        textGenerationLanguageBox.addItem(new BoxItem("English", "eng"));
        textGenerationLanguageBox.addItem(new BoxItem("Finnish", "fin"));
        textGenerationLanguageBox.addItem(new BoxItem("French", "fre"));
        textGenerationLanguageBox.addItem(new BoxItem("German", "ger"));
        textGenerationLanguageBox.addItem(new BoxItem("Greek", "gre"));
        textGenerationLanguageBox.addItem(new BoxItem("Hungarian", "hun"));
        textGenerationLanguageBox.addItem(new BoxItem("Korean", "kor"));
        textGenerationLanguageBox.addItem(new BoxItem("Italian", "ita"));
        textGenerationLanguageBox.addItem(new BoxItem("Japanese", "jpn"));
        textGenerationLanguageBox.addItem(new BoxItem("Polish", "pol"));
        textGenerationLanguageBox.addItem(new BoxItem("Portuguese", "por"));
        textGenerationLanguageBox.addItem(new BoxItem("Russian", "rus"));
        textGenerationLanguageBox.addItem(new BoxItem("Slovenian", "slv"));
        textGenerationLanguageBox.addItem(new BoxItem("Spanish", "spa"));
        textGenerationLanguageBox.addItem(new BoxItem("Swedish", "swe"));
        textGenerationLanguageBox.addItem(new BoxItem("Turkish", "tur"));

        // Set the standard value to "English".
        textGenerationLanguageBox.setSelectedIndex(8);

        // Fill translateBoxes with all supported languages.
        translateFromBox.addItem(new BoxItem("Arabic", "ar"));
        translateToBox.addItem(new BoxItem("Arabic", "ar"));
        translateFromBox.addItem(new BoxItem("Bulgarian", "bg"));
        translateToBox.addItem(new BoxItem("Bulgarian", "bg"));
        translateFromBox.addItem(new BoxItem("Chinese (Simplified)", "zh-CN"));
        translateToBox.addItem(new BoxItem("Chinese (Simplified)", "zh-CN"));
        translateFromBox.addItem(new BoxItem("Chinese (Traditional)", "zh-TW"));
        translateToBox.addItem(new BoxItem("Chinese (Traditional)", "zh-TW"));
        translateFromBox.addItem(new BoxItem("Croatian", "hr"));
        translateToBox.addItem(new BoxItem("Croatian", "hr"));
        translateFromBox.addItem(new BoxItem("Czech", "cs"));
        translateToBox.addItem(new BoxItem("Czech", "cs"));
        translateFromBox.addItem(new BoxItem("Danish", "da"));
        translateToBox.addItem(new BoxItem("Danish", "da"));
        translateFromBox.addItem(new BoxItem("Dutch", "nl"));
        translateToBox.addItem(new BoxItem("Dutch", "nl"));
        translateFromBox.addItem(new BoxItem("English", "en"));
        translateToBox.addItem(new BoxItem("English", "en"));
        translateFromBox.addItem(new BoxItem("Finnish", "fi"));
        translateToBox.addItem(new BoxItem("Finnish", "fi"));
        translateFromBox.addItem(new BoxItem("French", "fr"));
        translateToBox.addItem(new BoxItem("French", "fr"));
        translateFromBox.addItem(new BoxItem("German", "de"));
        translateToBox.addItem(new BoxItem("German", "de"));
        translateFromBox.addItem(new BoxItem("Greek", "el"));
        translateToBox.addItem(new BoxItem("Greek", "el"));
        translateFromBox.addItem(new BoxItem("Hungarian", "hu"));
        translateToBox.addItem(new BoxItem("Hungarian", "hu"));
        translateFromBox.addItem(new BoxItem("Korean", "ko"));
        translateToBox.addItem(new BoxItem("Korean", "ko"));
        translateFromBox.addItem(new BoxItem("Italian", "it"));
        translateToBox.addItem(new BoxItem("Italian", "it"));
        translateFromBox.addItem(new BoxItem("Japanese", "ja"));
        translateToBox.addItem(new BoxItem("Japanese", "ja"));
        translateFromBox.addItem(new BoxItem("Polish", "pl"));
        translateToBox.addItem(new BoxItem("Polish", "pl"));
        translateFromBox.addItem(new BoxItem("Portuguese", "pt"));
        translateToBox.addItem(new BoxItem("Portuguese", "pt"));
        translateFromBox.addItem(new BoxItem("Russian", "ru"));
        translateToBox.addItem(new BoxItem("Russian", "ru"));
        translateFromBox.addItem(new BoxItem("Slovenian", "sl"));
        translateToBox.addItem(new BoxItem("Slovenian", "sl"));
        translateFromBox.addItem(new BoxItem("Spanish", "es"));
        translateToBox.addItem(new BoxItem("Spanish", "es"));
        translateFromBox.addItem(new BoxItem("Swedish", "sv"));
        translateToBox.addItem(new BoxItem("Swedish", "sv"));
        translateFromBox.addItem(new BoxItem("Turkish", "tr"));
        translateToBox.addItem(new BoxItem("Turkish", "tr"));

        // Fill TextLanguageBoxes with all supported languages.
        textLanguageBox.addItem(new BoxItem("Arabic (Egypt)", "ar-eg"));
        textLanguageBox.addItem(new BoxItem("Arabic (Saudi Arabia)", "ar-sa"));
        textLanguageBox.addItem(new BoxItem("Bulgarian", "bg-bg"));
        textLanguageBox.addItem(new BoxItem("Chinese (Simplified)", "zh-cn"));
        textLanguageBox.addItem(new BoxItem("Chinese (Traditional)", "zh-tw"));
        textLanguageBox.addItem(new BoxItem("Croatian", "hr-hr"));
        textLanguageBox.addItem(new BoxItem("Czech", "cs-cz"));
        textLanguageBox.addItem(new BoxItem("Danish", "da-dk"));
        textLanguageBox.addItem(new BoxItem("Dutch (Belgium)", "nl-be"));
        textLanguageBox.addItem(new BoxItem("Dutch (Netherlands)", "nl-nl"));
        textLanguageBox.addItem(new BoxItem("English (Australia)", "en-au"));
        textLanguageBox.addItem(new BoxItem("English (Canada)", "en-ca"));
        textLanguageBox.addItem(new BoxItem("English (Great Britain)", "en-gb"));
        textLanguageBox.addItem(new BoxItem("English (India)", "en-in"));
        textLanguageBox.addItem(new BoxItem("English (Ireland)", "en-ie"));
        textLanguageBox.addItem(new BoxItem("English (United States)", "en-us"));
        textLanguageBox.addItem(new BoxItem("Finnish", "fi-fi"));
        textLanguageBox.addItem(new BoxItem("French (Canada)", "fr-ca"));
        textLanguageBox.addItem(new BoxItem("French (France)", "fr-fr"));
        textLanguageBox.addItem(new BoxItem("French (Switzerland)", "fr-ch"));
        textLanguageBox.addItem(new BoxItem("German (Austria)", "de-at"));
        textLanguageBox.addItem(new BoxItem("German (Germany)", "de-de"));
        textLanguageBox.addItem(new BoxItem("German (Switzerland)", "de-ch"));
        textLanguageBox.addItem(new BoxItem("Greek", "el-gr"));
        textLanguageBox.addItem(new BoxItem("Hungarian", "hu-hu"));
        textLanguageBox.addItem(new BoxItem("Korean", "ko-kr"));
        textLanguageBox.addItem(new BoxItem("Italian", "it-it"));
        textLanguageBox.addItem(new BoxItem("Japanese", "ja-jp"));
        textLanguageBox.addItem(new BoxItem("Polish", "pl-pl"));
        textLanguageBox.addItem(new BoxItem("Portuguese (Brazil)", "pt-br"));
        textLanguageBox.addItem(new BoxItem("Portuguese (Portugal)", "pt-pt"));
        textLanguageBox.addItem(new BoxItem("Russian", "ru-ru"));
        textLanguageBox.addItem(new BoxItem("Slovenian", "sl-si"));
        textLanguageBox.addItem(new BoxItem("Spanish (Mexico)", "es-mx"));
        textLanguageBox.addItem(new BoxItem("Spanish (Spain)", "es-es"));
        textLanguageBox.addItem(new BoxItem("Swedish", "sv-se"));
        textLanguageBox.addItem(new BoxItem("Turkish", "tr-tr"));
    }

    /**
     * Handles the user-specified local file that is to be processed for text generation.
     */
    private void fileButtonAction() {
        JFileChooser fileChooser = new JFileChooser();
        int chosenFile = fileChooser.showOpenDialog(this);

        if (chosenFile == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            try {
                String mime = Files.probeContentType(selectedFile.toPath());
                MimeManager mimeManager = new MimeManager();

                if (mimeManager.validImageFile(mime)) {
                    fileTextField.setText(selectedFile.toString());
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
        if (localFileRadioButton.isSelected() && !fileTextField.getText().equals("")) {
            generateTextForLocalFile();

            // If generate text for image link is selected
        } else if (fileLinkRadioButton.isSelected() && !fileTextField.getText().equals("")) {
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
            path = Path.of(userFilePathInput);
        } catch (InvalidPathException IPE) {
            openInvalidFileDialog();
            return;
        }

        // Assigns a valid file-directory.
        selectedFile = path.toFile();
        long fileSizeBytes = selectedFile.length();
        long fileSizeKB = fileSizeBytes / 1024;

        // Checks if selected file is within the file size limit (1 MB).
        if (fileSizeKB > FILE_SIZE_LIMIT) {
            openTooLargeFileSizeDialog();
        } else if (fileSizeKB == 0) {
            openInvalidFileDialog();
        } else {
            try {
                String mime = Files.probeContentType(path);
                MimeManager mimeManager = new MimeManager();

                // Checks if the content type of the selected file is supported for text generation.
                if (mimeManager.validImageFile(mime)) {
                    BoxItem item = (BoxItem) textGenerationLanguageBox.getSelectedItem();
                    assert item != null;
                    String selectedLanguage = item.getValue();

                    setStatusLabel("Size of local file: " + fileSizeKB + "KB. Initializing text generation...");

                    // Thread for handling the server communication and safely updating GUI throughout the process.
                    SwingWorker<OCRObject, String> worker = new SwingWorker<>() {

                        @Override
                        protected OCRObject doInBackground() {
                            OCRHandler ocrHandler = new OCRHandler();
                            ocrObject = ocrHandler.uploadLocalImage(path, selectedLanguage, unknownCheckBox.isSelected());
                            publish();

                            return ocrObject;
                        }

                        @Override
                        protected void process(List<String> chunks) {
                            setStatusLabel("Processing the file...");
                        }

                        @Override
                        protected void done() {
                            try {
                                OCRObject ocrObject = get();
                                System.out.println(ocrObject.toString());
                                provideGeneratedText(ocrObject);

                                if (ocrObject.isErrorOnProcessing()) {
                                    deactivateCopySaveButtons();
                                    openCustomOCRErrorDialog(ocrObject);
                                    resetStatusLabel();
                                } else {
                                    activateCopySaveButtons();
                                    enableTranslationTab();
                                    setStatusLabel("Success. Processing time: " + ocrObject.getProcessingTime() + " ms.");
                                }

                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    // Start the thread.
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
        BoxItem item = (BoxItem) textGenerationLanguageBox.getSelectedItem();
        assert item != null;
        String selectedLanguage = item.getValue();

        setStatusLabel("Initializing text generation for the provided url-link...");

        // Thread for handling the server communication and safely updating GUI throughout the process.
        SwingWorker<OCRObject, String> worker = new SwingWorker<>() {
            @Override
            protected OCRObject doInBackground() {
                OCRHandler ocrHandler = new OCRHandler();
                ocrObject = ocrHandler.uploadURLImage(userFilePathInput, selectedLanguage, unknownCheckBox.isSelected());
                publish();

                return ocrObject;
            }

            @Override
            protected void process(List<String> chunks) {
                setStatusLabel("Processing the file...");
            }

            @Override
            protected void done() {
                try {
                    OCRObject ocrObject = get();
                    System.out.println(ocrObject.toString());
                    provideGeneratedText(ocrObject);

                    if (ocrObject.isErrorOnProcessing()) {
                        openCustomOCRErrorDialog(ocrObject);
                        resetStatusLabel();
                    } else {
                        enableTranslationTab();
                        setStatusLabel("Success. Processing time: " + ocrObject.getProcessingTime() + " ms.");
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
            if (standardLayoutRadioButton.isSelected()) {
                textArea.setText(ocrObject.getParsedTextClean());
                textAreaOpt.setText(ocrObject.getParsedTextClean());
            } else {
                textArea.setText(ocrObject.getParsedText());
                textAreaOpt.setText(ocrObject.getParsedText());
            }
        }
    }

    /**
     * Copies the contents of the JTextArea GUI-element in the current tab to the clipboard.
     */
    private void copyToClipboard(JButton button) {
        if (button.getName().equals("copyTextButton")) {
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(textArea.getText()), null);
            setStatusLabel("Copied to clipboard.");

        } else {
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(textAreaOpt.getText()), null);
            setStatusOptLabel("Copied to clipboard.");
        }
    }

    /**
     * Saves the contents of the JTextArea GUI-element to a .txt file locally, bounded by the
     * naming and directory choice of the user.
     */
    private void saveLocalTxtFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);

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
                clone.setText(stringManager.removeExtraNewLines(textArea.getText()));
                clone.write(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                openSaveOKDialog();
            } catch (IOException e) {
                e.printStackTrace();
                openSaveFailedDialog();
            }
        }
    }

    private void setStatusLabel(String status) {
        statusLabel.setText(status);
    }

    private void setStatusOptLabel(String status) {
        statusLabelOpt.setText(status);
    }

    private void resetStatusLabel() {
        statusLabel.setText("Status...");
    }

    private void resetStatusOptLabel() {
        statusLabelOpt.setText("Status...");
    }

    /**
     * Activate the "Copy to Clipboard" and "Save to .txt" functionalities when these are ready.
     */
    private void activateCopySaveButtons() {
        copyTextButton.setEnabled(true);
        saveTextButton.setEnabled(true);
    }

    /**
     * De-activates the "Copy to Clipboard" and "Save to .txt" functionalities when these are not ready.
     */
    private void deactivateCopySaveButtons() {
        copyTextButton.setEnabled(false);
        saveTextButton.setEnabled(false);
    }

    private void handleSelectVoiceBox() {
        BoxItem boxItem = (BoxItem) textLanguageBox.getSelectedItem();
        assert boxItem != null;
        populateSelectVoiceBox(boxItem.getKey());
    }

    private void populateSelectVoiceBox(String lang) {
        switch (lang) {
            case "Arabic (Egypt)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Oda (Female)", "Oda"));
            }
            case "Arabic (Saudi Arabia)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Salim (Male)", "Salim"));
            }
            case "Bulgarian" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Dimo (Male)", "Dimo"));
            }
            case "Chinese (Simplified)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Luli (Female)", "Luli"));
                selectVoiceBox.addItem(new BoxItem("Shu (Female)", "Shu"));
                selectVoiceBox.addItem(new BoxItem("Chow (Female)", "Chow"));
                selectVoiceBox.addItem(new BoxItem("Wang (Male)", "Wang"));
            }
            case "Chinese (Traditional)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Akemi (Female)", "Akemi"));
                selectVoiceBox.addItem(new BoxItem("Lin (Female)", "Lin"));
                selectVoiceBox.addItem(new BoxItem("Lee (Male)", "Lee"));
            }
            case "Croatian" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Nikola (Male)", "Nikola"));
            }
            case "Czech" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Josef (Male)", "Josef"));
            }
            case "Danish" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Freja (Female)", "Freja"));
            }
            case "Dutch (Belgium)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Daan (Male)", "Daan"));
            }
            case "Dutch (Netherlands)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Lotte (Female)", "Lotte"));
                selectVoiceBox.addItem(new BoxItem("Bram (Male)", "Bram"));
            }
            case "English (Australia)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Zoe (Female)", "Zoe"));
                selectVoiceBox.addItem(new BoxItem("Isla (Female)", "Isla"));
                selectVoiceBox.addItem(new BoxItem("Evie (Female)", "Evie"));
                selectVoiceBox.addItem(new BoxItem("Jack (Male)", "Jack"));
            }
            case "English (Canada)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Rose (Female)", "Rose"));
                selectVoiceBox.addItem(new BoxItem("Clara (Female)", "Clara"));
                selectVoiceBox.addItem(new BoxItem("Emma (Female)", "Emma"));
                selectVoiceBox.addItem(new BoxItem("Mason (Male)", "Mason"));
            }
            case "English (Great Britain)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Alice (Female)", "Alice"));
                selectVoiceBox.addItem(new BoxItem("Nancy (Female)", "Nancy"));
                selectVoiceBox.addItem(new BoxItem("Lily (Female)", "Lily"));
                selectVoiceBox.addItem(new BoxItem("Harry (Male)", "Harry"));
            }
            case "English (India)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Eka (Female)", "Eka"));
                selectVoiceBox.addItem(new BoxItem("Jai (Female)", "Jai"));
                selectVoiceBox.addItem(new BoxItem("Ajit (Male)", "Ajit"));
            }
            case "English (Ireland)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Oran (Male)", "Oran"));
            }
            case "English (United States)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Linda (Female)", "Linda"));
                selectVoiceBox.addItem(new BoxItem("Amy (Female)", "Amy"));
                selectVoiceBox.addItem(new BoxItem("Mary (Female)", "Mary"));
                selectVoiceBox.addItem(new BoxItem("John (Male)", "John"));
                selectVoiceBox.addItem(new BoxItem("Mike (Male)", "Mike"));
            }
            case "Finnish" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Aada (Female)", "Aada"));
            }
            case "French (Canada)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Emile (Female)", "Emile"));
                selectVoiceBox.addItem(new BoxItem("Olivia (Female)", "Olivia"));
                selectVoiceBox.addItem(new BoxItem("Logan (Female)", "Logan"));
                selectVoiceBox.addItem(new BoxItem("Felix (Male)", "Felix"));
            }
            case "French (France)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Bette (Female)", "Bette"));
                selectVoiceBox.addItem(new BoxItem("Iva (Female)", "Iva"));
                selectVoiceBox.addItem(new BoxItem("Zola (Female)", "Zola"));
                selectVoiceBox.addItem(new BoxItem("Axel (Male)", "Axel"));
            }
            case "French (Switzerland)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Theo (Male)", "Theo"));
            }
            case "German (Austria)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Lukas (Male)", "Lukas"));
            }
            case "German (Germany)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Hanna (Female)", "Hanna"));
                selectVoiceBox.addItem(new BoxItem("Lina (Female)", "Lina"));
                selectVoiceBox.addItem(new BoxItem("Jonas (Male)", "Jonas"));
            }
            case "German (Switzerland)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Tim (Male)", "Tim"));
            }
            case "Greek" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Neo (Male)", "Neo"));
            }
            case "Hungarian" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Mate (Male)", "Mate"));
            }
            case "Korean" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Nari (Female)", "Nari"));
            }
            case "Italian" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Bria (Female)", "Bria"));
                selectVoiceBox.addItem(new BoxItem("Mia (Female)", "Mia"));
                selectVoiceBox.addItem(new BoxItem("Pietro (Male)", "Pietro"));
            }
            case "Japanese" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Hina (Female)", "Hina"));
                selectVoiceBox.addItem(new BoxItem("Airi (Female)", "Airi"));
                selectVoiceBox.addItem(new BoxItem("Fumi (Female)", "Fumi"));
                selectVoiceBox.addItem(new BoxItem("Akira (Male)", "Akira"));
            }
            case "Polish" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Julia (Female)", "Julia"));
                selectVoiceBox.addItem(new BoxItem("Jan (Male)", "Jan"));
            }
            case "Portuguese (Brazil)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Marcia (Female)", "Marcia"));
                selectVoiceBox.addItem(new BoxItem("Ligia (Female)", "Ligia"));
                selectVoiceBox.addItem(new BoxItem("Yara (Female)", "Yara"));
                selectVoiceBox.addItem(new BoxItem("Dinis (Male)", "Dinis"));
            }
            case "Portuguese (Portugal)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Leonor (Female)", "Leonor"));
            }
            case "Russian" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Olga (Female)", "Olga"));
                selectVoiceBox.addItem(new BoxItem("Marina (Female)", "Marina"));
                selectVoiceBox.addItem(new BoxItem("Peter (Male)", "Peter"));
            }
            case "Slovenian" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Vid (Male)", "Vid"));
            }
            case "Spanish (Mexico)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Juana (Female)", "Juana"));
                selectVoiceBox.addItem(new BoxItem("Silvia (Female)", "Silvia"));
                selectVoiceBox.addItem(new BoxItem("Teresa (Female)", "Teresa"));
                selectVoiceBox.addItem(new BoxItem("Jose (Male)", "Jose"));
            }
            case "Spanish (Spain)" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Camila (Female)", "Camila"));
                selectVoiceBox.addItem(new BoxItem("Sofia (Female)", "Sofia"));
                selectVoiceBox.addItem(new BoxItem("Luna (Female)", "Luna"));
                selectVoiceBox.addItem(new BoxItem("Diego (Male)", "Diego"));
            }
            case "Swedish" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Molly (Female)", "Molly"));
                selectVoiceBox.addItem(new BoxItem("Hugo (Male)", "Hugo"));
            }
            case "Turkish" -> {
                selectVoiceBox.removeAllItems();
                selectVoiceBox.addItem(new BoxItem("Omer (Male)", "Omer"));
            }
            default -> {
            }
        }
    }

    private void identifyLanguage() {
        // Verify that textAreaOpt is not empty.
        if (!textAreaOpt.getText().equals("")) {
            DetectLanguageHandler detectLanguageHandler = new DetectLanguageHandler();
            DetectLanguageObject detectLanguageObject = detectLanguageHandler.identifyLanguage(textAreaOpt.getText());

            // If language detection errorred on processing.
            if (detectLanguageObject.isErrorOnProcessing()) {
                openLanguageIdentificationFailedDialog();
            } else {
                String languageResult = detectLanguageObject.getLanguage();

                // If language is unsupported.
                if (languageResult.equals("Unsupported language.")) {
                    setStatusOptLabel("This language is not currently supported for identification.");
                } else {
                    setStatusOptLabel("Language identified as: " + languageResult + ". Confidence for correct " +
                            "identification: " + detectLanguageObject.getReliable());
                }
            }
        }
    }

    private void openUnsupportedFileDialog() {
        JOptionPane.showMessageDialog(this, "Please select a valid file. " +
                "Supported files: \n" +
                "JPG, GIF, PDF, PNG, BMP or TIFF.", "Unsupported file type detected", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openInvalidFileDialog() {
        JOptionPane.showMessageDialog(this, "Please select a valid file. " +
                "Make sure that the file exists at the specified location.", "Invalid file detected", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openTooLargeFileSizeDialog() {
        JOptionPane.showMessageDialog(this, "This file exceeds the 1 MB (1024 KB) limit for " +
                "generating text from file. \nPlease choose a smaller file.", "Too large file", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSaveOKDialog() {
        JOptionPane.showMessageDialog(this, "File (.txt) successfully saved to specified directory.",
                "Save: Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSaveFailedDialog() {
        JOptionPane.showMessageDialog(this, "File (.txt) could not be saved. Try again without adding any extensions to the file name.",
                "Save: Failed", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openCustomOCRErrorDialog(OCRObject ocrObject) {
        JOptionPane.showMessageDialog(this, ocrObject.getErrorMessage() + "\n" + "OCR Exit Code: "
                + ocrObject.getOcrExitCode() + "\n" + ocrObject.getErrorDetails(), "OCR API: Processing error", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openLanguageIdentificationFailedDialog() {
        JOptionPane.showMessageDialog(this, "Could not identify the language of the text.\n",
                "Language Detection failed", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI("ImageTextSpeech");
        });
    }
}
