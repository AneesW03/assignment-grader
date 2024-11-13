package com.assignmentgrader.app;

import java.util.List;

public interface Evaluator {
    List<String> runEvaluation(Class<?> chatBotClass);
}
