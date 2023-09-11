package com.company.commands;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class RotateCommand implements ImageProcessingCommand {
    @Override
    public BufferedImage process(BufferedImage image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.PI/2, image.getWidth()/2, image.getHeight()/2);
        double offset = (image.getWidth()-image.getHeight())/2;
        transform.translate(offset, offset);

        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        op.filter(image, bufferedImage);
        return bufferedImage;
    }
}
