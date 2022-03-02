import pt.up.fe.comp.jmm.analysis.table.*;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import java.util.ArrayList;

public class OllirVisitor extends AJmmVisitor<String, String>{
  
  private CustomSymbolTable symbolTable;
  private StringBuilder auxiliaryBuilder;
  private int auxiliaryVariables = 0;
  private int whileNumbers = 1;
  private int ifNumbers = 1;
  private Boolean recursiveDotMethod = false;
  private String assignVariableType = "V";
  private ArrayList<String> classVariables = new ArrayList<>();

  public OllirVisitor(CustomSymbolTable symbolTable){
    this.symbolTable = symbolTable;
    this.auxiliaryBuilder = new StringBuilder();
    addVisit("PARSER", this::visitParserDeclaration);
    addVisit("IMPORT", this::visitImportDeclaration);
    addVisit("CLASS", this::visitClassDeclaration);
    addVisit("OTHERMETHOD", this::visitOtherMethodDeclaration);
    addVisit("MAINMETHOD", this::visitMainMethodDeclaration);
    addVisit("ASSIGN", this::visitAssignDeclaration);
    addVisit("DOTMETHOD", this::visitDotMethodDeclaration);
    addVisit("IFELSE", this::visitIfElseDeclaration);
    addVisit("WHILE", this::visitWhileDeclaration);
  
  }

  private String visitParserDeclaration(JmmNode node, String str){
    String ollir = visitChildren(node, "");
    System.out.println("OLLIR: ");
    System.out.println(ollir);
    return ollir;
  }

  private String visitImportDeclaration(JmmNode node, String str){
    StringBuilder builder = new StringBuilder();
    String importStr = str + "import " + node.getChildren().get(0).get("Identifier") + ";\n";

    return importStr;
  }

  private String visitClassDeclaration(JmmNode node, String str){
    StringBuilder builder = new StringBuilder();
    // Get the class name
    String className = node.getChildren().get(0).get("Identifier");
    String extendsString = "";

    // Transform Extends
    if(node.getChildren().get(1).getKind().equals("EXTENDS")){
      extendsString = " extends " + node.getChildren().get(1).getChildren().get(0).get("Identifier");
    }

    // Transform private fields
    String privateFields = visitPrivateDeclartion(node, str + " ");
    String toAdd = className + extendsString + " {\n" + privateFields + "\n"+ str + ".construct " + className + "().V { \n" + str  + "  invokespecial(this, \"<init>\").V; \n  }\n";

    // Visit class methods
    builder.append(toAdd);
    builder.append(visitChildren(node, str));
    builder.append("\n}\n");

    return str.concat(builder.toString());

  }

  private String visitPrivateDeclartion(JmmNode node, String str){
    StringBuilder privateVar = new StringBuilder();
    privateVar.append("");
    
    // For each field save its name and type
    for(JmmNode child : node.getChildren()){
      if(child.getKind().equals("VARIABLE")){
        classVariables.add(child.getChildren().get(1).get("Identifier"));
        privateVar.append(str + ".field private " + child.getChildren().get(1).get("Identifier") + "." + typeToOllir(child.getChildren().get(0).get("type")) + ";\n");
      }
    }

    return privateVar.toString();
  }

  // Methods that are not main
  private String visitOtherMethodDeclaration(JmmNode node, String str){ 
    StringBuilder builder = new StringBuilder();
    
    // Get Method Name
    String methodName = node.get("MethodName");
    builder.append("\n  .method public " + methodName + "(");
    auxiliaryVariables = 0;
    
    // Transform the arguments
    if(node.getAttributes().contains("args")){
      String[] args = node.get("args").split(",");

      for(int i = 0; i < args.length; i++){
        builder.append(args[i] + ".");

        String type;
        if(node.getChildren().get(i).getKind().equals("TYPE"))
          type = node.getChildren().get(i).get("type");
        else 
          type = node.getChildren().get(i).get("Identifier");

        builder.append(typeToOllir(type));

        if(i != args.length - 1)
          builder.append(", ");      
      }
    }

    // Transform return type
    String returnType = typeToOllir(node.get("ReturnType"));
 
    builder.append(")." + returnType +  " {\n");

    // Transform instructions of the method
    builder.append(visitChildren(node, str));

    JmmNode returnNode = node.getChildren().get(node.getNumChildren()-1).getChildren().get(0);

    // If return node has more children, then it is need to create auxiliary variables 
    if(returnNode.getNumChildren() > 0){
      String aux = createAuxVariables(returnNode, str);

      builder.append(aux + "    ret." + returnType + " aux" + auxiliaryVariables + "." + returnType + ";\n  }\n");
      return builder.toString();
    }

    // If the return node is an identifier, check if it is a private field
    if(returnNode.getKind().equals("IDENTIFIER") && classVariables.contains(returnNode.get("Identifier"))){
      String aux = str + "aux" + ++auxiliaryVariables + "." + returnType + " :=." + returnType + " getfield(this," + returnNode.get("Identifier") + "." + returnType + ")." + returnType + ";\n";
      builder.append(aux + str + "ret." + returnType + " aux" + auxiliaryVariables + "." + returnType + ";\n  }\n");
      return builder.toString();

    }

    String returnVar = operationToOllir(returnNode, str);

    String returnstr = str + "  ret." + returnType + " " + returnVar;


    builder.append(returnstr + "  }\n"); 
    return str + builder.toString();
  }

