package se.metria.markkoll.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileSystemUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class FileUtil {

    /**
     * Skapa en temporär folder med prefixet "tmp_".
     */
    public static Path createTempFolder() throws IOException {
        return Files.createTempDirectory("tmp_");
    }

    /**
     * Ta bort en temporär folder med prefixet "tmp_" och allt dess innehåll.
     */
    public static boolean deleteTempFolder(Path tempFolderPath) {
        if(tempFolderPath == null || !tempFolderPath.toString().contains("tmp_")) {
            return false;
        }
        return FileSystemUtils.deleteRecursively(tempFolderPath.toFile());
    }

    /**
     * Packa upp zip-fil och returnera ett set med filer.
     */
    public static Set<File> unzipFiles(Path destDir, InputStream inputStream) throws IOException {
        Set<File> files = new HashSet<>();
        // Det är problem att läsa in ZIP-filer med åäö om man inte specar charset.
        // Cp437 ska vara default enligt denna kommentar på SO:
        // https://stackoverflow.com/questions/11276343/zipinputstreaminputstream-charset-decodes-zipentry-file-name-falsely#comment117650569_11276457
        // men det verkar den inte vara. Sannolikt en bug i OpenJDK.
        // Så vi hårdkodar in det för stunden och hoppas att det inte kommer filer med annan encoding...
        ZipInputStream zipInputStream = new ZipInputStream(inputStream, Charset.forName("Cp437"));
        byte[] buffer = new byte[1024];

        for (ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry()) {
            File newFile = new File(destDir.toFile(), zipEntry.getName());
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Error creating directory while unzipping");
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Error creating directory while unzipping");
                }

                final FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();

                files.add(newFile);
            }
        }

        return files;
    }

    public static String read(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return new String(inputStream.readAllBytes(), Charset.defaultCharset());
        }
    }

    public static void save(Resource resource, String path) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(resource.getInputStream().readAllBytes());
        }
    }

    public static void save(Resource resource, Path path) throws IOException {
        save(resource, path.toString());
    }

    public static void save(BufferedImage image, String path, String formatName) throws IOException {
        var baos = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, baos);

        var bar = new ByteArrayResource(baos.toByteArray());
        save(bar, path);
    }

    public static String saveTempFile(byte[] data, String suffix) throws IOException {
        return saveTempFile(data, "dokument_", suffix).getAbsolutePath();
    }

    public static File saveTempFile(byte[] data, String prefix, String suffix) throws IOException {
        File file = File.createTempFile(prefix, suffix);
        file.deleteOnExit();

        log.info("Skriver fil till sökväg: {}", file.getAbsolutePath());
        var fos = new FileOutputStream(file);
        fos.write(data);
        fos.close();

        return file;
    }
}
