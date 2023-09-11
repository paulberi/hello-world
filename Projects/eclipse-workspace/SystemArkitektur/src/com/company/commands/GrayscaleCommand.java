package com.company.commands;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class GrayscaleCommand implements ImageProcessingCommand {
    @Override
    public BufferedImage process(BufferedImage image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        op.filter(image, bufferedImage);

        return bufferedImage;
    }
}
