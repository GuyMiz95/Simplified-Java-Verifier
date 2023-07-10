package oop.ex6.main.scopeLogic;

import oop.ex6.main.sJavaDataTypes.SJavaMethod;
import oop.ex6.main.sJavaDataTypes.SJavaVariable;
import oop.ex6.main.validation.*;

import java.util.*;

/**
 * Class responsible for validating the compilability of the Global Scope of the provided SJava file
 */
public class GlobalScopeValidator {
    /**
     * Composition of MemoryCollector, for keeping the various data points associated with the Global
     * Scope validation process
     */
    private final MemoryCollector memoryCollector;

    /**
     * Constructor for the GlobalScopeValidator class
     * @param memoryCollector for saving the data with the associated Global Scope validation process
     */
    public GlobalScopeValidator(MemoryCollector memoryCollector) {
        this.memoryCollector = memoryCollector;
    }

    /**
     * Fills the globalVariableTable with the global Variables and the globalMethodsTable with the
     * Methods in the file.
     */
    public void fillGlobalTablesAndValidate(Scanner scanner)
            throws VariableAssignmentException, VariableDeclarationException,
            MethodDefinitionException, SyntaxException {
        /* We need to do several runs on the text, once for the global vars + methods,
         and then for each method we need to iterate in-side of it and activate the fillLocalTables on it */
        EmptyLineValidator emptyLineValidator = new EmptyLineValidator();
        CommentValidator commentValidator = new CommentValidator();

        /* There are 5 options for each line of the provided file for the global scope:
         1. Global Variable
         2. Global variable assignment
         3. Method and its scope
         4. Empty/Comment
         5. Invalid line - The file is uncompilable */
        while (scanner.hasNextLine()) {
            String curLine = scanner.nextLine();

            /* Global SjavacVariable, adds it to the Global SjavacVariable Table */
            boolean varDecBool = variableDeclarationAction(curLine);

            /* Global SjavacVariable assignment */
            boolean varAssignBool = variableAssignmentAction(curLine);

            /* Global Method, adds it to the Global Method Table */
            boolean methodDecBool = methodDeclarationAction(curLine, scanner);

            /* Two options:
             1. empty line, which is valid
             2. comment line
             3. anything else, which is invalid */
            boolean emptyLineBool = emptyLineValidator.validate(curLine);
            boolean commentLineBool = commentValidator.validate(curLine);

            /* Throw exception for line in global scope that isn't a global variable or part of a method
            * or an empty line/comment */
            if(!(varDecBool || varAssignBool || methodDecBool || emptyLineBool || commentLineBool)) {
                throw new SyntaxException("Line in the Global scope was not one of the valid lines");
            }

        }

    }

