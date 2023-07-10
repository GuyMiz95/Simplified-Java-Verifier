package main.validation;

import java.util.regex.Matcher;

/**
 * Represents an end-of-line validator, validates whether there's an end-of-line in given line.
 */
public class EndOfLineValidator implements Validator{
    // Regex patterns
    private static final String END_OF_LINE_PATTERN = ".*;\\s*$";

    /**
     * Validates that the current line ends with a proper ;
     * @param lineToValidate line to validate.
     * @return true if the current line ends with a proper ;
     */
    @Override
    public boolean validate(String lineToValidate) {
        Matcher matcher = getMatcher(lineToValidate, END_OF_LINE_PATTERN);
        return matcher.find() && matcher.start() == 0;
    }
}
