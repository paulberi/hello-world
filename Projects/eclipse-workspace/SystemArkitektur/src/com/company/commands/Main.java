package com.company.commands;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	    ImageProcessingPipeline processingPipeline = new ImageProcessingPipeline();
	    processingPipeline.addCommand(new BlurCommand());
	    processingPipeline.addCommand(new GrayscaleCommand());
	    processingPipeline.addCommand(new RotateCommand());

	    try {
            BufferedImage image = ImageIO.read(new File("C:\\temp\\command\\image.png"));
            image = processingPipeline.execute(image);
            ImageIO.write(image, "png", new File("C:\\temp\\command\\image_out.png"));
        } catch (IOException exception) {

        }
    }
}
