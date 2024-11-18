package com.assignmentgrader.app;

import java.util.ArrayList;
import java.util.List;

public class EvaluationResult {
    private int totalScore;
    private List<String> testResults;
    private String className;

    public EvaluationResult() {
        this.totalScore = 0;
        this.testResults = new ArrayList<>();
    }

    public void addTestResults(int score, String feedback) {
        this.totalScore += score;
        String result = "Score: " + score + " - " + feedback;
        testResults.add(result);
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String toPDFString() {
        StringBuilder string = new StringBuilder("Evaluation Report: " + this.className + "\n");
        for(String result : testResults) {
            string.append(result).append("\n");
        }
        string.append("Total Score: ").append(totalScore).append("\n\n");
        return string.toString();
    }
}
