package main.validation;

/**
 * Represents an exception thrown when an S-java file has a syntax related error
 */
public class SyntaxException extends Exception{
    public SyntaxException(String errorMessage){
        super(errorMessage);
    }
}
