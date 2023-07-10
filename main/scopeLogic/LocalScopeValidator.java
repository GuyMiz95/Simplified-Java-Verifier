package main.scopeLogic;

import main.sJavaDataTypes.SJavaMethod;
import main.sJavaDataTypes.SJavaVariable;
import main.validation.*;

import java.util.*;

/**
 * Class responsible for validating the compilability of the Local Scopes of the provided SJava file
 */
public class LocalScopeValidator {
    /**
     * Variable table for the current local scope
     */
    private Vector<SJavaVariable> localSJavaVariableTable;
    /**
     * Scanner of the stream that provides the lines of text of the current Local Scope
     */
    private final Scanner scopeScanner;
    /**
     * Composition of MemoryCollector, for keeping the various data points associated with the Local
     * Scopes validation process
     */
    private final MemoryCollector memoryCollector;
    // todo??
    private boolean methodParamsAccepted = false;
    /**
     * For internal use. Represents all the variables that have been assigned a value in the Local Scope
     */
    private final ArrayList<SJavaVariable> changedVariablesInScope = new ArrayList<>();

    /**
     * Constructor for the LocalScopeValidator class
     * @param memoryCollector for saving the data with the associated Local Scope validation process
     * @param scopeScanner Scanner of the stream that provides the lines of text of the current Local Scope
     */
    public LocalScopeValidator(MemoryCollector memoryCollector, Scanner scopeScanner) {
        this.scopeScanner = scopeScanner;

        this.memoryCollector = memoryCollector;
    }

