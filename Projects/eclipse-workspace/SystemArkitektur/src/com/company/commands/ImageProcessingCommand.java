package com.company.commands;

import java.awt.image.BufferedImage;

public interface ImageProcessingCommand {
    public BufferedImage process(BufferedImage image);
}
