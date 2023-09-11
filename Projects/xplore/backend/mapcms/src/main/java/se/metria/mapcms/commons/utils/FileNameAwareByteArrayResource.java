package se.metria.mapcms.commons.utils;

import org.springframework.core.io.ByteArrayResource;

/***
 * från Markkoll
 * https://stackoverflow.com/a/47941217
 * **/
public final class FileNameAwareByteArrayResource extends ByteArrayResource {
    private String fileName;

    public FileNameAwareByteArrayResource(byte[] byteArray, String fileName) {
        super(byteArray);
        this.fileName = fileName;
    }

    @Override
    public String getFilename() {
        return fileName;
    }
}
