package org.imagetextapp.gui;

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
    private JComboBox<TextGenerationBoxItem> textGenerationLanguageBox;
    private JCheckBox unknownCheckBox;
    private JButton generateTextButton;
    private JPanel mainPanel;
    private JRadioButton standardLayoutRadioButton;
    private JRadioButton retainLayoutRadioButton;
    private JButton copyTextButton;
    private JButton saveTextButton;

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
                copyToClipboard()
        );

        saveTextButton.addActionListener(e ->
                saveLocalTxtFile()
        );
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
        copyTextButton.setEnabled(false);
        saveTextButton.setEnabled(false);
    }

    /**
     * Fills JComboBoxes with correct items needed for the functionality of the application.
     */
    private void fillDropDowns() {
        // Fill textGenerationLanguageBox with all supported languages.
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Arabic", "ara"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Bulgarian", "bul"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Chinese (Simplified)", "chs"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Chinese (Traditional)", "cht"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Croatian", "hrv"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Czech", "cze"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Danish", "dan"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Dutch", "dut"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("English", "eng"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Finnish", "fin"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("French", "fre"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("German", "ger"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Greek", "gre"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Hungarian", "hun"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Korean", "kor"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Italian", "ita"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Japanese", "jpn"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Polish", "pol"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Portuguese", "por"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Russian", "rus"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Slovenian", "slv"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Spanish", "spa"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Swedish", "swe"));
        textGenerationLanguageBox.addItem(new TextGenerationBoxItem("Turkish", "tur"));

        // Set the standard value to "English".
        textGenerationLanguageBox.setSelectedIndex(8);
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
                    TextGenerationBoxItem item = (TextGenerationBoxItem) textGenerationLanguageBox.getSelectedItem();
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
        TextGenerationBoxItem item = (TextGenerationBoxItem) textGenerationLanguageBox.getSelectedItem();
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
            } else {
                textArea.setText(ocrObject.getParsedText());
            }
        }
    }

    /**
     * Copies the contents of the JTextArea GUI-element to the clipboard.
     */
    private void copyToClipboard() {
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(textArea.getText()), null);
        setStatusLabel("Copied to clipboard.");
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

    private void resetStatusLabel() {
        statusLabel.setText("Status...");
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI("ImageTextSpeech");
        });
    }
}
