package main.validation;

import main.sJavaDataTypes.SJavaVariable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This abstract class represents all the validators relevant to assignment of values,
 * to both methods and variables.
 */
public abstract class AssignmentValidator implements Validator {
    //** Regex patterns **//
    //variable declaration and initiation patterns:
    static final String LINE_START = "^\\s*";
    static final String END_OF_LINE = "\\s*;\\s*$";
    protected static final String LEGAL_VAR_NAME = "(?:[A-Za-z][\\w]*|_[A-Za-z0-9]+)\\b";
    private static final String FINAL_PATTERN = "[\\s\\t]*final\\s+";
    public static final String FINAL_MODIFIER = "(\\s*|" + FINAL_PATTERN + ")";
    private static final String EQUALS = "(|\\s*[=]\\s*(";
    protected static final String COMMA = "\\s*([,]\\s*";
    // Different types declaration patterns:
    protected static final String INT_DECLARATION = "int" + "\\s+" + LEGAL_VAR_NAME;
    protected static final String DOUBLE_DECLARATION = "double" + "\\s+" + LEGAL_VAR_NAME;
    protected static final String BOOLEAN_DECLARATION = "boolean" + "\\s+" + LEGAL_VAR_NAME;
    protected static final String CHAR_DECLARATION = "char" + "\\s+" + LEGAL_VAR_NAME;
    protected static final String STRING_DECLARATION = "String" + "\\s+" + LEGAL_VAR_NAME;
    // Different types possible values patterns:
    protected static final String INT_VALUES = "([-]|)[0-9]+";
    protected static final String DOUBLE_VALUES = "([-]|)[0-9]+([.][0-9]+)*";
    protected static final String BOOLEAN_VALUES = "true|false|([-]|)[0-9]+([.][0-9]+|)*";
    protected static final String CHAR_VALUES = "'.'";
    protected static final String STRING_VALUES = "\".*\"";
    // Different types assignment patterns:
    private static final String INT_ASSIGNMENT = EQUALS + INT_VALUES + "|" + LEGAL_VAR_NAME + "))";
    private static final String DOUBLE_ASSIGNMENT = EQUALS + DOUBLE_VALUES + "|" + LEGAL_VAR_NAME + "))";
    private static final String CHAR_ASSIGNMENT = EQUALS + CHAR_VALUES + "|" + LEGAL_VAR_NAME + "))";
    private static final String BOOLEAN_ASSIGNMENT = EQUALS + BOOLEAN_VALUES + "|" + LEGAL_VAR_NAME + ")*)";
    private static final String STRING_ASSIGNMENT = EQUALS + STRING_VALUES + "|" + LEGAL_VAR_NAME + "))";
    // Different types multi-assignment patterns:
    private static final String FOLLOW_UP_INT = COMMA + LEGAL_VAR_NAME + INT_ASSIGNMENT + ")*";
    private static final String FOLLOW_UP_DOUBLE = COMMA + LEGAL_VAR_NAME + DOUBLE_ASSIGNMENT + ")*";
    private static final String FOLLOW_UP_CHAR = COMMA + LEGAL_VAR_NAME + CHAR_ASSIGNMENT + ")*";
    private static final String FOLLOW_UP_BOOLEAN = COMMA + LEGAL_VAR_NAME + BOOLEAN_ASSIGNMENT + "\\s*)*";
    private static final String FOLLOW_UP_STRING = COMMA + LEGAL_VAR_NAME + STRING_ASSIGNMENT + ")*";
    // Different types total pattern:
    private static final String INT_PATTERN = LINE_START + FINAL_MODIFIER + INT_DECLARATION + INT_ASSIGNMENT
            + FOLLOW_UP_INT + END_OF_LINE;

    protected static final String DOUBLE_PATTERN = LINE_START + FINAL_MODIFIER + DOUBLE_DECLARATION +
            DOUBLE_ASSIGNMENT + FOLLOW_UP_DOUBLE + END_OF_LINE;

    private static final String CHAR_PATTERN = LINE_START + FINAL_MODIFIER + CHAR_DECLARATION +
            CHAR_ASSIGNMENT + FOLLOW_UP_CHAR + END_OF_LINE;

    private static final String BOOLEAN_PATTERN = LINE_START + FINAL_MODIFIER + BOOLEAN_DECLARATION +
            BOOLEAN_ASSIGNMENT + FOLLOW_UP_BOOLEAN + END_OF_LINE;

    private static final String STRING_PATTERN = LINE_START + FINAL_MODIFIER + STRING_DECLARATION +
            STRING_ASSIGNMENT + FOLLOW_UP_STRING + END_OF_LINE;

    // Total pattern of all variable types:
    protected static final String VARIABLE_DECLARATION = INT_PATTERN + "|" + DOUBLE_PATTERN + "|" +
            BOOLEAN_PATTERN + "|" + CHAR_PATTERN + "|" + STRING_PATTERN;


    protected static final String DOUBLE_QUOTE_STRING_VALUES = "\"[^\"]*\"";

    protected static final String VAR_TYPES = "(int|boolean|char|double|String)\\s+";

    protected static final String VARIABLE_ASSIGNMENT = EQUALS + LEGAL_VAR_NAME + "|" + INT_VALUES +
            "|" + DOUBLE_VALUES + "|" + BOOLEAN_VALUES + "|" + CHAR_VALUES + "|" + STRING_VALUES + "|" +
            DOUBLE_QUOTE_STRING_VALUES + "))";

    protected static final String LEGAL_VALUES = INT_VALUES + "|" + DOUBLE_VALUES + "|" + CHAR_VALUES + "|" +
            CHAR_VALUES + "|" + STRING_VALUES;

    protected static final String MULTI_ASSIGNMENT_PATTERN = "(" + LEGAL_VAR_NAME + "\\s*=\\s*" +
            LEGAL_VALUES + ")+";

    // empty pattern.
    protected static final String EMPTY_PATTERN = "[\\s\\t]*";

    // current line on parsing.
    protected String curLine;

    // is inspected variable final?
    protected boolean isFinal;

    /**
     * Checks if inspected variable is final.
     *
     * @param line line to check.
     * @return true if variable is final, false otherwise.
     */
    protected boolean isFinal(String line) {
        Pattern pattern = Pattern.compile(FINAL_PATTERN);
        Matcher match = pattern.matcher(line);
        if (match.find()) {
            curLine = match.replaceFirst("");
            return true;
        }
        curLine = line;
        return false;
    }

    /**
     * Checks for the variable type.
     *
     * @return type of variable.
     * @throws Exception in case variable is final and has no type.
     */
    protected String checkForType(String curString) {
        String type = null;
        if (isFinal) {
            curString = curString.replaceFirst(FINAL_PATTERN, "");
        }
        for (int i = 0; i < SJavaVariable.TYPES.length; i++) {
            Matcher match = getMatcher(curString, EMPTY_PATTERN + SJavaVariable.TYPES[i]);
            if (match.find() && match.start() == 0) {
                type = SJavaVariable.TYPES[i];
                break;
            }
        }
        return type;
    }
}
