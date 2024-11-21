package com.assignmentgrader.app;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * Evaluator for testing the `ChatBot` class.
 * Validates attributes, constructors, and methods based on predefined specifications.
 * 
 * Attributes are checked for:
 * - Correct name, type, access modifier, and initialization (if applicable).
 * 
 * Constructors are checked for:
 * - Correct default and parameterized initialization of class attributes.
 * 
 * Methods are checked for:
 * - Correct return types, access modifiers, and static or instance behavior.
 */
public class ChatBotEvaluator extends EvaluatorTemplate {
    @Override
    protected void evaluateAttributes() {
        if(!super.clazz.getSimpleName().equals("ChatBot")) {
            System.err.println("Error: Class Name should be 'ChatBot'.");
        }

        try {
            Field chatBotName = clazz.getDeclaredField("chatBotName");
            if (chatBotName.getType() == String.class && Modifier.isPrivate(chatBotName.getModifiers())) {
                result.addTestResults(1, "chatBotName: Correct attribute name and access modifier type.");
            } else {
                result.addTestResults(0, "chatBotName: Incorrect attribute name or access modifier type.");
            }
        } catch (NoSuchFieldException e) {
            System.err.println("chatBotName: Error - " + e.getMessage());
        }

        try {
            Field numResponsesGenerated = clazz.getDeclaredField("numResponsesGenerated");
            if (numResponsesGenerated.getType() == int.class && Modifier.isPrivate(numResponsesGenerated.getModifiers())) {
                result.addTestResults(1, "numResponsesGenerated: Correct attribute name and access modifier type.");
            } else {
                result.addTestResults(0, "numResponsesGenerated: Incorrect attribute name or access modifier type.");
            }
        } catch (NoSuchFieldException e) {
            System.err.println("numResponsesGenerated: Error - " + e.getMessage());
        }

        try {
            Field messageLimit = clazz.getDeclaredField("messageLimit");
            messageLimit.setAccessible(true);
            if (messageLimit.getType() == int.class && Modifier.isPrivate(messageLimit.getModifiers()) && messageLimit.getInt(null) == 10) {
                result.addTestResults(3, "messageLimit: Correct attribute name, access modifier type, is fixed and initialized correctly.");
            } else {
                result.addTestResults(0, "messageLimit: Incorrect attribute name, access modifier type or initialization.");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("messageLimit: Error - " + e.getMessage());
        }

        try {
            Field messageNumber = clazz.getDeclaredField("messageNumber");
            messageNumber.setAccessible(true);
            if (messageNumber.getType() == int.class && Modifier.isPrivate(messageNumber.getModifiers()) && messageNumber.getInt(null) == 0) {
                result.addTestResults(2, "messageNumber: Correct attribute name, access modifier type and initialization.");
            } else {
                result.addTestResults(0, "messageNumber: Incorrect attribute name, access modifier type or initialization.");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("messageNumber: Error - " + e.getMessage());
        }
    }

    @Override
    protected void evaluateConstructors() {
        Field chatBotNameField;
        try {
            Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
            Object defaultInstance = defaultConstructor.newInstance();
            chatBotNameField = clazz.getDeclaredField("chatBotName");
            chatBotNameField.setAccessible(true);
            String defaultName = (String) chatBotNameField.get(defaultInstance);
            if ("ChatGPT-3.5".equals(defaultName)) {
                result.addTestResults(3, "ChatBot(): Correct default chatBot object created.");
            } else {
                result.addTestResults(0, "ChatBot(): Incorrect default chatBot object created.");
            }
        } catch (NoSuchMethodException | NoSuchFieldException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("ChatBot(): Error - " + e.getMessage());
        }

        try {
            Constructor<?> overLoadedConstructor = clazz.getDeclaredConstructor(int.class);
            Object overLoadedInstance = overLoadedConstructor.newInstance(1);
            chatBotNameField = clazz.getDeclaredField("chatBotName");
            chatBotNameField.setAccessible(true);
            String chatBotName = (String) chatBotNameField.get(overLoadedInstance);
            if ("LLaMa".equals(chatBotName)) {
                result.addTestResults(3, "ChatBot(int): Correct chatBot object created.");
            } else {
                result.addTestResults(0, "ChatBot(int): Incorrect chatBot object created.");
            }
        } catch (NoSuchMethodException | NoSuchFieldException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("ChatBot(int LLMCode): Error - " + e.getMessage());
        }
    }
 
    @Override
    protected void evaluateMethods() {
        try {
            Method getChatBotName = clazz.getMethod("getChatBotName");
            if (getChatBotName.getReturnType() == String.class && Modifier.isPublic(getChatBotName.getModifiers())) {
                result.addTestResults(1, "getChatBotName(): Correct return type and access modifier.");
            } else {
                result.addTestResults(0, "getChatBotName(): Incorrect return type or access modifier.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("getChatBotName(): Error - " + e.getMessage());
        }

        try {
            Method getNumResponsesGenerated = clazz.getMethod("getNumResponsesGenerated");
            if (getNumResponsesGenerated.getReturnType() == int.class && Modifier.isPublic(getNumResponsesGenerated.getModifiers())) {
                result.addTestResults(1, "getNumResponsesGenerated(): Correct return type and access modifier.");
            } else {
                result.addTestResults(0, "getNumResponsesGenerated(): Incorrect return type or access modifier.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("getNumResponsesGenerated(): Error - " + e.getMessage());
        }

        try {
            Method getTotalNumResponsesGenerated = clazz.getMethod("getTotalNumResponsesGenerated");
            if (getTotalNumResponsesGenerated.getReturnType() == int.class && Modifier.isPublic(getTotalNumResponsesGenerated.getModifiers()) && Modifier.isStatic(getTotalNumResponsesGenerated.getModifiers())) {
                result.addTestResults(2, "getTotalNumResponsesGenerated(): Correct return type and access modifier and is a class method.");
            } else {
                result.addTestResults(0, "getTotalNumResponsesGenerated(): Incorrect return type, access modifier or is not a class method.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("getTotalNumResponsesGenerated(): Error - " + e.getMessage());
        }

        try {
            Method getTotalNumMessagesRemaining = clazz.getMethod("getTotalNumMessagesRemaining");
            if (getTotalNumMessagesRemaining.getReturnType() == int.class && Modifier.isPublic(getTotalNumMessagesRemaining.getModifiers()) && Modifier.isStatic(getTotalNumMessagesRemaining.getModifiers())) {
                result.addTestResults(3, "getTotalNumMessagesRemaining(): Correct return type and access modifier and is a class method.");
            } else {
                result.addTestResults(0, "getTotalNumMessagesRemaining(): Incorrect return type, access modifier or is not a class method.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("getTotalNumMessagesRemaining(): Error - " + e.getMessage());
        }

        try {
            Method limitReached = clazz.getMethod("limitReached");
            if (limitReached.getReturnType() == boolean.class && Modifier.isPublic(limitReached.getModifiers()) && Modifier.isStatic(limitReached.getModifiers())) {
                result.addTestResults(3, "limitReached(): Correct return type and access modifier.");
            } else {
                result.addTestResults(0, "limitReached(): Incorrect return type, access modifier or is not a class method.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("limitReached(): Error - " + e.getMessage());
        }

        try {
            Method generateResponse = clazz.getDeclaredMethod("generateResponse");
            if (generateResponse.getReturnType() == String.class && Modifier.isPrivate(generateResponse.getModifiers())) {
                result.addTestResults(5, "generateResponse(): Correct return type and access modifier.");
            } else {
                result.addTestResults(0, "generateResponse(): Incorrect return type or access modifier.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("generateResponse(): Error - " + e.getMessage());
        }

        try {
            Method prompt = clazz.getMethod("prompt", String.class);
            if (prompt.getReturnType() == String.class && Modifier.isPublic(prompt.getModifiers())) {
                result.addTestResults(4, "prompt(String): Correct return type and access modifier.");
            } else {
                result.addTestResults(0, "prompt(String): Incorrect return type or access modifier.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("prompt(String requestMessage): Error - " + e.getMessage());
        }

        try {
            Method toStringMethod = clazz.getMethod("toString");
            if (toStringMethod.getReturnType() == String.class && Modifier.isPublic(toStringMethod.getModifiers())) {
                result.addTestResults(4, "toString(): Correct return type and access modifier.");
            } else {
                result.addTestResults(0, "toString(): Incorrect return type or access modifier.");
            }
        } catch (NoSuchMethodException e) {
            System.err.println("toString(): Error - " + e.getMessage());
        }
    }
}
