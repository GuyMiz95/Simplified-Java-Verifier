package main.validation;

import java.util.Vector;
import java.util.regex.Matcher;

/**
 * Responsible for validating and extracting data from Variable Assignment lines
 */
public class VariableAssignmentValidator extends AssignmentValidator implements Validator{

    /**
     * Validates that the current line is a syntactically correct Variable Assignment
     * @param lineToValidate line to validate.
     * @return true if the current line is a syntactically correct Variable Assignment
     */
    @Override
    public boolean validate(String lineToValidate) {
        lineToValidate = lineToValidate.replaceFirst(";", "");
        Matcher matcher = getMatcher(lineToValidate, MULTI_ASSIGNMENT_PATTERN);
        return matcher.find() && matcher.start() == 0;
    }

    /**
     * Parses given line and extracts the names of 2 variables in line
     * @param line line to parse
     * @return Vector of String[] elements in the form of {left hand var name, right hand var name}
     */
    public Vector<String[]> getVariableAssignment(String line) {
        line = line.replaceFirst(";", "");
    // eligible for a case of a = b or a = 5, returns <"a","b" or "5">
        Vector<String[]> varsAndValues = new Vector<>();
        String[] segments = line.split(",");
        for (String segment : segments) {
            // every x = y
            String[] sides = segment.split("=");
            // add to vector "x" and "y" for further checks.
            varsAndValues.add(new String[] {sides[0].trim(), sides[1].trim()});
        }
        return varsAndValues;
    }
}
