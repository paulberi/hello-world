package se.metria.markkoll.util;

import org.springframework.core.io.ByteArrayResource;

// https://stackoverflow.com/a/47941217
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
