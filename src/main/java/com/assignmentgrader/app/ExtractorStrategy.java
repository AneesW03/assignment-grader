package com.assignmentgrader.app;
import java.io.File;
import java.io.IOException;

/**
 * Interface representing a strategy for extracting files.
 * Classes implementing this interface should define how specific file formats are extracted.
 */
public interface ExtractorStrategy {
    /**
     * Extracts a file to the specified destination directory.
     *
     * @param filePath    The path of the file to extract.
     * @param destination The directory where the extracted content should be placed.
     * @throws IOException If an I/O error occurs during extraction.
     */
    void extract(String filePath, File destination) throws IOException;
    
}