  private String visitMainMethodDeclaration(JmmNode node, String str){ //tested
    // Main Method is always the same
    String main = "\n .method public static main(args.array.String).V { \n";
    StringBuilder builder = new StringBuilder(main);

    auxiliaryVariables = 0;
    
    // Visit what it is done in main
    builder.append(visitChildren(node, str));
    builder.append(str + "ret.V;\n" +"  }\n");

    return str + builder.toString();
  }

  private String visitAssignDeclaration(JmmNode node, String str){
    StringBuilder builder = new StringBuilder();
    String varName = "";
    String typeOllir = "";
    String putField = "";

    // Transform the left side of the assign (it can only be identifier or array access)
    if (node.getChildren().get(0).getKind().equals("IDENTIFIER")){
      ClassMethod parentMethod = symbolTable.getParentMethod(node);
      Type type = symbolTable.getVariableType(parentMethod.getName(), node.getChildren().get(0).get("Identifier"));

      // Get the type and variable name
      typeOllir = typeToOllir(type.getName());
      varName = node.getChildren().get(0).get("Identifier") + "." + typeOllir;

      // Check if it is a private field
      if(classVariables.contains(node.getChildren().get(0).get("Identifier"))){
        putField = str + "putfield(this," + varName + ",aux" + ++auxiliaryVariables + "." + typeOllir + ").V;\n"; 
        varName = "aux" + auxiliaryVariables + "." + typeOllir;
      }


      assignVariableType = typeOllir;
    }
    else if(node.getChildren().get(0).getKind().equals("ARRAYACCESS")){

      // Check for recursivety in the arrayaccess index and create auxiliary variable
      String[] auxVariables = operationToOllir(node.getChildren().get(0), str).split(";\n");
      for(int i = 0; i < auxVariables.length - 1; i++){
        builder.append(auxVariables[i] + ";\n");
      }

      // Varriable name will be an auxiliary
      varName = auxVariables[auxVariables.length - 1];
      typeOllir = "i32";
      assignVariableType = "i32";
    }

    
    
    JmmNode rightAssign = node.getChildren().get(1);

    // Check if the right side is a private field
    if(rightAssign.getKind().equals("IDENTIFIER") && classVariables.contains(rightAssign.get("Identifier"))){
      String aux = str + "aux" + ++auxiliaryVariables + "." + assignVariableType + " :=." + assignVariableType + " getfield(this," + rightAssign.get("Identifier") + "." + assignVariableType + ")." + assignVariableType + ";\n";
      builder.append(str + varName + " :=." + typeOllir + " ");
      return aux + builder.toString() + "aux" + auxiliaryVariables + "." + assignVariableType + ";\n";
    }

    // Transform the right operation
    String rightSide = operationToOllir(rightAssign, "");

    // If right side has more then 1 line, then it was made recursivelly and there are auxiliary variables to account for
    if(rightSide.split("\n").length > 1){
      String[] splited = rightSide.split("\n");
      
      // Init class is a special case
      if(node.getChildren().get(1).getKind().equals("INITCLASS")){
        builder.append(str + varName + " :=." + typeOllir + " " + splited[0] + "\n");
    
        builder.append(str + splited[1] + "\n");
      }
      else{
        for(int i = 0; i < splited.length - 1; i++){
          builder.append(str + splited[i] + "\n");
        }

        builder.append(str + varName + " :=." + typeOllir + " " + splited[splited.length - 1] + "\n");
      }

      builder.append(putField);
      assignVariableType = "V";
      return builder.toString();
    }

    builder.append(str + varName + " :=." + typeOllir + " ");
    
    builder.append(rightSide);

    builder.append(putField);
    assignVariableType = "V";
    return builder.toString();
  }

