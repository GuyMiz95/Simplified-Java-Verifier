package main.validation;

import java.util.regex.Matcher;

/**
 * Represents an empty line validator, validates whether there's an empty line.
 */
public class EmptyLineValidator implements Validator{
    // Regex patterns
    private static final String EMPTY_LINE_PATTERN = "^[\\s\\t]*[\n\r]*$";

    /**
     * Validates that the current line is a syntactically correct Empty line
     * @param lineToValidate line to validate.
     * @return true if the current line is a syntactically correct Empty line
     */
    @Override
    public boolean validate(String lineToValidate) {
        Matcher matcher = getMatcher(lineToValidate, EMPTY_LINE_PATTERN);
        return matcher.find() && matcher.start() == 0;
    }
}
