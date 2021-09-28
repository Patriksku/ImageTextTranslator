package org.imagetextapp.utility;

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
}
