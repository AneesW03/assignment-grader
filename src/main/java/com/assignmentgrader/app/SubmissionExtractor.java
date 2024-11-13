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

        File destinationDirectory = new File("assignment-grader\\target\\unzipped", fileName);
        extractor.extract(filePath, destinationDirectory);
    }

    public static void main(String[] args) {
        Extractor extractor = new ZipFileExtractor();
        SubmissionExtractor handler = new SubmissionExtractor(extractor);
        
        try {
            handler.extractSubmission("assignment-grader\\src\\main\\resources\\assets\\816030569_A1.zip");
            System.out.println("Successful...");
        } catch (IOException e) {
            System.err.println("Failed... " + e.getMessage());
        }
    }
}