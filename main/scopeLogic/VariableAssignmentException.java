package main.scopeLogic;

/**
 * Exception thrown for invalid Variable assignments
 */
public class VariableAssignmentException extends Exception {
    VariableAssignmentException(String errorMessage) {
        super(errorMessage);
    }
}
