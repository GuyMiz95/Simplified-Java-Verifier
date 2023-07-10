package oop.ex6.main.scopeLogic;

/**
 * Exception thrown for invalid returns
 */
public class ReturnException extends Exception{
    ReturnException(String errorMessage) {
        super(errorMessage);
    }
}