  // Transformh method calls
  private String visitDotMethodDeclaration(JmmNode node, String str){
    String variableName = "", variableTypeName = "";
    StringBuilder  auxiliaryBuilder = new StringBuilder();
    StringBuilder argumentsBuilder = new StringBuilder();
    ClassMethod parentMethod = this.symbolTable.getParentMethod(node);
    
    // Transform the right side of the dot
    if(node.getChildren().get(0).getKind().equals("IDENTIFIER")){
      variableName = getVariableWithIndex(node.getChildren().get(0));

      Type variableType = symbolTable.getVariableType(parentMethod.getName(), node.getChildren().get(0).get("Identifier"));
      
      if(variableType != null){
        variableTypeName = "." + variableType.getName();
      }
      else{
        variableTypeName = "." + variableName;
      }

    }
    else if(node.getChildren().get(0).getKind().equals("THIS")){
      variableName = "this";
    }
    else if(node.getChildren().get(0).getKind().equals("INITCLASS")){
      auxiliaryBuilder.append(createAuxVariables(node.getChildren().get(0), str));

      variableName = "aux" + auxiliaryVariables;
      variableTypeName = "." + node.getChildren().get(0).getChildren().get(0).get("Identifier");
    }

    JmmNode methodCallNode = node.getChildren().get(1);
    String methodName = methodCallNode.getChildren().get(0).get("Identifier");
    String functionCallOllir = "";

    if(methodCallNode.getNumChildren() > 1){
      argumentsBuilder.append(", ");

      // Iterate over the call arguments and for each one see if there is a need to create auxiliary variables
      for(int i = 1; i < methodCallNode.getNumChildren(); i++){
        JmmNode arg = methodCallNode.getChildren().get(i);

        // If args have no children, then there is no need to create auxiliary variables
        if(arg.getChildren().isEmpty()){
          String variableType;

          if(arg.getKind().equals("IDENTIFIER")){
            variableType = typeToOllir(symbolTable.getVariableType(parentMethod.getName(), arg.get("Identifier")).getName());

            // Check if arg is a private field
            if(classVariables.contains(arg.get("Identifier"))){
              auxiliaryBuilder.append(str + "aux" + ++auxiliaryVariables + "." + variableType + " :=." + variableType + " getfield(this," + arg.get("Identifier") + "." + variableType + ")." + variableType + ";\n");
              argumentsBuilder.append("aux" + auxiliaryVariables);
            }
            else{
              argumentsBuilder.append(getValue(arg));
            }
            
          }
          else{
            variableType = kindToOllir(arg);
            argumentsBuilder.append(getValue(arg));
          }

          if(variableType != null){
            argumentsBuilder.append("." + variableType);
          }

        }
        // Create auxiliary Variables sinde it is a neste operation
        else{
          auxiliaryBuilder.append(createAuxVariables(arg, str));

          argumentsBuilder.append("aux" + auxiliaryVariables + "." + variableTypeByOperation(arg));
          
        }

        // Add a comma unless it is the very last argument
        if(i < methodCallNode.getNumChildren() - 1){
          argumentsBuilder.append(", ");
        }
      }

    }

    auxiliaryBuilder.append(str);

    // If the variable calling the method then is is invokevirtual, else is invokestatic
    if(!symbolTable.isClass(variableName)){
      // Check wheter we can get the method type from the class
      if(symbolTable.isMethod(methodName)){
        assignVariableType = typeToOllir(symbolTable.getMethodReturnType(methodName).getName());
        if(assignVariableType.equals("i32") && symbolTable.getMethodReturnType(methodName).isArray()){
          assignVariableType = "array.i32";
        }
      }

      // In case of recursiveness, we need to create an auxiliary variable to hold the value of the method
      if(recursiveDotMethod){
        recursiveDotMethod = false;
        auxiliaryVariables++;
        auxiliaryBuilder.append("aux" + auxiliaryVariables + "." + assignVariableType + " :=." + assignVariableType + " ");
      }

      functionCallOllir = auxiliaryBuilder.toString() + "invokevirtual(" + variableName + variableTypeName + ", \"" + methodName + "\"" + argumentsBuilder.toString() + ")." + assignVariableType + ";\n";
    }
    else{
      // In case of recursiveness, we need to create an auxiliary variable to hold the value of the method
      if(recursiveDotMethod){
        recursiveDotMethod = false;
        auxiliaryVariables++;
        auxiliaryBuilder.append("aux" + auxiliaryVariables + "." + assignVariableType + " :=." + assignVariableType + " ");
      }

      functionCallOllir = auxiliaryBuilder.toString() + "invokestatic(" + variableName + ", \"" + methodName + "\"" + argumentsBuilder.toString() + ")." + assignVariableType + ";\n";
    }


    return functionCallOllir;
  }

