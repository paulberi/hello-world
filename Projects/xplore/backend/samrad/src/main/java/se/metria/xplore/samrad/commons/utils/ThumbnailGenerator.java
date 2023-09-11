package se.metria.xplore.samrad.commons.utils;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import se.metria.xplore.samrad.entities.FilEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.awt.Image.SCALE_SMOOTH;

/**
 *från matDatabas bifogatfilService
 * **/

public class ThumbnailGenerator {

    public static void createThumbnail(FilEntity entity) throws IOException {

        try (var in = new ByteArrayInputStream(entity.getFil()); var out = new ByteArrayOutputStream()) {
            var image = ImageIO.read(in);
            var format = getFormat(entity);

            var width = image.getWidth();
            var height = image.getHeight();
            var scaleFactor = Math.max(Math.max(width, height) / 200d, 1d);
            var newWidth = (int) Math.round(width / scaleFactor);
            var newHeight = (int) Math.round(height / scaleFactor);

            var thumbnail = scaleImage(image, newWidth, newHeight);
            ImageIO.write(thumbnail, format, out);
            entity.setThumbnail(out.toByteArray());
        }
    }
    private static String getFormat(FilEntity entity) {
        return entity.getMimetyp() != null ? entity.getMimetyp().substring(entity.getMimetyp().indexOf("/") + 1) : "";
    }

    private static BufferedImage scaleImage(BufferedImage image, int newWidth, int newHeight) {
        Graphics2D g = null;
        try {
            var thumbnail = new BufferedImage(newWidth, newHeight, image.getType());
            g = thumbnail.createGraphics();
            g.drawImage(image.getScaledInstance(newWidth, newHeight, SCALE_SMOOTH), 0, 0, null);
            return thumbnail;
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
    }
}
