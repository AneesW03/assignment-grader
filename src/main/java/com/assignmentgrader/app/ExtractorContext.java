package com.assignmentgrader.app;
import java.io.File;
import java.io.IOException;

public class ExtractorContext {
    private ExtractorStrategy strategy;

    public void setExtractionStrategy(ExtractorStrategy strategy) {
        this.strategy = strategy;
    }

    public void extractSubmission(String filePath) throws IOException {
        File zipFile = new File(filePath);
        String fileName = zipFile.getName().replaceFirst("[.][^.]+$", "");

        File destinationDirectory = new File("target\\unzipped", fileName);
        strategy.extract(filePath, destinationDirectory);
    }
}