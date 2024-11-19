package com.assignmentgrader.app;
import java.io.File;
import java.io.IOException;

/**
 * Context class that uses a specified {@link ExtractorStrategy} to extract files.
 * This class facilitates switching between different extraction strategies at runtime.
 */
public class ExtractorContext {
    private ExtractorStrategy strategy;

    /**
     * Sets the extraction strategy to be used.
     *
     * @param strategy The {@link ExtractorStrategy} implementation to be used for extraction.
     */
    public void setExtractionStrategy(ExtractorStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Extracts a submission file to a directory named after the file (without its extension).
     * The extracted content is placed in the "target/unzipped/<fileName>" directory.
     *
     * @param filePath The path of the submission file to extract.
     * @throws IOException If an I/O error occurs during extraction.
     */
    public void extractSubmission(String filePath) throws IOException {
        File zipFile = new File(filePath);
        String fileName = zipFile.getName().replaceFirst("[.][^.]+$", "");

        File destinationDirectory = new File("target\\unzipped", fileName);
        strategy.extract(filePath, destinationDirectory);
    }
}