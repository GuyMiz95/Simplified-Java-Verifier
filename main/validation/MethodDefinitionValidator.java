package main.validation;

import main.sJavaDataTypes.SJavaMethod;
import main.sJavaDataTypes.SJavaVariable;

import java.util.*;

/**
 * Represents a method definition validator, validates whether there's a method definition in given line.
 */
public class MethodDefinitionValidator extends AssignmentValidator {

    /**
     * regex patterns
     */
    // valid function name pattern.
    private static final String NAME_PATTERN = "[\\s\\t]*[a-zA-Z]+[a-zA-Z_\\d]*";
    // valid function type pattern (including only void).
    private static final String VOID_PATTERN = "[\\s\\t]*void[\\s\\t]+";
    // open brackets pattern.
    private static final String OPEN_BRACKETS = "[\\s\\t]*[(]";
    // valid function head pattern.
    private static final String VAlID_HEAD = VOID_PATTERN + NAME_PATTERN + OPEN_BRACKETS;
    // remaining pattern that will include function parameters.
    private static final String PARAMS = "[^)]*";
    // valid tail pattern.
    private static final String VALID_TAIL = "[)][\\s\\t]*[{][\\s\\t]*$";
    private static final String LEGAL_VAR_DECLARATION = INT_DECLARATION + "|" + DOUBLE_DECLARATION + "|" + BOOLEAN_DECLARATION + "|" + CHAR_DECLARATION + "|" + STRING_DECLARATION;

    private static final String METHOD_DEF_ERR = "Illegal method definition syntax: ";

    private static final String TWO_SAME_PARAM_ERR =
            "function definition includes 2 or more parameters with same name";

    private static final String INVALID_VAR_TYPE = "invalid variable type";


    // params names.
    private static List<String> params = new ArrayList<>();
    // method name
    private static String methodName;

    /**
     * Validates that the current line is a syntactically correct Method definition
     * @param lineToValidate line to validate.
     * @return true if the current line is a syntactically correct Method definition
     */
    @Override
    public boolean validate(String lineToValidate) {
        if (!getMatcher(lineToValidate, VAlID_HEAD + PARAMS + VALID_TAIL).find()) {
            return false;
        }
        //get method name
        methodName = lineToValidate.split("\\(|\\s")[1].trim();
        //get method params
        String rawParams =
                lineToValidate.replaceFirst(VAlID_HEAD, "").replaceFirst(VALID_TAIL, "");
        params = Arrays.asList(rawParams.split(","));

        if (params.size() == 1) {
            if (params.get(0).matches("\\s*")) {
                return true;
            }
        }
        for (String param : params) {
            if (!param.trim().matches(LEGAL_VAR_DECLARATION)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check and handle parameters in method.
     *
     * @throws Exception in case of 2 parameters with same name.
     */
    public SJavaMethod getMethodData() throws SyntaxException {
        Map<String, SJavaVariable> methodParams = new HashMap<>();
        if (params.get(0).matches("\\s*")) {
            return new SJavaMethod(methodName, null);
        }
        String curParam;
        Vector<SJavaVariable> curParams = new Vector<>();
        for (String param : params) {
            curParam = param.trim();
            boolean isFinal = isFinal(curParam);
            String type = checkForType(curParam);
            if (type == null) {
                // invalid variable type exception
                throw new SyntaxException(METHOD_DEF_ERR + INVALID_VAR_TYPE);
            }
            //get parameter without type
            curLine = curLine.replaceFirst(type, "");
            String paramName = curLine.replaceFirst(type, "").trim();
            SJavaVariable newVar = new SJavaVariable(paramName, type, isFinal);
            if (methodParams.containsKey(paramName)) {
                // 2 params with same name exception
                throw new SyntaxException(METHOD_DEF_ERR + TWO_SAME_PARAM_ERR);
            }
            methodParams.put(paramName, newVar);

            curParams.add(newVar);
        }
        return new SJavaMethod(methodName, curParams);
    }
}
