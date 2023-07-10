package main;

import main.scopeLogic.*;
import main.validation.SyntaxException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class responsible for the general running of the Compilation checking process of the provided SJava file
 */
public class Sjavac {
    // Errors
    private static final String FILE_NOT_FOUND_ERROR = "The file name provided was not found!";

    // Possible outputs for a provided SJava file
    private static final int LEGAL_CODE_OUTPUT = 0;
    private static final int ILLEGAL_CODE_OUTPUT = 1;
    private static final int IO_ERROR_OUTPUT = 2;

    private static String link;

    /**
     * Main function running the program
     * @param args provided arguments. provided SJava file being in args[0]
     */
    public static void main(String[] args){
        link = args[0];

        if(link == null) {
            System.out.println(ILLEGAL_CODE_OUTPUT);
            return;
        }

        try {
            FileInputStream curFile = new FileInputStream(link);
            Sjavac.determineCompilable(curFile);

            System.out.println(LEGAL_CODE_OUTPUT);
        }
        catch (FileNotFoundException fe) {
            System.out.println(IO_ERROR_OUTPUT);
            System.err.println(FILE_NOT_FOUND_ERROR);
        }
        catch (SyntaxException | VariableAssignmentException | ConditionalException |
                MethodDefinitionException  | ReturnException | VariableDeclarationException se){
            System.out.println(ILLEGAL_CODE_OUTPUT);
            System.err.println(se);
        }
        catch (Exception e) {
            System.out.println(ILLEGAL_CODE_OUTPUT);

            /* Printing the Exception text */
            System.err.println(e);
        }
    }

    /**
     * Determines if the provided file is compilable according to the SJava specifications
     * @param curFile to be checked for compilability.
     * @throws Exception If the file is found to not be compilable.
     */
    private static void determineCompilable(FileInputStream curFile) throws Exception {
        MemoryCollector memoryCollector = new MemoryCollector();

        // Validating the global scope of each Method
        Scanner globalScanner = new Scanner(curFile);
        GlobalScopeValidator globalScopeValidator = new GlobalScopeValidator(memoryCollector);
        globalScopeValidator.fillGlobalTablesAndValidate(globalScanner);
        // Validating the local scope of each Method
        globalScanner.close();
        curFile = new FileInputStream(link);
        Scanner localScanner = new Scanner(curFile);
        LocalScopeValidator localScopeValidator = new LocalScopeValidator(memoryCollector, localScanner);

        String curLine;
        while (localScanner.hasNextLine()) {
            curLine = localScanner.nextLine();
            localScopeValidator.fillLocalTablesAndValidate(curLine, false);
        }
    }
}
