package oop.ex6.main.scopeLogic;

/**
 * Exception thrown for invalid Variable assignments
 */
public class VariableAssignmentException extends Exception {
    VariableAssignmentException(String errorMessage) {
        super(errorMessage);
    }
}
