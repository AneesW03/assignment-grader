package com.assignmentgrader.app;
import java.lang.reflect.*;

public class ChatBotGeneratorEvaluator implements Evaluator {
    private Class<?> chatBotGeneratorClass;
    private boolean passed;

    public ChatBotGeneratorEvaluator() {
        this.passed = true;
    }

    @Override
    public void setClass(Class<?> clazz) {
        this.chatBotGeneratorClass = clazz;
    }

    @Override
    public EvaluationResult evaluate() {
        EvaluationResult result = new EvaluationResult();
        result.setClassName("ChatBotGenerator");
        new MethodEvaluator().evaluate(result);
        return result;
    }

    @Override
    public boolean isPassed() {
        return this.passed;
    }

    private class MethodEvaluator {
        private void evaluate(EvaluationResult result) {
            try {
                Method generateChatBotLLM = chatBotGeneratorClass.getMethod("generateChatBotLLM", int.class);
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
}
