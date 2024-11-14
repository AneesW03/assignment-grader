package com.assignmentgrader.app;
import java.io.File;
import java.io.IOException;

public interface ExtractorStrategy {
    void extract(String filePath, File destination) throws IOException;
}
