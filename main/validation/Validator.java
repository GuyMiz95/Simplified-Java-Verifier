package main.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a validator, an object capable of validating a unique pattern in given line.
 */
public interface Validator {

    /**
     * Checks if given line contains a specific pattern to be validated.
     * @param lineToValidate line to validate.
     * @return true if line contains mentioned pattern, false otherwise.
     * @throws Exception in case of a syntax error.
     */
    boolean validate(String lineToValidate) throws Exception;

    /**
     * Gets a matcher according to given line and pattern to match.
     * @param line given line.
     * @param patternToMatch pattern to match.
     * @return a Matcher object.
     */
    default Matcher getMatcher(String line, String patternToMatch) {
        Pattern pattern = Pattern.compile(patternToMatch);
        return pattern.matcher(line);
    }

}
