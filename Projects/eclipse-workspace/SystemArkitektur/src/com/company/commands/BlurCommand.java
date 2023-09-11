package com.company.commands;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class BlurCommand implements ImageProcessingCommand {
    float[] elements = { 0.11f, 0.11f, 0.11f,
                        0.11f, 0.11f, 0.11f,
                        0.11f, 0.11f, 0.11f,
                        };

    @Override
    public BufferedImage process(BufferedImage image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Kernel kernel = new Kernel(3, 3, elements);
        ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        cop.filter(image, bufferedImage);
        return bufferedImage;
    }
}
