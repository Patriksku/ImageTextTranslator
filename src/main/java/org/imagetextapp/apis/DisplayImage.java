package org.imagetextapp.apis;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisplayImage {

    public static void main(String avg[]) throws IOException
    {
        DisplayImage abc=new DisplayImage();
    }

    public DisplayImage() throws IOException
    {
        BufferedImage img= ImageIO.read(new File("C:\\Users\\fourseven\\Desktop\\notes\\Java\\projects\\bookpage.jpg"));
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200,300);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
