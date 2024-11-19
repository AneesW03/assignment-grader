package com.assignmentgrader.app;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the results of evaluating a submission.
 * Stores individual test results, computes the total score, and formats results for reporting.
 */
public class EvaluationResult {
    private int totalScore;
    private List<String> testResults;
    private String className;

    /**
     * Constructs a new {@code EvaluationResult} with a total score of 0 and an empty list of test results.
     */
    public EvaluationResult() {
        this.totalScore = 0;
        this.testResults = new ArrayList<>();
    }

    /**
     * Adds a test result to the evaluation.
     *
     * @param score    The score for the test.
     * @param feedback Feedback or a description of the test result.
     */
    public void addTestResults(int score, String feedback) {
        this.totalScore += score;
        String result = "Score: " + score + " - " + feedback;
        testResults.add(result);
    }

    /**
     * Returns the total score accumulated from all test results.
     *
     * @return The total score.
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Sets the name of the class being evaluated.
     *
     * @param className The name of the class under evaluation.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Formats the evaluation results into a string suitable for inclusion in a PDF report.
     * The string contains the class name, individual test results, and the total score.
     *
     * @return A formatted string representation of the evaluation results.
     */
    public String toPDFString() {
        StringBuilder string = new StringBuilder("Evaluation Report: " + this.className + "\n");
        for(String result : testResults) {
            string.append(result).append("\n");
        }
        string.append("Total Score: ").append(totalScore).append("\n\n");
        return string.toString();
    }
}
