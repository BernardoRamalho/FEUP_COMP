## GROUP: 3E



Clara Gadelho, 201806309, 16, 25% 

Bernardo Ramalho, 201704334, 16, 25%

João Rosário, 201806334, 16, 25%

Leonor Gomes, 201806567, 16, 25%



Global Grade of the project: 16 



## SUMMARY: 


This project compiles and runs jmm files.
Our tool starts by generating the syntax tree, annotating the nodes and leafs of the tree with the information necessary to perform the following steps. Afterwards, it generates the JSON files and the required symbol tables. After this, it implements the semantic analysis and generates the Ollir code. Finally, it generates Jasmin Code and 
runs the code.






## DEALING WITH SYNTACTIC ERRORS: 

Syntatic erros are all handled by the parser of our tool which has a set of defined rules and some other otimizations, all of which throw errors when they find a bad sequence of tokens. Some of the otimizations made are on the NEG token which only accepts a boolean expression after it (used to accept multiplications and divisions too).



## SEMANTIC ANALYSIS: 
For this stage of processing done by our tool we have implemented visitors that go over the node tree and collect usefull information. This is done by a visitor Class that fills our version of the SymbolTable with information about each method, class, variable and other things that were on the jmm file; thus allowing us on the next visitor to be able to check for semantic errors in conditions (comparing a bool to an integer), errors with assignments (trying to assign a bool to an integer) and in array accesses where something like accessing the value "true" of an array "arr" was possible before (arr[true]).




## CODE GENERATION:

We fist start by converting the code given into Ollir. To do this we start by visiting the children of the Root node which are the imports and class node. We transform the imports into Ollir and then we visit the class children which will be the private fields and the methods. For each method we visit the assign nodes, if and else nodes, else nodes and dot method (method calls) nodes. All of them are transformed recursivelly to Ollir, this makes it so we can have assigns with various operations and method calls or if and elses with multiple nested conditions.  This also helps making sure that all the lines are in the correct order since the father node in only written after the child. For example, the assign node will first visit his children before creating the string representing it, in case there are nested operation. The method and class node start by writing their headers first, then appending their children and lastly they close the method or class.

After the Ollir translation, we process it and start doing the Jasmin translation. First, we translate, the class and super directives, then the private fields. Additionally, for each method, we translate it. Inside each method, we start by writing the method declaration, and then we translate instruction by instruction. While doing this, we calculate dynamically the local and stack limits for the method. After running over all the instructions, we add the limits and the strings of each instruction. Finally, we append the return if necessary and the .end method.



## TASK DISTRIBUTION: 

Syntatic Tree: Bernardo, João, Leonor

While Error Treatment: Clara

JSON and Symbol Tables: Bernardo, João

Semantic Analysis: Bernardo, João

Ollir (first part): Clara, Leonor

Ollir (second part): Bernardo, João

Jasmin (with .limit): Clara, Leonor

Tests: Clara, Bernardo, João, Leonor




## PROS: 

Ollir generation is implemented with recursion in mind;

The first syntatic error doesn't exit the program;

We use lookhead of 1;

The tree generation already filters some errors;

We optimize the way we load constants (we use iconst, bipush, sipush and ldc);

When we have something like if (var.bool && true.bool), we just load the var and compare it with ifne, not having the need to also load iconst_1.



## CONS:

There are some minor bugs;

Sometimes, our code generation has repeated labels.
