# Simplified-Java-Verifier
A verifier tool, able to verify the validity of a Java code, with simplified syntax

### =      File description     =

package main contains:

    Sjavac.java - Class responsible for the general running of the Compilation checking
        process of the provided SJava file.
		
    package scopeLogic:
        ConditionalException.java - Exception thrown for invalid conditionals.
		
        GlobalScopeValidator.java - Class responsible for validating the compilability of the Global
            Scope of the provided SJava file.
			
        LocalScopeValidator.java - Class responsible for validating the compilability of the Local
            Scopes of the provided SJava file.
			
        MemoryCollector.java - Class responsible for the collection of the various data in the compiler
            scopes for checking the various possible compilation problems.
			
        MethodDefinitionException.java - Exception thrown for invalid Method calls and declarations.
		
        ReturnException.java - Exception thrown for invalid returns.
		
        VariableAssignmentException.java - Exception thrown for invalid Variable assignments.
		
        VariableDeclarationException.java - Exception thrown for invalid Variable declarations.
		
    package sJavaDataTypes:
        SJavaMethod.java - Class representing a Method under the SJava specifications.
		
        SJavaVariable.java - Represents a single variable, contains all the required metadata.
		
    package validation:
        AssignmentValidator.java - Abstract class which represents all the validators relevant to assignment
            of values, to both methods and variables.
			
        CommentValidator.java - Represents a comment validator - checking if given line has a comment in it.
		
        ConditionsValidator.java - Represents condition validator, validates whether there's any condition
            (if\else\while) in given line.
			
        EmptyLineValidator.java - Represents an empty line validator, validates whether there's an empty line.
		
        EndOfBlockValidator.java - Represents an end-of-block validator, validates whether
            there's an end-of-block in given line.
			
        EndOfLineValidator.java - Represents an end-of-line validator, validates whether there's an
            end-of-line in given line.
			
        MethodCallValidator.java - Represents a method call validator, validates whether there's a method
            call in given line.
			
        MethodDefinitionValidator.java - Represents a method definition validator, validates whether
            there's a method definition in given line.
			
        ReturnValidator.java - Represents a return keyword validator.
		
        Validator.java - An interface which represents a validator, an object capable of validating a
            unique pattern in given line.
			
        VariableAssignmentValidator.java - Responsible for validating and extracting data from Variable
            Assignment lines.
			
        VariableDeclarationValidator.java - Responsible for validating and extracting data from Variable
            Declaration lines

### =  Design & Implementation  =

When designing and implementing this project, our main principle was to be able to create a code that is as
modular and expandable as possible, yet requires minimal existing code tampering to be expanded.
To make that happen, we've decided to split the project's task into 2 main tasks:
the first task is to be able, given a single line to deter whether that given line is a valid SJava command
(according to given rules) and furthermore - be able to identify what type of command that line is.
the second task is to be able to combine the abilities to recognize the different commands and possible code
lines and integrate between them, that to make sure the different parts in the given code are compatible with
each other (using existing and matching variables and methods, for example - making sure each method called
is called with the matching parameters as it was defined with).
according to the aforementioned tasks, we've implemented 3 packages:
package validation is responsible for handling the first task, and is built of different validators that each
of them is capable of recognizing a specific command or type of code line.
package scopeLogic is responsible for handling the second task and combines the different validators into a
functioning code that is capable of receiving a code segment and deterring whether it is a valid piece of code
or not.
package sJavaDataTypes' purpose is to be able to orderly serve the required data of the existing variables
and methods, and is used as a common language for the code parts to interact with each other.
that way we've split a huge problem into smaller problems and that way managed to build our code upwards in
a way that will allow any other programmer to add new types of code validators with minimal change of existing
code.
That way, we've split our code into smaller, independent units, which allows the open-close principle.

We've also implemented a class called MemoryCollector, which hold all the functional data received from a
given code (e.g variables and methods) in format of SJavaMethod and SJavaVariable.
the MemoryCollector class is capable of dividing the different data into their relevant scope, and grants
access to that information for the scopeLogic classes which use it to verify their valid usage in other parts
of the code.

to effectively and efficiently handle and iterate all the different types and data, we've used a variety
of data structures accordingly (Vectors, Linked Lists & Array Lists).

### =           Tests           =

The following are tests which were made to proove the validity of our code:

	testFinalVarDeclaration.sjava - tests all final declaration of all types.
 
	testGlobalVarReAssignment.sjava - tests the re-assignment of a global variable.
 
	testIndentationProof.sjava - tests a valid piece of code that has indentation.
 
	testIfWhileConditions.sjava - tests a valid code which includes many if and while blocks.
 
	testInnerFunction.sjava - tests an inner function definition.
 
	testInvalidBrackets.sjava - tests an invalid code with un-matching amount of opening and closing brackets.
 
	testInvalidCommentType.sjava - tests an invalid type of comment.
 
	testInvalidConditionSyntax.sjava - tests an invalid condition syntax.
 
	testInvalidCondType.sjava - tests an invalid condition type.
 
	testInvalidIfConditions.sjava - tests invalid if condition structure.
 
	testInvalidMethodParam.sjava - tests an invalid method param at definition.
 
	testInvalidParamsAtCall.sjava - tests an invalid method param at method call.
 
	testInvalidParamType.sjava - tests an invalid type of parameter usage in function call.
 
	testInvalidReturnSyntax.sjava - tests an invalid return syntax.
 
	testInvalidTypeAssignment.sjava - tests an invalid type re-assignment.
 
	testInvalidVarDeclarationSyntax.sjava - tests an invalid variable declaration and assignment.
 
	testInvalidVarName.sjava - tests an invalid variable name given.
 
	testMultiVarDeclaration.sjava - tests a single-line multi-variable declaration.
 
	testVarDeclarationSpacing.sjava - tests a valid, yet very spaced code.
 
	testVarDeclarationTwice.sjava - tests a declaration of same variable twice in code.
