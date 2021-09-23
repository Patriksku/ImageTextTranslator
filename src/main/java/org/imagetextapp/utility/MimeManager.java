package org.imagetextapp.utility;

import java.nio.file.Path;

/**
 * Manages functionality having to do with the MIME-types of files.
 */
public class MimeManager {

    /**
     * Checks if the specified file is of a valid MIME-type as specified by the OCR API.
     * @param mime of a file
     * @return true if the MIME-type is valid, else false.
     */
    public boolean validImageFile(String mime) {
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

    public String getFileExtension(String mime) {
        if (mime.equals("image/jpeg")) {
            return ".jpg";
        }
        if (mime.equals("image/gif")) {
            return ".gif";
        }
        if (mime.equals("application/pdf")) {
            return ".pdf";
        }
        if (mime.equals("image/png")) {
            return ".png";
        }
        if (mime.equals("image/bmp")) {
            return ".bnp";
        }
        if (mime.equals("image/tiff")) {
            return ".tif";
        }
        return "";
    }
}
