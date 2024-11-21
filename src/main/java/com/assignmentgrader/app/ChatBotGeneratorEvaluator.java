package com.assignmentgrader.app;
import java.lang.reflect.*;

/**
 * Evaluator for testing the `ChatBotGenerator` class.
 * Validates the method `generateChatBotLLM` to ensure correct mapping of input codes to chatbot names.
 * 
 * Criteria for validation:
 * - Correct return type and access modifier.
 * - Static behavior of the method.
 * - Correct mappings of input codes (e.g., 1 -> "LLaMa").
 */
public class ChatBotGeneratorEvaluator extends EvaluatorTemplate {
    @Override
    protected void evaluateMethods() {
        try {
            Method generateChatBotLLM = clazz.getMethod("generateChatBotLLM", int.class);
            if (generateChatBotLLM.getReturnType() == String.class && Modifier.isPublic(generateChatBotLLM.getModifiers()) && Modifier.isStatic(generateChatBotLLM.getModifiers())) {
                if(((String) generateChatBotLLM.invoke(null, 1) == "LLaMa") && ((String) generateChatBotLLM.invoke(null, 2) == "Mistral7B")
                    && ((String) generateChatBotLLM.invoke(null, 3) == "Bard") && ((String) generateChatBotLLM.invoke(null, 4) == "Claude")
                    && ((String) generateChatBotLLM.invoke(null, 5) == "Solar") && ((String) generateChatBotLLM.invoke(null, 0) == "ChatGPT-3.5")) {
                    result.addTestResults(7, "generateChatBotLLM(int): Correct return type, access modifier and chatBot for each input.");
                } else {
                    result.addTestResults(2, "generateChatBotLLM(int): Incorrect chatBot for each input.");
                }
            } else {
                result.addTestResults(0, "Incorrect return type and access modifier.");
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("generateChatBotLLM(int LLMCodeNumber): Error - " + e.getMessage());
        }
    }
}