  private String visitIfElseDeclaration(JmmNode node, String str){
    int currentIf = ifNumbers++;
    JmmNode ifNode =  node.getChildren().get(0);
    StringBuilder ifElseOllirBuilder = new StringBuilder();

    // Visit the condition declaration
    String[] conditionString = visitConditionDeclaration(ifNode.getChildren().get(0).getChildren().get(0), str).split("\n");

    // Create the base ollir for the if
    if(conditionString.length == 1){
      ifElseOllirBuilder.append(str + "if (" + conditionString[0] + ") goto else"+ currentIf +";\n");
    }
    else{
      for(int i = 0; i < conditionString.length - 1; i++){
        ifElseOllirBuilder.append(conditionString[i] + "\n");
      }

      ifElseOllirBuilder.append(str + "if (" + conditionString[conditionString.length - 1] + ") goto else"+ currentIf +";\n");
    }

    // Transform all the instructions inside the if
    for(int i = 1; i < ifNode.getNumChildren(); i++){
      JmmNode child = ifNode.getChildren().get(i);
      ifElseOllirBuilder.append(visit(child, str + "  "));
    }

    ifElseOllirBuilder.append(str + "goto endif"+ currentIf +";\n" + str + "else"+ currentIf +":\n");

    // Transform else node
    JmmNode elseNode = node.getChildren().get(1);

    // Transform else node instructions
    for(int i = 0; i < elseNode.getNumChildren(); i++){
      JmmNode child = elseNode.getChildren().get(i);
      ifElseOllirBuilder.append(visit(child, str + "  "));
    }

    ifElseOllirBuilder.append(str + "endif"+ currentIf +":\n");

    return ifElseOllirBuilder.toString();
  }

  private String visitWhileDeclaration(JmmNode node, String str){
    int currentWhile = whileNumbers++;
    JmmNode conditionNode =  node.getChildren().get(0);
    StringBuilder whileOllirBuilder = new StringBuilder();

    // Transform while condition into ollir
    String[] conditionString = visitConditionDeclaration(conditionNode.getChildren().get(0), str + "  ").split("\n");
    whileOllirBuilder.append(str + "Loop" + whileNumbers + ":\n");

    // Transform the start of the while into ollir
    if(conditionString.length == 1){
      whileOllirBuilder.append(str + "  " + "if (" + conditionString[0] + ") goto Body" + whileNumbers +  ";\n");
    }
    else{
      for(int i = 0; i < conditionString.length - 1; i++){
        whileOllirBuilder.append(conditionString[i] + "\n");
      }

      whileOllirBuilder.append(str + "  " + "if (" + conditionString[conditionString.length - 1] + ") goto Body" + whileNumbers +  ";\n" + str + "  " + "goto EndLoop" + whileNumbers + ";\n");
    }

    whileOllirBuilder.append(str + "Body" + whileNumbers + ":\n");

    // Transform the instruction inside the while into ollir
    for(int i = 1; i < node.getNumChildren(); i++){
      JmmNode child = node.getChildren().get(i);
      whileOllirBuilder.append(visit(child, str + "  "));
    }

    whileOllirBuilder.append(str + "EndLoop" + whileNumbers + ":\n");
    return whileOllirBuilder.toString();
  }

