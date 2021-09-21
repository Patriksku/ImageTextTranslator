package org.imagetextapp.utility;

import java.nio.file.Path;

public class MimeManager {


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
