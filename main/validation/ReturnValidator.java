package main.validation;

/**
 * Represents a return keyword validator.
 */
public class ReturnValidator implements Validator{
    // Regex patterns
    private static final String RETURN_KEYWORD_PATTERN = "^\\s*return\\s*";

    /**
     * Validates that the current line is a syntactically correct Return statement
     * @param lineToValidate line to validate.
     * @return true if the current line is a syntactically correct Return statement
     */
    @Override
    public boolean validate(String lineToValidate) {
        //check if we got a valid end of line
        if(new EndOfLineValidator().validate(lineToValidate)){
            return lineToValidate.replaceFirst(";", "").matches(RETURN_KEYWORD_PATTERN);
        }
        return false;
    }
}
