package se.metria.markkoll.util;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;

public class FilesUtil {
    public static String read(Resource resource) throws IOException {
        return new String(resource.getInputStream().readAllBytes(), Charset.defaultCharset());
    }
}
