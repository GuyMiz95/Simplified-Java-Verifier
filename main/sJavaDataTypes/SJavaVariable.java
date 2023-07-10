package main.sJavaDataTypes;

import java.util.*;

/**
 * Represents a single variable, contains all the required metadata.
 */
public class SJavaVariable {

    private static final String INT_VALUES = "([-]|)[0-9]+";
    private static final String DOUBLE_VALUES = "([-]|)[0-9]+([.][0-9]+)*";
    private static final String BOOLEAN_VALUES = "true|false|([-]|)[0-9]+([.][0-9]+|)*";
    private static final String CHAR_VALUES = "'.'";
    protected static final String STRING_VALUES = "\".*\"";
    protected static final String LEGAL_VAR_NAME = "([A-Za-z]+|_+[A-Za-z0-9])\\w*";


    // List of different possible variable types.
    public static final String[] TYPES = {"boolean", "int", "double", "String", "char"};

    private static final Set<String> booleanMatches = new HashSet<>(Arrays.asList("boolean", "double", "int"));
    private static final Set<String> doubleMatches = new HashSet<>(Arrays.asList("double", "int"));

    // name of variable.
    private final String name;
    // type of variable.
    private final String type;
    // is variable declared final?
    private boolean isFinal;
    // does variable have any value assigned to it?
    private boolean hasValue;
    // optional types of variable.
    private List<String> types;

    /**
     * Constructor.
     *
     * @param name    name of variable.
     * @param type    type of variable.
     * @param isFinal indicates that the variable is final
     */
    public SJavaVariable(String name, String type, boolean isFinal) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
        this.hasValue = false;
        types = new ArrayList<>();
        types.add(type);
    }

    /**
     * Returns the name of the Variable
     *
     * @return Variable name
     */
    public String getName() {
        return name;
    }

    /**
     * Deters if variable is final.
     *
     * @return true if final, false otherwise.
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Deters if variable has a value.
     *
     * @return true if it has a value, false otherwise.
     */
    public boolean hasValue() {
        return hasValue;
    }

    /**
     * Returns the type of the SJavaVariable
     */
    public String getType() {
        return this.type;
    }

    public void setHasValue(boolean hasValue){
        this.hasValue = hasValue;
    }

    /**
     * Returns the matching type of given string part.
     * @param part part to identify its type.
     * @return type of part.
     */
    public static String getMatchingType(String part) {
        if (part.matches(INT_VALUES)) {
            return "int";
        } else if (part.matches(DOUBLE_VALUES)) {
            return "double";
        } else if (part.matches(BOOLEAN_VALUES)) {
            return "boolean";
        } else if (part.matches(CHAR_VALUES)) {
            return "char";
        } else if (part.matches(STRING_VALUES)) {
            return "String";
        } else if (part.matches(LEGAL_VAR_NAME)) {
            return "var";
        }
        return null;
    }

    /**
     * Checks if the given left data type can be assigned by the right data type
     * @param leftHandType to be assigned to
     * @param rightHandType to be assigned
     * @return true is the right-hand data-type could be legally translated to the left-hand data-type
     */
    public static boolean couldAssignValue(String leftHandType, String rightHandType){
        switch(leftHandType) {
            case "boolean":
                return (booleanMatches.contains(rightHandType));
            case "double":
                return (doubleMatches.contains(rightHandType));
            case "int":
                return (rightHandType.equals("int"));
            case "char":
                return (rightHandType.equals("char"));
            case "String":
                return (rightHandType.equals("String"));
        }
        return false;
    }
}