  private String visitConditionDeclaration(JmmNode conditionOperation, String str){
    String operationSymbol = "";
    StringBuilder conditionOllirBuilder = new StringBuilder();
    String operationValue = "";

    // Check if the condition isn't an and or an or
    if(conditionOperation.getKind().equals("DOTMETHOD")){
      String ollirMethodCall = createAuxVariables(conditionOperation, str);

      return ollirMethodCall + "aux" + auxiliaryVariables + ".bool &&.bool true.bool";
    }
    else if(conditionOperation.getKind().equals("IDENTIFIER")){
      // Check if the identifier is a private field
      if(conditionOperation.getKind().equals("IDENTIFIER") && classVariables.contains((conditionOperation.get("Identifier")))){
        String aux = str + "aux" + ++auxiliaryVariables + operationValue + " :=" + operationValue + " getfield(this," + conditionOperation.get("Identifier") + operationValue + ")" + operationValue + ";\n";
        return aux + "aux" + auxiliaryVariables + ".bool &&.bool true.bool";
      }
      return conditionOperation.get("Identifier") + ".bool &&.bool true.bool";
    }
    else if(conditionOperation.getKind().equals("BOOL")){
      return conditionOperation.get("Bool") + ".bool &&.bool true.bool";
    }
    else if(conditionOperation.getKind().equals("NEG")){
      String aux = visitNegDeclaration(conditionOperation, str);

      if(conditionOperation.getChildren().get(0).getNumChildren() > 0){
        return aux + "aux" + auxiliaryVariables + ".bool &&.bool true.bool";
      }

      return str + "aux" + ++auxiliaryVariables + ".bool :=.bool " + aux + "aux" + auxiliaryVariables + ".bool &&.bool true.bool";
    }

    System.out.println(conditionOperation);

    // In ollir the conditions are reversed
    switch (conditionOperation.getKind()) {
      case "LESS":
        operationSymbol = " >=.bool ";
        operationValue = ".i32";
        break;
      case "AND":
        operationSymbol = " ||.bool ";
        operationValue = ".bool";
        break;
      default:
        break;
    }

    // Treat left side of the condition
    JmmNode leftVariable = conditionOperation.getChildren().get(0);
    String leftVariableOllir = "";

    // If the left variable has children, then we need to create auxiliary variables
    if(leftVariable.getNumChildren() != 0){
      conditionOllirBuilder.append(createAuxVariables(leftVariable, str));
      leftVariableOllir = "aux" + auxiliaryVariables + operationValue;
    }
    else{
      leftVariableOllir = operationToOllir(leftVariable, str).split(";")[0];

      // Check if the identifier is a private field
      if(leftVariable.getKind().equals("IDENTIFIER") && classVariables.contains(leftVariable.get("Identifier"))){
        conditionOllirBuilder.append(str + "aux" + ++auxiliaryVariables + operationValue + " :=" + operationValue + " getfield(this," + leftVariable.get("Identifier") + operationValue + ")" + operationValue + ";\n");
        leftVariableOllir = "aux" + auxiliaryVariables + operationValue;
      }
    }

    // Treat right side of the condition
    JmmNode rightVariable = conditionOperation.getChildren().get(1);
    String rightVariableOllir = "";

    // If the right variable has children, then we need to create auxiliary variables
    if(rightVariable.getNumChildren() != 0){
      conditionOllirBuilder.append(createAuxVariables(rightVariable, str));
      rightVariableOllir = "aux" + auxiliaryVariables + operationValue;
    }
    else{
      rightVariableOllir = operationToOllir(rightVariable, str).split(";")[0];

      // Check if the identifier is a private field
      if(rightVariable.getKind().equals("IDENTIFIER") && classVariables.contains(rightVariable.get("Identifier"))){
        conditionOllirBuilder.append(str + "aux" + ++auxiliaryVariables + operationValue + " :=" + operationValue + " getfield(this," + rightVariable.get("Identifier") + operationValue + ")" + operationValue + ";\n");
        rightVariableOllir = "aux" + auxiliaryVariables + operationValue;
      }
    }

    conditionOllirBuilder.append(leftVariableOllir + operationSymbol + rightVariableOllir);

    return conditionOllirBuilder.toString();
  }

  private String visitLengthDeclaration(JmmNode node, String str){
    ClassMethod parentMethod = symbolTable.getParentMethod(node);
    String parameterIndex = parentMethod.getParameterIndex(node.getChildren().get(0).get("Identifier"));

    // Check if the array is an argument of the method
    if(!parameterIndex.equals("-1")){
      return "arraylength($" + parameterIndex + "." + node.getChildren().get(0).get("Identifier") + ".array.i32).i32;\n";
    }

    // Check if the array is a private field
    if(classVariables.contains(node.getChildren().get(0).get("Identifier"))){
      String getField = str + "aux" + ++auxiliaryVariables + ".array.i32 :=.array.i32 getfield(this," + node.getChildren().get(0).get("Identifier") + ".array.i32).array.i32;\n";

      return getField + "arraylength(aux" + auxiliaryVariables + ".array.i32).i32;\n";
    }

    return "arraylength(" + node.getChildren().get(0).get("Identifier") + ".array.i32).i32;\n";
  }

  
  private String visitNegDeclaration(JmmNode node, String str){
    
    // If the Neg children has children, then we need to create aux variables
    if(node.getChildren().get(0).getNumChildren() > 0){
      String aux = createAuxVariables(node.getChildren().get(0), str);
      
      return aux + str + "aux"+ ++auxiliaryVariables + ".bool :=.bool !.bool " + (auxiliaryVariables - 1) + ".bool;\n";
    }
    
    if(node.getChildren().get(0).getKind().equals("IDENTIFIER")){
      return "!.bool " + getVariableWithIndex(node.getChildren().get(0)) + ".bool;\n";
    }
    else if (node.getChildren().get(0).getKind().equals("BOOL")){
      return "!.bool " + node.getChildren().get(0).get("Bool") + ".bool;\n";

    }

    return "";
  }

