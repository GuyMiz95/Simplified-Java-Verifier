package oop.ex6.main.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a method call validator, validates whether there's a method call in given line.
 */
public class MethodCallValidator implements Validator{

    // Regex patterns
    private static final String NAME_PATTERN  = "\\s*[a-zA-Z][a-zA-Z_\\d]*";

    private static final String PARENTHESES_PATTERN = "\\s*[(].*[)]$";

    private static final String VALID_HEAD = "\\s*[(]";

    private static final String VALID_TAIL = "[)]$";

    // List containing method data (name and params)
    private final List<String> methodParams = new ArrayList<>();

    /**
     * Validates that the current line is a syntactically correct Method call
     * @param lineToValidate line to validate.
     * @return true if the current line is a syntactically correct Method call
     */
    @Override
    public boolean validate(String lineToValidate) {
        lineToValidate = lineToValidate.replaceAll(";", "");
        if (!lineToValidate.matches(NAME_PATTERN + PARENTHESES_PATTERN)) {
            return false;
        }
        //extract method name
        String methodName = lineToValidate.split("\\(")[0].trim();
        //first element is method name
        methodParams.add(methodName);
        lineToValidate = lineToValidate.replaceFirst(NAME_PATTERN + VALID_HEAD, "");
        lineToValidate = lineToValidate.replaceFirst(VALID_TAIL,"");
        String[] parameters = lineToValidate.split(",");
        //adds method params.
        for (String param : parameters){
            if (!param.trim().equals("")) {
                methodParams.add(param.trim());
            }
        }
        return true;
    }

    /**
     * MethodParams getter
     * @return Method params.
     */
    public List<String> getMethodParams(){
        return methodParams;
    }
}
