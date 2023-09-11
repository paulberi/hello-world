package com.company;

import com.company.commands.ImageProcessingCommand;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageProcessingPipeline {
    private ArrayList<ImageProcessingCommand> pipeline = new ArrayList<>();

    public void addCommand(ImageProcessingCommand command) {
        this.pipeline.add(command);
    }

    public BufferedImage execute(BufferedImage image) {
        BufferedImage result = image;
        for(ImageProcessingCommand command: pipeline) {
            result = command.process(result);
        }
        return result;
    }

}
