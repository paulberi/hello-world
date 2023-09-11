package se.metria.markkoll.service.map;

import org.springframework.stereotype.Service;
import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class ImageService {

    /**
     * Rendera bild med text.
     */
    public BufferedImage textImage(String text, int imageWidth, int imageHeight, int fontSize, Color fontColor) {
        Font textFont = new Font("Arial", Font.PLAIN, fontSize);
        var image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        FontMetrics metrics = graphics.getFontMetrics(textFont);
        int posX = (image.getWidth() - metrics.stringWidth(text)) / 2;
        int posY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        graphics.setFont(textFont);
        graphics.setColor(fontColor);
        graphics.drawString(text, posX, posY);

        return image;
    }
}
