package org.imagetextapp.gui;

import org.imagetextapp.apis.ocr.OCRHandler;
import org.imagetextapp.apis.ocr.OCRObject;
import org.imagetextapp.utility.MimeManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

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

        fileButton.addActionListener(e -> {
            fileButtonAction();
        });

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

        generateTextButton.addActionListener(e -> {
            generateTextButtonAction();
        });
    }

    private void enableButtonGroups() {
        ButtonGroup localLinkGroup = new ButtonGroup();
        localLinkGroup.add(localFileRadioButton);
        localLinkGroup.add(fileLinkRadioButton);

        ButtonGroup generatedTextOptionsGroup = new ButtonGroup();
        generatedTextOptionsGroup.add(standardLayoutRadioButton);
        generatedTextOptionsGroup.add(retainLayoutRadioButton);
    }

    private void initStandardEnabling() {
        copyTextButton.setEnabled(false);
        saveTextButton.setEnabled(false);
    }

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

    private void generateTextButtonAction() {

        // If generate text for local file is selected
        if (localFileRadioButton.isSelected() && !fileTextField.getText().equals("")) {
            generateTextForLocalFile();

            // If generate text for image link is selected
        } else if (fileLinkRadioButton.isSelected() && !fileTextField.getText().equals("")) {
            generateTextForImageLink();
        }
    }

    private void generateTextForLocalFile() {
        resetStatusLabel();
        Path path = null;
        try {
            path = Path.of(userFilePathInput);
        } catch (InvalidPathException IPE) {
            openInvalidFileDialog();
            return;
        }
        selectedFile = path.toFile();
        long fileSizeBytes = selectedFile.length();
        long fileSizeKB = fileSizeBytes / 1024;

        if (fileSizeKB > FILE_SIZE_LIMIT) {
            openTooLargeFileSizeDialog();
        } else if (fileSizeKB == 0) {
            openInvalidFileDialog();
        } else {
            try {
                String mime = Files.probeContentType(path);
                MimeManager mimeManager = new MimeManager();

                if (mimeManager.validImageFile(mime)) {
                    TextGenerationBoxItem item = (TextGenerationBoxItem) textGenerationLanguageBox.getSelectedItem();
                    assert item != null;
                    String selectedLanguage = item.getValue();
                    Path finalPath = path;

                    setStatusLabel("Size of local file: " + fileSizeKB + "KB. Initializing text generation...");

                    EventQueue.invokeLater(() -> {
                        OCRHandler ocrHandler = new OCRHandler();
                        ocrObject = ocrHandler.uploadLocalImage(finalPath, selectedLanguage, unknownCheckBox.isSelected());
                        System.out.println(ocrObject.toString());
                        provideGeneratedText(ocrObject);

                        if (ocrObject.isErrorOnProcessing()) {
                            openCustomOCRErrorDialog(ocrObject);
                            resetStatusLabel();
                        } else {
                            setStatusLabel("Success. Processing time: " + ocrObject.getProcessingTime() + " ms.");
                        }
                    });

                } else {
                    openUnsupportedFileDialog();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void generateTextForImageLink() {
        resetStatusLabel();
        TextGenerationBoxItem item = (TextGenerationBoxItem) textGenerationLanguageBox.getSelectedItem();
        assert item != null;
        String selectedLanguage = item.getValue();

        setStatusLabel("Initializing text generation for the provided url-link...");

        EventQueue.invokeLater(() -> {
            OCRHandler ocrHandler = new OCRHandler();
            ocrObject = ocrHandler.uploadURLImage(userFilePathInput, selectedLanguage, unknownCheckBox.isSelected());
            System.out.println(ocrObject.toString());
            provideGeneratedText(ocrObject);

            if (ocrObject.isErrorOnProcessing()) {
                openCustomOCRErrorDialog(ocrObject);
                resetStatusLabel();
            } else {
                setStatusLabel("Success. Processing time: " + ocrObject.getProcessingTime() + " ms.");
            }
        });
    }

    private void provideGeneratedText(OCRObject ocrObject) {
        if (ocrObject != null) {
            if (standardLayoutRadioButton.isSelected()) {
                textArea.setText(ocrObject.getParsedTextClean());
            } else {
                textArea.setText(ocrObject.getParsedText());
            }
        }
    }

    private void setStatusLabel(String status) {
        statusLabel.setText(status);
    }

    private void resetStatusLabel() {
        statusLabel.setText("Status...");
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

    private void openCustomOCRErrorDialog(OCRObject ocrObject) {
        JOptionPane.showMessageDialog(this, ocrObject.getErrorMessage() + "\n" + "OCR Exit Code: "
                + ocrObject.getOcrExitCode() + "\n" + ocrObject.getErrorDetails(), "OCR API: Invalid request", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args) {
            GUI gui = new GUI("ImageTextSpeech");
    }
}
