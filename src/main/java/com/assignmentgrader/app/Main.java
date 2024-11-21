package com.assignmentgrader.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Entry point for the Assignment Grader application.
 * 
 * This application automates the process of:
 * - Extracting ZIP submissions from a directory.
 * - Compiling and loading student Java files.
 * - Evaluating submissions against specified criteria using evaluators.
 * - Generating PDF reports with evaluation results for each submission.
 * 
 * The application assumes a specific directory structure for ZIP submissions
 * and processes each submission independently.
 */
public class Main {
    private static String filePath = "src/main/resources/assets";
    private static Path extractedJavaFiles = Paths.get("target/unzipped");
    private static Path compiledFilesDirectory = Paths.get("target/classes");
    private static char[] studentID;

    /**
     * Main method to execute the grading process.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        boolean testSuccess = true;
        boolean compilationSuccess = true;

        ExtractorContext extractor = new ExtractorContext();
        extractor.setExtractionStrategy(new ZipExtractorStrategy());

        File folder = new File(filePath);
        if(folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if(files != null) {
                for (File file : files) {
                    try {
                        extractor.extractSubmission(file.getAbsolutePath());
                    } catch (Exception e) {
                        System.err.println("Error here: " + e.getMessage());
                    }
                }
            }
        }

        File[] submissions = getSubmissions(extractedJavaFiles);
        for(File submission : submissions) {
            studentID = submission.getName().substring(0,9).toCharArray();
            Path javaFiles = Paths.get("target/unzipped/" + submission.getName());
            Path simulationFilePath = Paths.get("target/unzipped/" + submission.getName() + "/ChatBotSimulation.java");
            String testResults = "";
            int score = 0;

            CompilationFacade facade = new CompilationFacade(javaFiles, compiledFilesDirectory);
            EvaluationResult result = new EvaluationResult();

            EvaluatorTemplate chatBotEvaluator = new ChatBotEvaluator();
            result = runEvaluation("ChatBot", chatBotEvaluator, facade);
            testResults += result.toPDFString();
            score += result.getTotalScore();
            if(result.getTotalScore() != 36) { testSuccess = false; }
            if(!facade.isSuccessful()) { compilationSuccess = false; } 
                            
            EvaluatorTemplate chatBotPlatformEvaluator = new ChatBotPlatformEvaluator();
            result = runEvaluation("ChatBotPlatform", chatBotPlatformEvaluator, facade);
            testResults += result.toPDFString();
            score += result.getTotalScore();
            if(result.getTotalScore() != 20) { testSuccess = false; }
            if(!facade.isSuccessful()) { compilationSuccess = false; } 
                            
            EvaluatorTemplate chatBotGeneratorEvaluator = new ChatBotGeneratorEvaluator();
            result = runEvaluation("ChatBotGenerator", chatBotGeneratorEvaluator, facade);
            testResults += result.toPDFString();
            score += result.getTotalScore();
            if(result.getTotalScore() != 7) { testSuccess = false; }
            if(!facade.isSuccessful()) { compilationSuccess = false; } 
                            
            EvaluatorTemplate chatBotSimulationEvaluator = new ChatBotSimulationEvaluator(simulationFilePath);
            result = runEvaluation("ChatBotSimulation", chatBotSimulationEvaluator, facade);
            testResults += result.toPDFString();
            score += result.getTotalScore();
            if(result.getTotalScore() != 12) { testSuccess = false; }
            if(!facade.isSuccessful()) { compilationSuccess = false; } 
                            
            EvaluationResult general = new EvaluationResult();
            general.setClassName("General");
            if (testSuccess) { general.addTestResults(10, "All tests for all classes passed."); } 
            else { general.addTestResults(0, "Not all tests passed successfully."); }
                            
            if (compilationSuccess) { general.addTestResults(15, "Compiles and runs successfully."); }
            else { general.addTestResults(0, "Does not compile and run."); }
                                                    
            testResults += general.toPDFString();
            score += general.getTotalScore();
                            
            try {
                String totalScore = Integer.toString(score);
                PDFReportGenerator reportGenerator = new PDFReportGenerator(javaFiles, studentID, testResults, totalScore);
                reportGenerator.generatePDFReport();
            } catch (IOException e) {
                System.err.println("Error generating PDF: " + e.getMessage());
            }

            deleteClassFiles(compiledFilesDirectory);
        }
    }
    
    /**
     * Runs evaluation for a given class using the specified evaluator.
     *
     * @param className The name of the class to evaluate.
     * @param eval      The {@link Evaluator} to use for evaluation.
     * @param facade    The {@link CompilationFacade} used to compile and load the class.
     * @return The evaluation result.
     */
    private static EvaluationResult runEvaluation(String className, EvaluatorTemplate eval, CompilationFacade facade) {
        EvaluationResult result = new EvaluationResult();
        Optional<Class<?>> clazz = facade.compileAndLoadClass(className);
        if(facade.isSuccessful()) {
            if(clazz.isPresent()) {
                Class<?> clazzClass = clazz.get();
                eval.setClass(clazzClass);
                result = eval.evaluate();
                return result;
            }
        } else {
            result.addTestResults(0, "Does Not Compile");
            return result;
        }
        return null;
    }

    /**
     * Deletes all `.class` files from the specified directory.
     *
     * @param directory The directory to clean.
     */
    private static void deleteClassFiles(Path directory) {
        File file = new File(directory.toString());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if(f.getName().endsWith(".class")) {
                    f.delete();
                }
            }
        }
    }

    /**
     * Retrieves all student submission directories from the specified directory.
     *
     * @param directory The directory containing submission folders.
     * @return An array of submission directories, or {@code null} if none are found.
     */
    private static File[] getSubmissions(Path directory) {
        File file = new File(directory.toString());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                int count = 0;

                for (File f : files) {
                    if (f.isDirectory()) {
                        count++;
                    }
                }

                File[] folders = new File[count];
                int index = 0;
                for (File f : files) {
                    if (f.isDirectory()) {
                        folders[index] = f;
                        index++;
                    }
                }
                return folders;
            }
        }
        return null;
    }

}
