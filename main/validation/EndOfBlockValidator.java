package oop.ex6.main.validation;

import java.util.regex.Matcher;

/**
 * Represents an end-of-block validator, validates whether there's an end-of-block in given line.
 */
public class EndOfBlockValidator implements Validator{
    // Regex patterns
    private static final String END_OF_BLOCK_PATTERN = "^[\\s\\t]*\\}{1}[\\s\\t]*$";

    /**
     * Validates that the current line is a syntactically correct End of block
     * @param lineToValidate line to validate.
     * @return true if the current line is a syntactically correct End of block
     */
    @Override
    public boolean validate(String lineToValidate) {
        Matcher matcher = getMatcher(lineToValidate, END_OF_BLOCK_PATTERN);
        return matcher.find() && matcher.start() == 0;
    }
}
