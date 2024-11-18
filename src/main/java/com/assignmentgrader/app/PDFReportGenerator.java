package com.assignmentgrader.app;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

public class PDFReportGenerator {
    private static final float MARGIN = 50;
    private Path outputDir;
    private String studentID;
    private String testResults;
    private String scoreResults;

    public PDFReportGenerator(Path outputDir, char[] studentID, String testResults, String scoreResults) {
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
            contentStream.newLine();
            contentStream.showText("Grade: " + scoreResults);
            contentStream.endText();

            // Test Results Section
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, 650);
            contentStream.showText("Test Results:");
            contentStream.endText();
            
            contentStream.setFont(PDType1Font.HELVETICA, 11);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, 630);
            String[] lines = testResults.split("\n");
            for (String line : lines) {
                contentStream.showText(line);
                contentStream.newLine();
            }
            contentStream.endText();
        }

        Path filePath = outputDir.resolve("EvaluationReport_" + studentID + ".pdf");
        document.save(filePath.toFile());
        document.close();
        System.out.println("PDF report generated at: " + filePath.toAbsolutePath());
    }
}