  private String initArrayWithLength(JmmNode node, String str){
    String lengthVariable = createAuxVariables(node.getChildren().get(0), str);

    return lengthVariable + "new(array, aux" + auxiliaryVariables + ".int32).array.i32;\n";
  }

  private String createAuxVariables(JmmNode node, String str){

    // NEG or DOTMETHOD can create their own auxiliary variables
    if(node.getKind().equals("DOTMETHOD")){
      recursiveDotMethod = true;
      String s = visitDotMethodDeclaration(node, str);
      return s;
    }
    else if(node.getKind().equals("NEG")){
      return visitNegDeclaration(node, str);
    }

    String rightSide = "";

    // Transform the right side into ollir
    rightSide = operationToOllir(node, str);

    auxiliaryVariables++;
    String auxVariable = str + "aux" + auxiliaryVariables + "." + variableTypeByOperation(node) + " :=." + variableTypeByOperation(node) + " ";

    // Init class is a special case
    if(node.getKind().equals("INITCLASS")){
      return auxVariable + rightSide;

    }

    // If the right side has only one element then there was no recursivety
    if(rightSide.split("\n").length == 1){
      return  auxVariable + rightSide;
    }

    String[] rightAux = rightSide.split("\n");
    StringBuilder auxBuilder = new StringBuilder();

    // Go thourgh the auxiliary variables created with recursive and add them before adding the new one
    for(int i = 0; i < rightAux.length - 1; i++){
      auxBuilder.append(rightAux[i] + "\n");
    }

    return auxBuilder.toString() + auxVariable + rightAux[rightAux.length - 1] + "\n";
  }

  // Transform an operation into ollir
  private String operationToOllir(JmmNode node, String str){
    // Get the parent method in case we need to nome the return type
    ClassMethod parentMethod = symbolTable.getParentMethod(node);

    switch (node.getKind()) {
      case "IDENTIFIER":
        String varName = getVariableWithIndex(node);

        Type type2 = symbolTable.getVariableType(parentMethod.getName(), node.get("Identifier"));
        String typeOllir2 = typeToOllir(type2.getName());
        
        return varName + "." + typeOllir2 + ";\n";

      case "NUM":
        String value = node.get("Int");
        return value + ".i32;\n";

      case "BOOL":
        String bool = node.get("Bool");
        return bool + ".bool;\n";
        
      case "INITCLASS":
        String className = node.getChildren().get(0).get("Identifier");
        String invokeString;

        if(node.getParent().getChildren().get(0).getKind().equals("IDENTIFIER")){
          invokeString = str + "invokespecial(" + node.getParent().getChildren().get(0).get("Identifier") + "." + className +",\"<init>\").V;\n";
        }
        else{
          invokeString = str + "invokespecial(aux" + (auxiliaryVariables + 1) + "." + className +",\"<init>\").V;\n";
        }

        return "new(" + className + ")." + className + ";\n" + invokeString; 
      
      case "INITARRAY":
        if(node.getChildren().get(0).getKind().equals("NUM")){
          String arraySize = node.getChildren().get(0).get("Int");
          return " new(array, " + arraySize + ".int32).array.i32;\n";
        }
        return initArrayWithLength(node, str);

      case "DOTMETHOD":
        return visitDotMethodDeclaration(node, str);

      case "LENGTH":
        return visitLengthDeclaration(node, str);

      case "ARRAYACCESS":
        String var = getVariableWithIndex(node.getChildren().get(0));
        String getField = "";

        if(classVariables.contains(node.getChildren().get(0).get("Identifier"))){
          getField = str + "aux" + ++auxiliaryVariables + ".array.i32 :=.array.i32 getfield(this," + node.getChildren().get(0).get("Identifier") + ".array.i32).array.i32;\n";
          var = "aux" + auxiliaryVariables;
        }
      
        
        if(node.getChildren().get(1).getKind().equals("IDENTIFIER")){
          
          return getField + var + "[" + getVariableWithIndex(node.getChildren().get(1)) + ".i32].i32;\n";
        }
        else if(node.getChildren().get(1).getKind().equals("NUM")){
          return getField + createAuxVariables(node.getChildren().get(1), str) + var + "[aux" + auxiliaryVariables + ".i32].i32;\n";
        }
        else{
          String auxVariables = createAuxVariables(node.getChildren().get(1), str);
          return auxVariables + getField + var + "[" + "aux" + auxiliaryVariables + ".i32].i32;\n";
        }

      case "NEG":
        return visitNegDeclaration(node, str);
      default:
        if(node.getKind().equals("SUM") || node.getKind().equals("SUB") || node.getKind().equals("MUL") || node.getKind().equals("DIV"))
          return dealIntOperations(node, str) + ";\n";
        else if(node.getKind().equals("AND") || node.getKind().equals("LESS") )
          return dealBoolOperations(node, str) + ";\n";
        return "";

    }
  }

