package main.validation;

import java.util.regex.Matcher;

/**
 * Represents a comment validator - checking if given line has a comment in it.
 */
public class CommentValidator implements Validator{

    // comment pattern
    private final static String COMMENT_PATTERN = "//";

    /**
     * Validates that the current line is a syntactically correct Comment
     * @param lineToValidate line to validate.
     * @return true if the current line is a syntactically correct Comment
     */
    @Override
    public boolean validate(String lineToValidate) {
        Matcher matcher = getMatcher(lineToValidate, COMMENT_PATTERN);
        return matcher.find() && matcher.start() == 0;
    }
}
