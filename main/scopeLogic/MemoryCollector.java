package oop.ex6.main.scopeLogic;

import oop.ex6.main.sJavaDataTypes.SJavaMethod;
import oop.ex6.main.sJavaDataTypes.SJavaVariable;

import java.util.*;

/**
 * Class responsible for the collection of the various data in the compiler scopes for checking the
 * various possible compilation problems.
 */
public class MemoryCollector {
    /**
     * Global scope Variable table
     */
    private final Vector<SJavaVariable> globalVariableTable;
    /**
     * Global scope Method table
     */
    private final Vector<SJavaMethod> globalMethodsTable;
    /**
     * Local scope Variable table linked list that represents the various local scopes-within-scopes
     */
    private final LinkedList<Vector<SJavaVariable>> variableTables;

    /**
     * Constructor for the MemoryCollector class
     */
    public MemoryCollector() {
        this.globalVariableTable = new Vector<>();
        this.globalMethodsTable = new Vector<>();
        this.variableTables = new LinkedList<>();
    }

    /**
     * Adds a method to the global scope method table
     * @param sJavaMethod to add
     */
    public void addMethod(SJavaMethod sJavaMethod) {
        this.globalMethodsTable.add(sJavaMethod);
    }
    /**
     * Adds a variable to the global scope variable table
     * @param sJavaVariable to add
     */
    public void addGlobalVariable(SJavaVariable sJavaVariable) {
        this.globalVariableTable.add(sJavaVariable);
    }
    /**
     * @return the local variable table linked list
     */
    public LinkedList<Vector<SJavaVariable>> getLocalVariableTables() {
        return this.variableTables;
    }

    /**
     * Checks for the method by the provided name and returns it
     * @param methodName the method's name
     * @return SJavaMethod by that name, null if it does not exist in the global method table
     */
    public SJavaMethod getMethodFromName(String methodName) {
        for (SJavaMethod sv : this.globalMethodsTable) {
            if(sv.getName().equals(methodName)) {
                return sv;
            }
        }
        return null;
    }

    /**
     * Checks for the variable in the global scope by the provided name and returns it
     * @param variableName the variable's name
     * @return SJavaVariable by that name, null if it does not exist in the global variable table
     */
    public SJavaVariable checkVariableNameByGlobalVariableTable(String variableName) {
        for(SJavaVariable sv : this.globalVariableTable) {
            if(sv.getName().equals(variableName)) {
                return sv;
            }
        }

        return null;
    }

    /**
     * Checks for the variable in the local scopes linked list by the provided name, goes
     * through the whole linked list in a backwards fashion for all scopes that contain the
     * current local scope, and returns it if it exists
     * @param variableName the variable's name
     * @return SJavaVariable by that name, null if it does not exist in the local variable tables linked list
     * @link https://www.geeksforgeeks.org/iterate-a-linkedlist-in-reverse-order-in-java/
     */
    public SJavaVariable checkVariableNameByVariableTables(String variableName) {
        for (Iterator<Vector<SJavaVariable>> it = this.variableTables.descendingIterator(); it.hasNext(); ) {
            Vector<SJavaVariable> svVector = it.next();
            for(SJavaVariable sv : svVector) {
                if(sv.getName().equals(variableName)) {
                    return sv;
                }
            }
        }
        return null;
    }

    /**
     * Checks for the variable in the current local scope by the provided name and returns it
     * @param variableName the variable's name
     * @return SJavaVariable by that name, null if it does not exist in the current local variable table
     */
    public SJavaVariable checkVariableNameByCurLocalTable(String variableName) {
        for(SJavaVariable sv : this.variableTables.getLast()) {
            if(sv.getName().equals(variableName)) {
                return sv;
            }
        }

        return null;
    }
}
