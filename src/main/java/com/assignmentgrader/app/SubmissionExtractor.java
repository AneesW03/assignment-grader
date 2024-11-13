package com.assignmentgrader.app;

import java.io.File;
import java.io.IOException;

public class SubmissionExtractor {
    private Extractor extractor;

    public SubmissionExtractor(Extractor extractor) {
        this.extractor = extractor;
    }

    public void extractSubmission(String filePath) throws IOException {
        File zipFile = new File(filePath);
        String fileName = zipFile.getName().replaceFirst("[.][^.]+$", "");

        File destinationDirectory = new File("target\\unzipped", fileName);
        extractor.extract(filePath, destinationDirectory);
    }
}
