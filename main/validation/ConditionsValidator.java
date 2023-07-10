package oop.ex6.main.validation;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents condition validator, validates whether there's any condition (if\else\while) in given line.
 */
public class ConditionsValidator extends AssignmentValidator implements Validator {
    // Regex patterns
    private static final String VALID_HEAD = "[\\s\\t]*if[\\s\\t]*[(]|[\\s\\t]*while[\\s\\t]*[(]";
    private static final String VALID_TAIL = "[)][\\s\\t]*[{][\\s\\t]*$";
    private static final String VALID_COND = "true|false|[-]?\\d+.\\d*|[-]?\\d+";
    /* And or Or */
    private static final String BINARY_OP = "[|]{2}|[&]{2}";

    // Errors
    private static final String COND_SYNTAX_ERR_MSG = "Illegal condition syntax: ";
    private static final String BIN_OP_ERR = "Binary operators in beginning or end of condition";
    private static final String ONE_OR_MORE_INVALID_COND = "one or more conditions is of invalid syntax";

    /**
     * Validates that the current line is a syntactically correct Conditional
     * @param lineToValidate line to validate.
     * @return true if the current line is a syntactically correct Conditional
     */
    @Override
    public boolean validate(String lineToValidate) {
        Matcher headMatcher = getMatcher(lineToValidate, VALID_HEAD);
        if (!headMatcher.find() || headMatcher.start() != 0) {
            return false;
        }
        lineToValidate = headMatcher.replaceAll("").replaceAll(" ", "");
        Matcher tailMatcher = getMatcher(lineToValidate, VALID_TAIL);
        if (!tailMatcher.find()) {
            return false;
        }
        return true;
    }

    /**
     * Inspects conditions, makes sure all conditions are valid and returns a vector of variable names
     * found within conditions.
     *
     * @param lineToValidate line to parse.
     * @return vector of variable names (MIGHT BE empty)
     * @throws Exception in case of invalid conditions syntax.
     */
    public Vector<String> inspectConditions(String lineToValidate) throws SyntaxException {
        // leaves line with only conditions inside;
        lineToValidate = lineToValidate.replaceAll(VALID_HEAD + "|" + VALID_TAIL, "");

        if (lineToValidate.startsWith("||") || lineToValidate.endsWith("||") ||
                lineToValidate.startsWith("&&") || lineToValidate.endsWith("&&")) {
            // binary operators in beginning or end of condition.
            throw new SyntaxException(COND_SYNTAX_ERR_MSG + BIN_OP_ERR);
        }
        Vector<String> condVarNames = new Vector<>();
        Pattern binaryOperatorPattern = Pattern.compile(BINARY_OP);
        Pattern validPattern = Pattern.compile(EMPTY_PATTERN + VALID_COND + EMPTY_PATTERN);
        Pattern validVarName = Pattern.compile(EMPTY_PATTERN + LEGAL_VAR_NAME + EMPTY_PATTERN);
        String[] lineParts = lineToValidate.split(String.valueOf(binaryOperatorPattern));
        for (String condition : lineParts) {
            condition = condition.trim();
            Matcher condMatcher = validPattern.matcher(condition);
            if (condMatcher.matches()) {
                // if condition matches syntax demands
                continue;
            }
            Matcher varNameMatcher = validVarName.matcher(condition);
            if (varNameMatcher.matches()) {
                condVarNames.add(condition);
                /**
                 * if it's not a specific matching type,
                 * then check if it's a legal var name, if so add it to vector.
                 */
            } else {
                // condition is of invalid syntax.
                throw new SyntaxException(COND_SYNTAX_ERR_MSG + ONE_OR_MORE_INVALID_COND);
            }
        }
        return condVarNames;
    }
}
