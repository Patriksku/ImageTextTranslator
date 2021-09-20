package org.imagetextapp.gui;

import org.imagetextapp.apis.ocr.OCRHandler;
import org.imagetextapp.apis.ocr.OCRObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    private JComboBox textLanguageBox;
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
            JFileChooser fileChooser = new JFileChooser();
            int chosenFile = fileChooser.showOpenDialog(this);

            if (chosenFile == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();

                try {
                    String mime = Files.probeContentType(selectedFile.toPath());
//                    System.out.println(mime);

                    if (validImageFile(mime)) {
                        fileTextField.setText(selectedFile.toString());
                    }
                    else {
                        openUnsupportedFileDialog();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        fileLinkRadioButton.addActionListener(e -> {
            fileButton.setVisible(false);
        });

        localFileRadioButton.addActionListener(e -> {
            fileButton.setVisible(true);
        });

        unknownCheckBox.addActionListener(e -> {
            textLanguageBox.setEnabled(!unknownCheckBox.isSelected());
        });

        standardLayoutRadioButton.addActionListener(e -> {
            provideGeneratedText(ocrObject);
        });

        retainLayoutRadioButton.addActionListener(e -> {
            provideGeneratedText(ocrObject);
        });

        generateTextButton.addActionListener(e -> {
            // Empty
            if (localFileRadioButton.isEnabled() && !fileTextField.getText().equals("")) {
                Path path = Path.of(userFilePathInput);
                selectedFile = path.toFile();
                System.out.println("Size of local file: " + selectedFile.length());

                try {
                    String mime = Files.probeContentType(path);

                    if (validImageFile(mime)) {
                        OCRHandler ocrHandler = new OCRHandler();
                        ocrHandler.uploadLocalImage(path, "eng", unknownCheckBox.isSelected());

                        ocrObject = ocrHandler.getOcrObject();
                        provideGeneratedText(ocrObject);
                    }
                    else {
                        openUnsupportedFileDialog();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
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

    private boolean validImageFile(String mime) {
        if (mime != null) {
            return mime.equals("image/jpeg") ||
                    mime.equals("image/gif") ||
                    mime.equals("application/pdf") ||
                    mime.equals("image/png") ||
                    mime.equals("image/bmp") ||
                    mime.equals("image/tiff");
        }
        return false;
    }

    private void openUnsupportedFileDialog() {
        JOptionPane.showMessageDialog(this, "Please select a valid file. " +
                "Supported files: \n" +
                "JPG, GIF, PDF, PNG, BMP or TIFF.", "Unsupported file type detected", JOptionPane.INFORMATION_MESSAGE);
    }

    private void provideGeneratedText(OCRObject ocrObject) {
        if (ocrObject != null) {
            if (standardLayoutRadioButton.isEnabled()) {
                textArea.setText(ocrObject.getParsedText());
                System.out.println("Unclean: \n" + ocrObject.getParsedText());
            } else {
                textArea.setText(ocrObject.getParsedTextClean());
                System.out.println("Clean: \n" + ocrObject.getParsedTextClean());
            }
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args) {
        GUI gui = new GUI("ImageTextSpeech");

    }
}
