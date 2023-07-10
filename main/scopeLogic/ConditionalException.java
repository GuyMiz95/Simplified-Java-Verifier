package oop.ex6.main.scopeLogic;

/**
 * Exception thrown for invalid conditionals
 */
public class ConditionalException extends Exception {
    ConditionalException(String errorMessage) {
        super(errorMessage);
    }
}
