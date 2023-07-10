package main.scopeLogic;

/**
 * Exception thrown for invalid Method calls and declarations
 */
public class MethodDefinitionException extends Exception {
    MethodDefinitionException(String errorMessage) {
        super(errorMessage);
    }
}