  public String visitChildren(JmmNode node, String str){

    StringBuilder childrenOllir = new StringBuilder();

    for(JmmNode children : node.getChildren()){
  
      String res = visit(children, str + "  ");
      if(res != null){
        childrenOllir.append(res);
      }
    }

    return childrenOllir.toString();
  }

  // Transform variable type into ollir
  private String typeToOllir(String type){
    switch (type) {
      case "int":
        return "i32";
      case "intArray":
        return "array.i32";
      case "bool":
        return "bool";
      case "String":
        return "String";
      default:
        return type;
    }
  }

  // Transform node kind into ollir
  private String kindToOllir(JmmNode node){
    String kind = node.getKind();
    switch (kind) {
      case "NUM":
        return "i32";
      case "BOOL":
        return "bool";
      case "IDENTIFIER":
        return node.get("Identifier");
      default:
        return kind;
    }
  }

  // Returns node the variable type of each operation
  private String variableTypeByOperation(JmmNode node){
    String kind = node.getKind();
    switch (kind) {
      case "AND":
        return "bool";
      case "LESS":
        return "bool";
      case "INITARRAY":
        return "array.i32";
      case "INITCLASS":
        return node.getChildren().get(0).get("Identifier");
      case "DOTMETHOD":
        if(symbolTable.isMethod(node.getChildren().get(1).getChildren().get(0).get("Identifier"))){
          return typeToOllir(symbolTable.getMethodReturnType(node.getChildren().get(1).getChildren().get(0).get("Identifier")).getName());
        }
        else{
          return "V";
        }
      default:
        return "i32";
    }
  }
  
  // Returns the value of a node
  public String getValue(JmmNode node){
    String kind = node.getKind();

    switch (kind) {
      case "NUM":
        return node.get("Int");
      case "BOOL":
        return node.get("Bool");
      case "IDENTIFIER":
        return node.get("Identifier");
      default:
        return "";
    }
  }

  // Addes argument index to a variable (if the variable is an argument)
  public String getVariableWithIndex(JmmNode node){
    String variableName = node.get("Identifier");

    ClassMethod parentMethod = symbolTable.getParentMethod(node);
    String parameterIndex = parentMethod.getParameterIndex(node.get("Identifier"));

    if(!parameterIndex.equals("-1")){
      return "$" + parameterIndex + "." + variableName;
    }

    return variableName;
  }

  // Transforms sums, multiplications, subtractions and divisions into Ollir
  private String dealIntOperations(JmmNode node, String str){
    JmmNode left = node.getChildren().get(0);
    String leftStr = "";
    String auxiliaryLeft = "";
    String auxiliaryRight = "";
    StringBuilder builder = new StringBuilder();

    // Transform the left side into Ollir
    if(left.getKind().equals("IDENTIFIER")){
      leftStr = getVariableWithIndex(left) + ".i32";

      if(classVariables.contains(left.get("Identifier"))){
        auxiliaryLeft = str + "aux" + ++auxiliaryVariables + ".i32 :=.i32 getfield(this," + left.get("Identifier") + ".i32).i32;\n";
        leftStr = "aux" + auxiliaryVariables + ".i32";
      }

    }
    else if (left.getKind().equals("NUM")){
      leftStr = left.get("Int") +  ".i32";
    }
    else{
      // This means it is a nested operation and auxiliary variables must be created
      auxiliaryLeft = createAuxVariables(left, str);
      leftStr = "aux" + auxiliaryVariables + ".i32";
    }
    
    // Transform operator into Ollir
    String operator = "";
    switch (node.getKind()) {
      case "SUM":
        operator =  " +.i32 ";
        break;
      case "SUB":
        operator = " -.i32 ";
        break;
      case "MUL":
        operator = " *.i32 ";
        break;
      case "DIV":
        operator = " /.i32 ";
        break;
    }

    JmmNode right = node.getChildren().get(1);
    String rightStr = "";

    // Transform the right side into Ollir
    if (right.getKind().equals("IDENTIFIER")){
     rightStr = getVariableWithIndex(right) + ".i32";

     if(classVariables.contains(right.get("Identifier"))){
      auxiliaryRight = str + "aux" + ++auxiliaryVariables + ".i32 :=.i32 getfield(this," + right.get("Identifier") + ".i32).i32;\n";
      rightStr = "aux" + auxiliaryVariables + ".i32";
    }
    }
    else if  (right.getKind().equals("NUM")){
     rightStr = right.get("Int") +  ".i32";
    }
    else{
      // This means it is a nested operation and auxiliary variables must be created
      auxiliaryRight = createAuxVariables(right, str);
      rightStr = "aux" + auxiliaryVariables + ".i32";
    }

    builder.append(auxiliaryLeft + auxiliaryRight + leftStr + operator + rightStr);

    return builder.toString();
  }

