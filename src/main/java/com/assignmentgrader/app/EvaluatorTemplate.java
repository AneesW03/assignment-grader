package com.assignmentgrader.app;

/**
 * Abstract base class for evaluating submissions.
 * Provides a template for evaluating attributes, constructors, and methods of a given class.
 * Subclasses can customize behavior by overriding specific methods.
 */
public abstract class EvaluatorTemplate {
    protected Class<?> clazz;
    protected EvaluationResult result;

    /**
     * Evaluates the submission by invoking methods to check attributes, constructors, and methods.
     * The evaluation process follows the Template Method design pattern:
     * - Sets up the evaluation result.
     * - Evaluates attributes, constructors, and methods.
     *
     * @return An {@link EvaluationResult} containing the evaluation details.
     */
    public final EvaluationResult evaluate() {
        result = new EvaluationResult();
        result.setClassName(clazz.getSimpleName());
        evaluateAttributes();
        evaluateConstructors();
        evaluateMethods();
        return result;
    }

    /**
     * Sets the class to be evaluated.
     *
     * @param clazz The {@link Class} object representing the class to evaluate.
     */
    public void setClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Evaluates the attributes of the class. Default implementation does nothing.
     * Subclasses can override this method to provide specific attribute checks.
     */
    protected void evaluateAttributes() {

    }

    /**
     * Evaluates the constructors of the class. Default implementation does nothing.
     * Subclasses can override this method to provide specific constructor checks.
     */
    protected void evaluateConstructors() {

    }

    /**
     * Evaluates the methods of the class. Subclasses must implement this method.
     */
    protected abstract void evaluateMethods();

}