    /**
     * Fills the Method parameters into the local table of Variables(since the Method's parameters are
     * part of the local variables)
     * @param curLine Method definition line
     * @throws SyntaxException Thrown if the given parameters of the method are syntactically invalid
     */
    private boolean fillMethodParams(String curLine) throws SyntaxException{
        /* Indicates that the current local scope is supposed to be a method and thus should be checked
         * at the beginning for having a valid method declaration */
        MethodDefinitionValidator methodDefinitionValidator = new MethodDefinitionValidator();
        if(methodDefinitionValidator.validate(curLine)) {
            /* Adding the Method params to the local table of variables */
            if (!methodParamsAccepted){
                Vector<SJavaVariable> methodParams = methodDefinitionValidator.getMethodData().getParams();
                if (methodParams != null){
                    for(SJavaVariable param : methodParams) {
                        param.setHasValue(true);
                    }
                    this.localSJavaVariableTable.addAll(methodParams);
                    methodParamsAccepted = true;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Fills the Local Scope variable table and validates the compilability of the current Local Scope
     * @param curLine Beginning line of the current Local Scope
     * @param isNonMethodLocalScope false if the local scope is of a method, true otherwise
     */
    public void fillLocalTablesAndValidate(String curLine, boolean isNonMethodLocalScope)
            throws VariableAssignmentException, VariableDeclarationException, ReturnException,
            MethodDefinitionException, ConditionalException, SyntaxException {
        EndOfBlockValidator endOfBlockValidator = new EndOfBlockValidator();
        ReturnValidator returnValidator = new ReturnValidator();
        EmptyLineValidator emptyLineValidator = new EmptyLineValidator();
        CommentValidator commentValidator = new CommentValidator();
        boolean returnStatement = isNonMethodLocalScope;

        this.localSJavaVariableTable = new Vector<>();

        /* Adding the localVariableTable to the memoryCollector LinkedList of Scope Tables */
        this.memoryCollector.getLocalVariableTables().add(this.localSJavaVariableTable);

        if(!isNonMethodLocalScope) {
            if(!fillMethodParams(curLine)) {
                return;
            }
        }

        curLine = scopeScanner.nextLine();

        while(!(endOfBlockValidator.validate(curLine))) {
            /* Line is empty or a comment, so nothing needs to happen */
            if (!emptyLineValidator.validate(curLine) && !commentValidator.validate(curLine)) {
                returnStatement = isNonMethodLocalScope;

                /* Local SjavacVariable, adds it to the Local SjavacVariable Table */
                boolean varDecBool= variableDeclarationAction(curLine);

                /* Local SjavacVariable assignment */
                boolean varAssignBool = variableAssignmentAction(curLine);

                /* Checks a condition and it's scopes */
                boolean conditionsBool = conditionsAction(curLine);

                /* Checks a method call */
                boolean methodCallBool = methodCallAction(curLine);

                /* If there is a valid return statement, it indicates it for the next line with
                * the returnStatement */
                boolean returnBool = returnValidator.validate(curLine);
                if (returnBool) {
                    returnStatement = true;
                    // If curLine is a return, it is valid and you can continue

                    // Can there be return, then empty/comment Line, then }??
                }
                if(!(varDecBool || varAssignBool || conditionsBool || methodCallBool || returnBool)) {
                    /* If curLine is none of these types, it is an invalid line and there needs to be a
                    * compilation error */
                    throw new SyntaxException("Line in the Local scope was not one of the valid lines");
                }
            }

            curLine = this.scopeScanner.nextLine();
        }

        /* The variables that have been assigned a value and were unassigned before the scope must be
         * redefined as unassigned when we finish running this local scope */
        for (SJavaVariable sv : this.changedVariablesInScope) {
            sv.setHasValue(false);
        }

        /* Removing the localVariableTable from the possible scopes and returning whether the scope is
        * compilable in terms of the final return statement. */
        this.memoryCollector.getLocalVariableTables().remove(this.localSJavaVariableTable);

        /* Checks if return statement is right before the end of the scope */
        if(!returnStatement && !isNonMethodLocalScope) {
            throw new ReturnException("Lack of return statement before the final end line of the method");
        }
        methodParamsAccepted = false;
    }

    /**
     * Checks if the value that was given is an existing and assigned variable that can be further
     * assigned by-value to the given Variable.
     * @param sv Variable to be assigned to
     * @param curAssignValue Variable name of the variable to be assigned to the input Variable
     * @throws VariableAssignmentException The left/right variables do not exist or do not match
     */
    private void assignableVariableCheck(SJavaVariable sv, String curAssignValue)
            throws VariableAssignmentException {
        SJavaVariable curAssignVar = memoryCollector.checkVariableNameByVariableTables(curAssignValue);
        /* If the right-side's variable does not exist in the tables */
        if (curAssignVar == null) {
            curAssignVar = memoryCollector.checkVariableNameByGlobalVariableTable(curAssignValue);
            if (curAssignVar == null) {
                throw new VariableAssignmentException("Assignment variable was not declared");
            }
        }
        /* If the right-side's variable is not assigned a value */
        if(!curAssignVar.hasValue()) {
            throw new VariableAssignmentException("Assignment variable was not assigned a value");
        }
        /* If the right-side's variable type does not match the variable left-sides type  */
        if (!SJavaVariable.couldAssignValue(sv.getType(), curAssignVar.getType())) {
            throw new VariableAssignmentException("Tried to assign an incompatible data type");
        }
    }

    /**
     * Checks if the value that was given is fit to be assigned to the Variable
     * @param sv Variable to be assigned to
     * @param curAssignValue Value to be assigned to the variable
     * @throws VariableAssignmentException The left/right variable and value do not exist or do not match
     */
    private void assignableValueCheck(SJavaVariable sv, String curAssignValue)
            throws VariableAssignmentException{
        /* If the right var is a var name or a data type */
        String curAssignmentVarType = SJavaVariable.getMatchingType(curAssignValue);
        if(curAssignmentVarType == null) {
            throw new VariableAssignmentException("Assignment value in the declaration is not of a valid type");
        }

        /* If the right-side is not a variable */
        if (!curAssignmentVarType.equals("var")) {
            /* If the right-side's data type does not match the left-sides variable type */
            if(!SJavaVariable.couldAssignValue(sv.getType(), curAssignmentVarType)) {
                throw new VariableAssignmentException("Tried to assign an incompatible data type");
            }
        }
        else {
            assignableVariableCheck(sv, curAssignValue);
        }
    }

    /**
     * Checks the line for being a variable declaration, and if it is, it will check it for being legally
     * compilable and save the variable's related data.
     * @param curLine to check for a variable declaration
     * @return true if the line is a compilable variable declaration,
     * false if it is not a variable declaration
     * @throws VariableDeclarationException Line is a syntactically valid variable declaration but
     *                                                                     is not compilable for other reasons
     */
    private boolean variableDeclarationAction(String curLine)
            throws SyntaxException, VariableDeclarationException, VariableAssignmentException {
        VariableDeclarationValidator variableDeclarationValidator = new VariableDeclarationValidator();

        /* Checks if the current line is a variable declaration */
        if(variableDeclarationValidator.validate(curLine)) {

            HashMap<SJavaVariable, String> sJavaVariablesDecPairs =
                    variableDeclarationValidator.getVars(curLine);

            for(SJavaVariable sv : sJavaVariablesDecPairs.keySet()) {
                /* Checks if there are two Variables of the same name */
                if(this.memoryCollector.checkVariableNameByCurLocalTable(sv.getName()) == null) {
                    this.localSJavaVariableTable.add(sv);

                    String curAssignValue = sJavaVariablesDecPairs.get(sv);
                    /* Checks if the right-hand side data type is present */
                    if(curAssignValue != null) {
                        assignableValueCheck(sv, curAssignValue);

                        sv.setHasValue(true);
                        this.localSJavaVariableTable.add(sv);
                    }
                    /* In the case that the right-hand side is null */
                    else {
                        if (sv.isFinal()){
                            throw new VariableDeclarationException("Attempted to declare an empty final variable");
                        }

                        sv.setHasValue(false);
                        this.localSJavaVariableTable.add(sv);
                    }

                }
                else {
                    throw new VariableDeclarationException("Tried to declare another variable of the same name in the scope");
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks the line for being a variable assignment, and if it is, it will check it for being legally
     * compilable and save the variable's related data.
     * @param curLine to check for a variable assignment
     * @return true if the line is a compilable variable assignment,
     * false if it is not a variable assignment
     * @throws VariableAssignmentException Line is a syntactically valid variable assignment but
     *                                                                     is not compilable for other reasons
     */
    private boolean variableAssignmentAction(String curLine) throws VariableAssignmentException {
        VariableAssignmentValidator variableAssignmentValidator = new VariableAssignmentValidator();

        if (variableAssignmentValidator.validate(curLine)) {
            Vector<String[]> curVariableNames = variableAssignmentValidator.getVariableAssignment(curLine);
            for (String[] curNamePair : curVariableNames) {
                SJavaVariable sv = memoryCollector.checkVariableNameByVariableTables(curNamePair[0]);
                // If the left var does not exist in the tables
                if (sv == null) {
                    sv = memoryCollector.checkVariableNameByGlobalVariableTable(curNamePair[0]);
                    if (sv == null) {
                        throw new VariableAssignmentException("Tried to assign to an undeclared variable");
                    }
                }
                // If the left var is final and cannot be assigned
                if (sv.isFinal()) {
                    throw new VariableAssignmentException("Tried to assign a value to a final variable");
                }

                assignableValueCheck(sv, curNamePair[1]);

                /* curVar is assigned a value and is added to the changedVariablesInScope ArrayList which
                 * will indicate that at the end of the run on the scope, these variables need to be changed
                 * from being defined as having a value */
                if(!sv.hasValue()) {
                    sv.setHasValue(true);
                    changedVariablesInScope.add(sv);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks the line for being a conditional, and if it is, it will check it for being legally
     * compilable by checking the arguments of the conditional. A conditional opens it's own Local Scope and
     * thus the validity of the conditional's compilability is also determined by the compilability of it's
     * Local Scope.
     * @param curLine to check for a conditional
     * @return true if the line is a compilable conditional, false if it is not a conditional
     * @throws ConditionalException Line is a syntactically valid conditional but it either provided an
     * invalid type of variable or has an invalid inner Local Scope
     */
    private boolean conditionsAction(String curLine)
            throws SyntaxException, ConditionalException, VariableDeclarationException,
            MethodDefinitionException, VariableAssignmentException, ReturnException {
        ConditionsValidator conditionsValidator = new ConditionsValidator();

        /* Checks if the current line is a conditional */
        if (conditionsValidator.validate(curLine)) {
            Vector<String> curConditionVars = conditionsValidator.inspectConditions(curLine);
            /* Checks if the variables exist and are assigned a value */
            for (String curVar : curConditionVars) {
                SJavaVariable sv = this.memoryCollector.checkVariableNameByVariableTables(curVar);
                /* If there is no variable of this name */
                if(sv == null) {
                    sv = memoryCollector.checkVariableNameByGlobalVariableTable(curVar);
                    if (sv == null) {
                        throw new ConditionalException("Tried to refer to an undeclared variable as a condition");
                    }
                }
                /* If the variable has no value*/
                if(!sv.hasValue()) {
                    throw new ConditionalException("Tried to refer to an unassigned variable as a condition");
                }
                /* If the type is not a boolean, double or int, it is not valid */
                if(!(sv.getType().equals("boolean") || sv.getType().equals("int") ||
                        sv.getType().equals("double"))) {
                    throw new ConditionalException("Valid conditions are of the boolean, int and double data types");
                }
            }

            /* If curLine is a condition, make a new ConditionalScopeValidator and validate(recursion) */
            LocalScopeValidator localScopeValidator =
                    new LocalScopeValidator(memoryCollector, scopeScanner);

            localScopeValidator.fillLocalTablesAndValidate(curLine, true);

            return true;
        }
        return false;
    }

    /**
     * Checks the line for being a method call, and if it is, it will check it for being legally
     * compilable and save the method's related data.
     * @param curLine to check for a method call
     * @return true if the current line is a valid method call, false if it not a method call
     * @throws MethodDefinitionException Line is a syntactically valid method call but it either provided an
     * invalid type of variable, the wrong number of variables, or the method in invalid in itself.
     */
    private boolean methodCallAction(String curLine)
            throws MethodDefinitionException, VariableAssignmentException {
        MethodCallValidator methodCallValidator = new MethodCallValidator();

        /* Checks if the current line is a method call */
        if (methodCallValidator.validate(curLine)) {
            List<String> curMethodCallParams =  methodCallValidator.getMethodParams();

            /* Checks if the method exists */
            String methodName = curMethodCallParams.get(0);
            curMethodCallParams.remove(0);

            SJavaMethod curMethod = this.memoryCollector.getMethodFromName(methodName);
            if(curMethod == null) {
                throw new MethodDefinitionException("Tried to refer to an undeclared method");
            }

            /* Checks if the number of parameters is the same for the method definition and the call */
            Vector<SJavaVariable> curMethodParams = curMethod.getParams();
            if (curMethodParams == null){
                if (curMethodCallParams.size() > 0){
                    throw new MethodDefinitionException("Invalid number of parameters in the method call");
                }
            } else {
                if(curMethod.getParams().size() != curMethodCallParams.size()) {
                    throw new MethodDefinitionException("Invalid number of parameters in the method call");
                }
            }

            /* Going over each parameter in the method call and checking the type versus the method's
            * param type at that position */
            for (int i = 0; i < curMethodCallParams.size(); i++) {
                assignableValueCheck(curMethodParams.get((i)), curMethodCallParams.get(i));
            }
        return true;
        }

        return false;
    }
}
