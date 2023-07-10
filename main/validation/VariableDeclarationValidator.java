package main.validation;

import main.sJavaDataTypes.SJavaVariable;

import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Responsible for validating and extracting data from Variable Declaration lines
 */
public class VariableDeclarationValidator extends AssignmentValidator implements Validator {
    // Errors
    private static final String ASSIGN_VARIABLE_TO_IT_SELF_ERR =
            "attempt to assign variable to itself upon declaration";

    /**
     * Validates that the current line is a syntactically correct Variable Declaration
     * @param lineToValidate line to validate.
     * @return true if the current line is a syntactically correct Variable Declaration
     */
    @Override
    public boolean validate(String lineToValidate) {
        isFinal = isFinal(lineToValidate.replaceFirst(";", ""));
        Matcher matcher = getMatcher(lineToValidate, VARIABLE_DECLARATION);
        boolean didFind = matcher.find();
        return ((didFind) && (matcher.start() == 0));
    }

    /**
     * out of a variable declaration line - extracts all data and creates a vector of Variables
     * @param line line to parse
     * @return Vector of SJavaVariables
     * @throws SyntaxException in case of wrongful variable assign attempt
     */
    public HashMap<SJavaVariable,String> getVars(String line) throws SyntaxException {
        String type = checkForType(line);
        // removes type from line and splits all assignments to array of strings
        String[] segments =
                line.replaceFirst(FINAL_MODIFIER + VAR_TYPES, "").split(",");
        HashMap<SJavaVariable,String> vars = new HashMap<>();
        for (String segment : segments) {
            // for every "x = y" string
            Matcher matcher = getMatcher(segment, VARIABLE_ASSIGNMENT);
            if (matcher.find()) {
                // splits into "x" and "y"
                String[] sides = segment.split("=");
                String rightHand;
                String name = sides[0].trim().replaceFirst(";", "");
                if (sides.length == 1){
                    rightHand = null;
                } else {
                    rightHand = sides[1].replaceFirst(";","").trim();
                }
                if (name.equals(rightHand)){
                    throw new SyntaxException(ASSIGN_VARIABLE_TO_IT_SELF_ERR);
                }
                // either way put right hand (y) as value at hashmap for further checks
                vars.put(new SJavaVariable(name, type, isFinal),rightHand);
            } else {
                // if value is only declared.
                vars.put(new SJavaVariable(segment.trim(), type, isFinal),null);
            }
        }
        return vars;
    }
}
