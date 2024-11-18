package com.assignmentgrader.app;

public interface Evaluator {
    EvaluationResult evaluate();
    boolean isPassed();
    void setClass(Class<?> clazz);
}
