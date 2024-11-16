package com.assignmentgrader.app;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;


public class ChatBotEvaluator implements Evaluator {
    private final Class<?> chatBotClass;

    public ChatBotEvaluator(Class<?> clazz) {
        this.chatBotClass = clazz;
    }

    @Override
    public EvaluationResult evaluate() {
        EvaluationResult result = new EvaluationResult();
        result.setClassName("ChatBot");
        new AttributeEvaluator().evaluate(result);
        new ConstructorEvaluator().evaluate(result);
        new MethodEvaluator().evaluate(result);
        return result;
    }

    private class AttributeEvaluator {
        public void evaluate(EvaluationResult result) {
            if(!chatBotClass.getSimpleName().equals("ChatBot")) {
                System.err.println("Error: Class Name should be 'ChatBot'.");
            }

            try {
                Field chatBotName = chatBotClass.getDeclaredField("chatBotName");
                if (chatBotName.getType() == String.class && Modifier.isPrivate(chatBotName.getModifiers())) {
                    result.addTestResults(1, "chatBotName: Correct attribute name and access modifier type.");
                } else {
                    result.addTestResults(0, "chatBotName: Incorrect attribute name or access modifier type.");
                }

                Field numResponsesGenerated = chatBotClass.getDeclaredField("numResponsesGenerated");
                if (numResponsesGenerated.getType() == int.class && Modifier.isPrivate(numResponsesGenerated.getModifiers())) {
                    result.addTestResults(1, "numResponsesGenerated: Correct attribute name and access modifier type.");
                } else {
                    result.addTestResults(0, "numResponsesGenerated: Incorrect attribute name or access modifier type.");
                }

                Field messageLimit = chatBotClass.getDeclaredField("messageLimit");
                if (messageLimit.getType() == int.class && Modifier.isPrivate(messageLimit.getModifiers()) && messageLimit.getInt(null) == 10 && Modifier.isFinal(messageLimit.getModifiers())) {
                    result.addTestResults(3, "messageLimit: Correct attribute name, access modifier type, is fixed and initialized correctly.");
                } else {
                    result.addTestResults(0, "messageLimit: Incorrect attribute name, access modifier type or initialization.");
                }

                Field messageNumber = chatBotClass.getDeclaredField("messageNumber");
                if (messageNumber.getType() == int.class && Modifier.isPrivate(messageNumber.getModifiers()) && messageNumber.getInt(null) == 0) {
                    result.addTestResults(2, "messageNumber: Correct attribute name, access modifier type and initialization.");
                } else {
                    result.addTestResults(0, "messageNumber: Incorrect attribute name, access modifier type or initialization.");
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private class ConstructorEvaluator {
        public void evaluate(EvaluationResult result) {
            try {
                Constructor<?> defaultConstructor = chatBotClass.getConstructor();
                Object defaultInstance = defaultConstructor.newInstance();
                Field chatBotNameField = chatBotClass.getDeclaredField("chatBotName");
                chatBotNameField.setAccessible(true);
                String defaultName = (String) chatBotNameField.get(defaultInstance);
                if ("ChatGPT-3.5".equals(defaultName)) {
                    result.addTestResults(3, "ChatBot(): Correct default chatBot object created.");
                } else {
                    result.addTestResults(0, "ChatBot(): Incorrect default chatBot object created.");
                }

                Constructor<?> overLoadedConstructor = chatBotClass.getConstructor(int.class);
                Object overLoadedInstance = overLoadedConstructor.newInstance(1);
                String chatBotName = (String) chatBotNameField.get(overLoadedInstance);
                if ("LLaMa".equals(chatBotName)) {
                    result.addTestResults(3, "ChatBot(int): Correct chatBot object created.");
                } else {
                    result.addTestResults(0, "ChatBot(int): Incorrect chatBot object created.");
                }
            } catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e ) {
                System.out.println("Error in constructor evaluation: " + e.getMessage());
            }
        }
    }

    private class MethodEvaluator {
        public void evaluate(EvaluationResult result) {
            try {
                Method getChatBotName = chatBotClass.getMethod("getChatBotName");
                if (getChatBotName.getReturnType() == String.class && Modifier.isPublic(getChatBotName.getModifiers())) {
                    result.addTestResults(1, "getChatBotName(): Correct return type and access modifier.");
                } else {
                    result.addTestResults(0, "getChatBotName(): Incorrect return type or access modifier.");
                }

                Method getNumResponsesGenerated = chatBotClass.getMethod("getNumResponsesGenerated");
                if (getNumResponsesGenerated.getReturnType() == int.class && Modifier.isPublic(getNumResponsesGenerated.getModifiers())) {
                    result.addTestResults(1, "getNumResponsesGenerated(): Correct return type and access modifier.");
                } else {
                    result.addTestResults(0, "getNumResponsesGenerated(): Incorrect return type or access modifier.");
                }

                Method getTotalNumResponsesGenerated = chatBotClass.getMethod("getTotalNumResponsesGenerated");
                if (getTotalNumResponsesGenerated.getReturnType() == int.class && Modifier.isPublic(getTotalNumResponsesGenerated.getModifiers()) && Modifier.isStatic(getTotalNumResponsesGenerated.getModifiers())) {
                    result.addTestResults(2, "getTotalNumResponsesGenerated(): Correct return type and access modifier and is a class method.");
                } else {
                    result.addTestResults(0, "getTotalNumResponsesGenerated(): Incorrect return type, access modifier or is not a class method.");
                }

                Method getTotalNumMessagesRemaining = chatBotClass.getMethod("getTotalNumMessagesRemaining");
                if (getTotalNumMessagesRemaining.getReturnType() == int.class && Modifier.isPublic(getTotalNumMessagesRemaining.getModifiers()) && Modifier.isStatic(getTotalNumMessagesRemaining.getModifiers())) {
                    result.addTestResults(3, "getTotalNumMessagesRemaining(): Correct return type and access modifier and is a class method.");
                } else {
                    result.addTestResults(0, "getTotalNumMessagesRemaining(): Incorrect return type, access modifier or is not a class method.");
                }
        
                Method limitReached = chatBotClass.getMethod("limitReached");
                if (limitReached.getReturnType() == boolean.class && Modifier.isPublic(limitReached.getModifiers()) && Modifier.isStatic(limitReached.getModifiers())) {
                    result.addTestResults(3, "limitReached(): Correct return type and access modifier.");
                } else {
                    result.addTestResults(0, "limitReached(): Incorrect return type, access modifier or is not a class method.");
                }
        
                Method generateResponse = chatBotClass.getDeclaredMethod("generateResponse");
                if (generateResponse.getReturnType() == String.class && Modifier.isPrivate(generateResponse.getModifiers())) {
                    result.addTestResults(5, "generateResponse(): Correct return type and access modifier.");
                } else {
                    result.addTestResults(0, "generateResponse(): Incorrect return type or access modifier.");
                }
        
                Method prompt = chatBotClass.getMethod("prompt", String.class);
                if (prompt.getReturnType() == String.class && Modifier.isPublic(prompt.getModifiers())) {
                    result.addTestResults(4, "prompt(String): Correct return type and access modifier.");
                } else {
                    result.addTestResults(0, "prompt(String): Incorrect return type or access modifier.");
                }
        
                Method toStringMethod = chatBotClass.getMethod("toString");
                if (toStringMethod.getReturnType() == String.class && Modifier.isPublic(toStringMethod.getModifiers())) {
                    result.addTestResults(4, "toString(): Correct return type and access modifier.");
                } else {
                    result.addTestResults(0, "toString(): Incorrect return type or access modifier.");
                }
            } catch (NoSuchMethodException e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
    }
}