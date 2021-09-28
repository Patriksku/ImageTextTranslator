package org.imagetextapp.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;


/**
 * Responsible for all GUI-elements. Provides an interface for controller-GUI manipulation with getters.
 */
public class View extends JFrame {
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
    private JButton saveTextOptButton;
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
    private JButton saveVoiceButton;
    private JLabel createdBy;
    private JLabel applicationTitle;
    private JPanel localLinkPanel;
    private JPanel textGenerationLanguagePanel;
    private JLabel textGenerationLanguageLabel;
    private JPanel generateTextPanel;
    private JPanel translatePanel;
    private JScrollPane scrollOptPane;
    private JPanel voiceSelectionPanel;
    private JPanel voicePlaybackPanel;
    private JLabel spaceLabel;
    private JPanel aboutInPanel;
    private JPanel aboutInnerPanel;

    /**
     * Constructor for initializing the GUI (View).
     *
     * @param title of the GUI (View) window.
     */
    public View(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);

        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        createdBy.setForeground(Color.BLUE.darker());
        createdBy.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Getters for GUI (View) components.
     */

    public JTextArea getTextArea() {
        return textArea;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JTextField getFileTextField() {
        return fileTextField;
    }

    public JButton getFileButton() {
        return fileButton;
    }

    public JRadioButton getLocalFileRadioButton() {
        return localFileRadioButton;
    }

    public JRadioButton getFileLinkRadioButton() {
        return fileLinkRadioButton;
    }

    public JComboBox<BoxItem> getTextGenerationLanguageBox() {
        return textGenerationLanguageBox;
    }

    public JCheckBox getUnknownCheckBox() {
        return unknownCheckBox;
    }

    public JButton getGenerateTextButton() {
        return generateTextButton;
    }

    public JRadioButton getStandardLayoutRadioButton() {
        return standardLayoutRadioButton;
    }

    public JRadioButton getRetainLayoutRadioButton() {
        return retainLayoutRadioButton;
    }

    public JButton getCopyTextButton() {
        return copyTextButton;
    }

    public JButton getSaveTextButton() {
        return saveTextButton;
    }

    public JLabel getStatusLabelOpt() {
        return statusLabelOpt;
    }

    public JRadioButton getRetainLayoutRadioButtonOpt() {
        return retainLayoutRadioButtonOpt;
    }

    public JRadioButton getStandardLayoutRadioButtonOpt() {
        return standardLayoutRadioButtonOpt;
    }

    public JButton getSaveTextOptButton() {
        return saveTextOptButton;
    }

    public JButton getCopyTextOptButton() {
        return copyTextOptButton;
    }

    public JComboBox<BoxItem> getTranslateFromBox() {
        return translateFromBox;
    }

    public JComboBox<BoxItem> getTranslateToBox() {
        return translateToBox;
    }

    public JCheckBox getUnknownOptCheckBox() {
        return unknownOptCheckBox;
    }

    public JTextArea getTextAreaOpt() {
        return textAreaOpt;
    }

    public JComboBox<BoxItem> getTextLanguageBox() {
        return textLanguageBox;
    }

    public JComboBox<BoxItem> getSelectVoiceBox() {
        return selectVoiceBox;
    }

    public JButton getTranslateTextButton() {
        return translateTextButton;
    }

    public JButton getIdentifyLanguageButton() {
        return identifyLanguageButton;
    }

    public JButton getGenerateVoiceButton() {
        return generateVoiceButton;
    }

    public JButton getPlayVoiceButton() {
        return playVoiceButton;
    }

    public JButton getStopVoiceButton() {
        return stopVoiceButton;
    }

    public JButton getSaveVoiceButton() {
        return saveVoiceButton;
    }

    public JLabel getCreatedBy() {
        return createdBy;
    }

}