    /**
     * Checks if the value that was given is fit to be assigned to the Variable
     * @param sv Variable to be assigned to
     * @param curAssignValue Value to be assigned to the variable
     * @throws VariableAssignmentException The left/right variables do not exist or do not match
     */
    public void assignableValueCheck(SJavaVariable sv, String curAssignValue)
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
            SJavaVariable curAssignVar =
                    memoryCollector.checkVariableNameByGlobalVariableTable(curAssignValue);
            /* If the right-side's variable does not exist in the global table */
            if (curAssignVar == null) {
                throw new VariableAssignmentException("Assignment variable was not declared");
            }
            /* If the right-side's variable is not assigned a value */
            if(!curAssignVar.hasValue()) {
                throw new VariableAssignmentException("Assignment variable was not assigned a value");
            }
            /* If the right-side's variable type does not match the variable left-sides type  */
            if(!SJavaVariable.couldAssignValue(sv.getType(), curAssignVar.getType())) {
                throw new VariableAssignmentException("Tried to assign an incompatible data type");
            }
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
            throws VariableDeclarationException, VariableAssignmentException, SyntaxException {
        VariableDeclarationValidator variableDeclarationValidator = new VariableDeclarationValidator();

        /* Checks if the current line is a variable declaration */
        if(variableDeclarationValidator.validate(curLine)) {

            HashMap<SJavaVariable, String> sJavaVariablesDecPairs =
                    variableDeclarationValidator.getVars(curLine);

            for(SJavaVariable sv : sJavaVariablesDecPairs.keySet()) {

                /* Checks if there are two Variables of the same name */
                if(this.memoryCollector.checkVariableNameByGlobalVariableTable(sv.getName()) == null){
                    String curAssignValue = sJavaVariablesDecPairs.get(sv);
                    /* Checks if the right-hand side data type is present */
                    if(curAssignValue != null) {
                        assignableValueCheck(sv, curAssignValue);

                        sv.setHasValue(true);
                        this.memoryCollector.addGlobalVariable(sv);
                    }
                    /* In the case that the right-hand side is null */
                    else {
                        if (sv.isFinal()){
                            throw new VariableDeclarationException("Attempted to declare an empty final variable");
                        }

                        sv.setHasValue(false);
                        this.memoryCollector.addGlobalVariable(sv);
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
     * Checks the line for being a method declaration, and if it is, it will check it for being legally
     * compilable and save the method's related data.
     * @param curLine to check for a method declaration
     * @return true if the line is a compilable method declaration,
     * false if it is not a method declaration
     * @throws MethodDefinitionException Line is a syntactically valid method declaration but
     *                                                                     is not compilable for other reasons
     */
    private boolean methodDeclarationAction(String curLine, Scanner scanner)
            throws MethodDefinitionException, SyntaxException {
        MethodDefinitionValidator methodDefinitionValidator = new MethodDefinitionValidator();

        /* Checks if the line is a method declaration */
        if (methodDefinitionValidator.validate(curLine)) {
            SJavaMethod sJavaMethod = methodDefinitionValidator.getMethodData();

            /* Checks if there are no previous methods of the same name in the file */
            if(memoryCollector.getMethodFromName(sJavaMethod.getName()) != null) {
                throw new MethodDefinitionException("Tried to create a method with an already existing method's name");
            }

            /* Checks if the Method has an adequate number of open/closed curly brackets for the
             scope count */
            int numOfOpenScopes = 1;
            while (scanner.hasNextLine()) {
                curLine = scanner.nextLine();
                if (curLine.contains("{")) {
                    numOfOpenScopes++;
                } else if (curLine.contains("}")) {
                        numOfOpenScopes--;
                    // Local scope closes in a valid manner
                    if (numOfOpenScopes == 0) {
                        // Breaks the inner while scope that represents the local scope loop.
                        break;
                    }
                }
            }

            /* Exception thrown when the local scope is not properly closed */
            if (numOfOpenScopes > 0) {
                throw new MethodDefinitionException("Scope was not properly closed with the correct " +
                        "number of closing brackets");
            }

            this.memoryCollector.addMethod(methodDefinitionValidator.getMethodData());

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

        /* Checks if the current line is a variable assignment */
        if (variableAssignmentValidator.validate(curLine)) {
            Vector<String[]> curVariableNames = variableAssignmentValidator.getVariableAssignment(curLine);
            for (String[] curNamePair : curVariableNames) {
                SJavaVariable sv = memoryCollector.checkVariableNameByGlobalVariableTable(
                        curNamePair[0]);
                /* If the left var does not exist in the tables */
                if (sv == null) {
                    throw new VariableAssignmentException("Tried to assign to an undeclared variable");
                }
                /* If the left var is final and cannot be assigned */
                if (sv.isFinal()) {
                    throw new VariableAssignmentException("Tried to assign a value to a final variable");
                }
                /* If the right var is a var name or a data type */
                String curAssignmentVarType = SJavaVariable.getMatchingType(curNamePair[1]);
                if(curAssignmentVarType == null) {
                    throw new VariableAssignmentException("Assignment value in the declaration is not of a valid type");
                }

                assignableValueCheck(sv, curNamePair[1]);

                sv.setHasValue(true);
            }
            return true;
        }
        return false;
    }
}
