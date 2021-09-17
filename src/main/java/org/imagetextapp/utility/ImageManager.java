package org.imagetextapp.utility;

import org.imgscalr.Scalr.*;
import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.resize;

import java.awt.image.BufferedImage;

/**
 * WIP
 */
public class ImageManager {
    private BufferedImage image;

    // File instead to store name and handle conversions?
    public ImageManager(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void reduceImageSize(int reduction) {

            int width = image.getWidth();
            int height = image.getHeight();
            int targetWidth = width / reduction;
            int targetHeight = height / reduction;

            this.image = resize(image, Method.ULTRA_QUALITY, targetWidth, targetHeight, OP_ANTIALIAS);
        }

    public void saveImage(BufferedImage image, String destinationPath) {

    }


}
