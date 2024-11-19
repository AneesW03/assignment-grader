package com.assignmentgrader.app;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Implementation of {@link ExtractorStrategy} for extracting ZIP files.
 * Supports extracting nested ZIP files and filters specific file types (e.g., `.java`).
 */
public class ZipExtractorStrategy implements ExtractorStrategy {
    /**
     * Extracts the contents of a ZIP file to the specified destination directory.
     * - Creates directories for nested folders.
     * - Extracts `.zip` files recursively into their own directories.
     * - Extracts `.java` files while preserving their directory structure.
     *
     * @param filePath    The path of the ZIP file to extract.
     * @param destination The directory where the extracted content should be placed.
     * @throws IOException If an I/O error occurs during extraction.
     */
    @Override
    public void extract (String filePath, File destination) throws IOException {
        if (!destination.exists()) {
            destination.mkdirs();
        }

        try (ZipInputStream inputStream = new ZipInputStream(new FileInputStream(filePath))) {
            ZipEntry entry;
            while ((entry = inputStream.getNextEntry()) != null) {
                File outputFile = new File(destination, entry.getName());

                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                }
                
                else {
                    outputFile.getParentFile().mkdirs();

                    if(entry.getName().endsWith(".zip")) {
                        try (FileOutputStream outputStream = new FileOutputStream(outputFile);
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                            
                            byte[] buffer = new byte[4096];
                            int read;
                            while ((read = inputStream.read(buffer)) != -1) {
                                bufferedOutputStream.write(buffer, 0, read);
                            }
                        }

                        File nestedDirectory = new File(destination, outputFile.getName().replace(".zip", ""));
                        extract(outputFile.getAbsolutePath(), nestedDirectory);
                        outputFile.delete();
                    }

                    else if(entry.getName().endsWith(".java")) {

                        try (FileOutputStream outputStream = new FileOutputStream(outputFile);
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                            
                            byte[] buffer = new byte[4096];
                            int read;
                            while ((read = inputStream.read(buffer)) != -1) {
                                bufferedOutputStream.write(buffer, 0, read);
                            }
                        }
                    }
                }
                inputStream.closeEntry();
            }
        }
    }
}
