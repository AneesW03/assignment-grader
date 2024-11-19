package com.assignmentgrader.app;

/**
 * Interface for evaluating submissions.
 * Provides methods to evaluate a class, check if the evaluation passed, and set the class to evaluate.
 */
public interface Evaluator {
    /**
     * Evaluates the submission and returns the evaluation result.
     *
     * @return An {@link EvaluationResult} containing the evaluation details.
     */
    EvaluationResult evaluate();

    /**
     * Indicates whether the evaluation passed or failed.
     *
     * @return {@code true} if the evaluation passed; {@code false} otherwise.
     */
    boolean isPassed();

    /**
     * Sets the class to be evaluated.
     *
     * @param clazz The class to evaluate.
     */
    void setClass(Class<?> clazz);
}