  // Transforms boolean operations (AND and LESS) into Ollir
  private String dealBoolOperations(JmmNode node, String str){
    ClassMethod parentMethod = symbolTable.getParentMethod(node);
    JmmNode left = node.getChildren().get(0);
    String leftStr = "";
    String auxiliaryLeft = "";
    String auxiliaryRight = "";
    StringBuilder builder = new StringBuilder();
    String type = "";

    // Transform the left side into Ollir
    if(left.getKind().equals("IDENTIFIER")){
      type = typeToOllir(symbolTable.getVariableType(parentMethod.getName(), left.get("Identifier")).getName());
      leftStr = getVariableWithIndex(left) + "." + type;

      if(classVariables.contains(left.get("Identifier"))){
        auxiliaryLeft = str + "aux" + ++auxiliaryVariables + "." + type + " :=." + type + " getfield(this," + left.get("Identifier") + "." + type + ")." + type + ";\n";
        leftStr = "aux" + auxiliaryVariables + ".i32";
      }
    }
    else if (left.getKind().equals("BOOL")){
      type = "bool";
      leftStr = left.get("Bool") +  ".bool";
    }
    else if(left.getKind().equals("NUM")){
      type = "i32";
      leftStr = left.get("Int") + ".i32";
    }
    else if(left.getKind().equals("ARRAYACCESS")){
      type = "i32";
      auxiliaryLeft = createAuxVariables(left, str);
      leftStr =  "aux" + auxiliaryVariables + ".i32";
    }
    else if(left.getKind().equals("NEG")){
      //
      auxiliaryLeft = visitNegDeclaration(left, str);
      
      if(left.getChildren().get(0).getNumChildren() == 0){
        auxiliaryLeft = "aux" + ++auxiliaryVariables + ".bool :=.bool !.bool " + auxiliaryVariables;
      }

      leftStr =  "aux" + auxiliaryVariables + ".bool";

    }
    else{
      // This means it is a nested operation and auxiliary variables must be created
      auxiliaryLeft = createAuxVariables(left, str);

      leftStr =  "aux" + auxiliaryVariables + ".bool";
    }

    // Transform operator into Ollir
    String operator = "";
    switch (node.getKind()) {
      case "AND":
        operator = " &&.bool" + " ";
        break;
      case "LESS":
        operator = " <.bool" + " ";
        break;
      case "NEG":
        operator = "";
        break;
    }

    JmmNode right = node.getChildren().get(1);
    String rightStr = "";

    // Transform the right side into Ollir
    if (right.getKind().equals("IDENTIFIER")){
      type = typeToOllir(symbolTable.getVariableType(parentMethod.getName(), right.get("Identifier")).getName());
      rightStr = getVariableWithIndex(right) + "." + type;

      if(classVariables.contains(right.get("Identifier"))){
        auxiliaryRight = str + "aux" + ++auxiliaryVariables + "." + type + " :=." + type + " getfield(this," + right.get("Identifier") + "." + type + ")." + type + ";\n";
        rightStr = "aux" + auxiliaryVariables + ".i32";
      }
    }
    else if  (right.getKind().equals("BOOL")){
     rightStr = right.get("Bool") +  ".bool ";
    }
    else if(right.getKind().equals("NUM")){
      rightStr = right.get("Int") + ".i32";
    }
    else if(right.getKind().equals("ARRAYACCESS")){
      auxiliaryRight = createAuxVariables(right, str);
      rightStr =  "aux" + auxiliaryVariables + ".i32";
    }
    else if(right.getKind().equals("NEG")){
      auxiliaryRight = visitNegDeclaration(right, str);

      if(right.getChildren().get(0).getNumChildren() == 0){
        auxiliaryRight = "aux" + ++auxiliaryVariables + ".bool :=.bool !.bool " + auxiliaryVariables;
      }

      rightStr =  "aux" + auxiliaryVariables + ".bool";
    }
    else{
      // This means it is a nested operation and auxiliary variables must be created
      auxiliaryRight = createAuxVariables(right, str);

      rightStr =  "aux" + auxiliaryVariables + ".bool";
    }

    builder.append(auxiliaryLeft + auxiliaryRight + leftStr + operator + rightStr);

    return builder.toString();
  }
}
