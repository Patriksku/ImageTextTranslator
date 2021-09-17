package org.imagetextapp.utility;

import org.imagetextapp.apis.detectlanguage.DetectLanguageHandler;
import org.imagetextapp.apis.ocr.OCRHandler;
import org.imagetextapp.apis.ocr.OCRObject;
import org.imagetextapp.apis.translate.TranslateHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;

/**
 * Builds a valid multipart/form-data POST query which can be handled by Java's HttpClient.
 * From: https://stackoverflow.com/questions/46392160/java-9-httpclient-send-a-multipart-form-data-request @ittupelo
 */
public class MultiPartBody {
    private List<PartsSpecification> partsSpecificationList = new ArrayList<>();
    private String boundary = UUID.randomUUID().toString();

    public HttpRequest.BodyPublisher build() {
        if (partsSpecificationList.size() == 0) {
            throw new IllegalStateException("Must have at least one part to build multipart message.");
        }
        addFinalBoundaryPart();
        return HttpRequest.BodyPublishers.ofByteArrays(PartsIterator::new);
    }

    public String getBoundary() {
        return boundary;
    }

    public MultiPartBody addPart(String name, String value) {
        PartsSpecification newPart = new PartsSpecification();
        newPart.type = PartsSpecification.TYPE.STRING;
        newPart.name = name;
        newPart.value = value;
        partsSpecificationList.add(newPart);
        return this;
    }

    public MultiPartBody addPart(String name, Path value) {
        PartsSpecification newPart = new PartsSpecification();
        newPart.type = PartsSpecification.TYPE.FILE;
        newPart.name = name;
        newPart.path = value;
        partsSpecificationList.add(newPart);
        return this;
    }

    public MultiPartBody addPart(String name, Supplier<InputStream> value, String filename, String contentType) {
        PartsSpecification newPart = new PartsSpecification();
        newPart.type = PartsSpecification.TYPE.STREAM;
        newPart.name = name;
        newPart.stream = value;
        newPart.filename = filename;
        newPart.contentType = contentType;
        partsSpecificationList.add(newPart);
        return this;
    }

    private void addFinalBoundaryPart() {
        PartsSpecification newPart = new PartsSpecification();
        newPart.type = PartsSpecification.TYPE.FINAL_BOUNDARY;
        newPart.value = "--" + boundary + "--";
        partsSpecificationList.add(newPart);
    }

    static class PartsSpecification {

        public enum TYPE {
            STRING, FILE, STREAM, FINAL_BOUNDARY
        }

        PartsSpecification.TYPE type;
        String name;
        String value;
        Path path;
        Supplier<InputStream> stream;
        String filename;
        String contentType;

    }

    class PartsIterator implements Iterator<byte[]> {

        private Iterator<PartsSpecification> iter;
        private InputStream currentFileInput;

        private boolean done;
        private byte[] next;

        PartsIterator() {
            iter = partsSpecificationList.iterator();
        }

        @Override
        public boolean hasNext() {
            if (done) return false;
            if (next != null) return true;
            try {
                next = computeNext();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            if (next == null) {
                done = true;
                return false;
            }
            return true;
        }

        @Override
        public byte[] next() {
            if (!hasNext()) throw new NoSuchElementException();
            byte[] res = next;
            next = null;
            return res;
        }

        private byte[] computeNext() throws IOException {
            if (currentFileInput == null) {
                if (!iter.hasNext()) return null;
                PartsSpecification nextPart = iter.next();
                if (PartsSpecification.TYPE.STRING.equals(nextPart.type)) {
                    String part =
                            "--" + boundary + "\r\n" +
                                    "Content-Disposition: form-data; name=" + nextPart.name + "\r\n" +
                                    "Content-Type: text/plain; charset=UTF-8\r\n\r\n" +
                                    nextPart.value + "\r\n";
                    return part.getBytes(StandardCharsets.UTF_8);
                }
                if (PartsSpecification.TYPE.FINAL_BOUNDARY.equals(nextPart.type)) {
                    return nextPart.value.getBytes(StandardCharsets.UTF_8);
                }
                String filename;
                String contentType;
                if (PartsSpecification.TYPE.FILE.equals(nextPart.type)) {
                    Path path = nextPart.path;
                    filename = path.getFileName().toString();
                    contentType = Files.probeContentType(path);
                    if (contentType == null) contentType = "application/octet-stream";
                    currentFileInput = Files.newInputStream(path);
                } else {
                    filename = nextPart.filename;
                    contentType = nextPart.contentType;
                    if (contentType == null) contentType = "application/octet-stream";
                    currentFileInput = nextPart.stream.get();
                }
                String partHeader =
                        "--" + boundary + "\r\n" +
                                "Content-Disposition: form-data; name=" + nextPart.name + "; filename=" + filename + "\r\n" +
                                "Content-Type: " + contentType + "\r\n\r\n";
                return partHeader.getBytes(StandardCharsets.UTF_8);
            } else {
                byte[] buf = new byte[8192];
                int r = currentFileInput.read(buf);
                if (r > 0) {
                    byte[] actualBytes = new byte[r];
                    System.arraycopy(buf, 0, actualBytes, 0, r);
                    return actualBytes;
                } else {
                    currentFileInput.close();
                    currentFileInput = null;
                    return "\r\n".getBytes(StandardCharsets.UTF_8);
                }
            }
        }
    }

    public static void main(String[] args) {

        Path localFile = Paths.get("C:\\Users\\fourseven\\Desktop\\notes\\Java\\projects\\bookpage.jpg");
        String urlFile = "https://marketplace.canva.com/EAEjCdGx_iA/1/0/1131w/canva-pizza-menu-LjjRAs0R_8A.jpg";

        Path wrongFile = Paths.get("C:\\Users\\fourseven\\Desktop\\notes\\Java\\projects\\notImage.txt");
        String wrongUrl = "http://wallpaperswide.com/new_york_city_buildings-wallpapers.html";

        Path largeLocalFile = Paths.get("C:\\Users\\fourseven\\Desktop\\notes\\Java\\projects\\large.png");
        String largeUrlFile = "https://i.redd.it/e440eaxet2f11.png";

        String OCR_URL = "https://api.ocr.space/parse/image";
        String DETECT_LANGUAGE_URL = "https://ws.detectlanguage.com/0.2/detect";


//        System.getProperty("\njava.class.path");

//        OCRHandler ocrHandler = new OCRHandler();
//        ocrHandler.uploadLocalImage(localFile, "eng", false);
//        OCRObject ocrObject = ocrHandler.getOcrObject();
//
//        DetectLanguageHandler detectLanguageHandler = new DetectLanguageHandler();
//
//        System.out.println("Detecting language with clean String: \n");
//        detectLanguageHandler.identifyLanguage(ocrObject.getParsedTextClean());

        TranslateHandler translateHandler = new TranslateHandler();
        translateHandler.translateText("Hello, world!", "es");
    }
}