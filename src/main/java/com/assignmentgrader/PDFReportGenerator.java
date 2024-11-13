package com.assignmentgrader.app;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class PDFReportGenerator {
    private static final float MARGIN = 50;
    private final Path outputDir;
    private final String studentID;
    private final List<String> testResults; // List of pass/fail results
    private final List<String> scoreResults;    // List of feedback messages for failed tests

    public PDFReportGenerator(Path outputDir, char[] studentID, List<String> testResults, List<String> scoreResults) {
        this.outputDir = outputDir;
        this.studentID = new String(studentID);
        this.testResults = testResults;
        this.scoreResults = scoreResults;
    }

    public void generatePDFReport() throws IOException {
        // Create a new PDF document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.beginText();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(MARGIN, 750);
            contentStream.showText("Evaluation Report for Student ID: " + studentID);
            contentStream.newLine();
            contentStream.showText("Date: " + LocalDate.now().toString());
            contentStream.endText();

            // Test Results Section
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, 650);
            contentStream.showText("Test Results:");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, 630);
            for (String result : testResults) {
                contentStream.showText("- " + result);
                contentStream.newLine();
            }
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA_BOLD,14);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, 150);
            contentStream.showText("Overall Score:");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, 130);
            for (String result : scoreResults) {
                contentStream.showText("- " + result);
                contentStream.newLine();
            }
            contentStream.endText();
        }

        // Save the PDF to the output directory
        Path filePath = outputDir.resolve("EvaluationReport_" + studentID + ".pdf");
        document.save(filePath.toFile());
        document.close();
        System.out.println("PDF report generated at: " + filePath.toAbsolutePath());
    }

    // public static void main(String[] args) {
    //     try {
    //         Path outputDir = Path.of("assignment-grader\\src\\main\\resources");
    //         String studentID = "816030569";
    //         List<String> testResults = List.of("ChatBot class name: Passed", "ChatBot numResponsesGenerated field: Passed", "ChatBot generateResponse method: Failed");
    //         List<String> scoreResults = List.of("generateResponse method should return a formatted response and increment response counter.");

    //         //PDFReportGenerator reportGenerator = new PDFReportGenerator(outputDir, studentID, testResults, scoreResults);
    //         //reportGenerator.generatePDFReport();
    //     //} catch (IOException e) {
    //     //    System.out.println("Error generating PDF report: " + e.getMessage());
    //     //}
    // //}
}
