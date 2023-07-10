package main.scopeLogic;

/**
 * Exception thrown for invalid Variable declarations
 */
public class VariableDeclarationException extends Exception {
    VariableDeclarationException(String errorMessage) {
        super(errorMessage);
    }
}
