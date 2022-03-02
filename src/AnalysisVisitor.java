import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;

import pt.up.fe.comp.jmm.analysis.table.Type;

import java.util.List;

public class AnalysisVisitor extends PreorderJmmVisitor<Boolean, Boolean>{
  
  private CustomSymbolTable symbolTable;
  private List<Report> reports;

  public AnalysisVisitor(CustomSymbolTable symbolTable, List<Report> reports){
    this.symbolTable = symbolTable;
    this.reports = reports;

    addVisit("ASSIGN", this::visitAssignDeclaration);
    addVisit("ARRAYACCESS", this::visitArrayAccessDeclaration);
    addVisit("AND", this::visitCondDeclaration);
    addVisit("LESS", this::visitCondDeclaration);
    addVisit("SUM", this::visitCondDeclaration);
    addVisit("SUB", this::visitCondDeclaration);
    addVisit("MUL", this::visitCondDeclaration);
    addVisit("DIV", this::visitCondDeclaration);
    addVisit("LENGTH", this::visitLengthDeclaration);
  }

  private Boolean visitAssignDeclaration(JmmNode node, Boolean valid){

    List<JmmNode> children = node.getChildren();

    JmmNode parent = node.getParent();

    // get the node parent function so that we know if it was declared locally or globally (ended up not being used)
    while(!parent.getKind().equals("OTHERMETHOD") && !parent.getKind().equals("MAINMETHOD")){
      parent = parent.getParent();
    }

    //Get the left type of the assign
    ClassMethod localMethod = this.symbolTable.getPureMethods().get(parent.get("MethodName"));
    Type leftType;


    if(children.get(0).getKind().equals("ARRAYACCESS")){
      leftType = this.symbolTable.getVariableType(localMethod.getName(), children.get(0).getChildren().get(0).get("Identifier"));
    }
    else{
      leftType = this.symbolTable.getVariableType(localMethod.getName(), children.get(0).get("Identifier"));
    }

    Report report;

    //Using the left type check if the right is of the same type
    switch(children.get(1).getKind()){
        
      case "NUM":
        if(leftType.getName().equals("int") || leftType.getName().equals("intArray")){
          return true;
        }
        
        report = new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(node.get("line")), Integer.parseInt(node.get("column")), "Cannot assign int to non int variable..."); //ReportType type, Stage stage, int line, int column, String message
        this.reports.add(report);
        return false;

      case "BOOL":
      
        if(leftType.getName().equals("bool")){
          return true;
        }

        report = new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(node.get("line")), Integer.parseInt(node.get("column")), "Cannot assign int to non int variable..."); //ReportType type, Stage stage, int line, int column, String message
        this.reports.add(report);
        return false;
    }



    return valid;
  }

  private Boolean visitArrayAccessDeclaration(JmmNode node, Boolean valid){
    
    List<JmmNode> children = node.getChildren();

    // If an arrayaccess is not of type int then it is invalid semantics
    if(!node.getChildren().get(1).getKind().equals("NUM") && !getOperationType(node.getChildren().get(1)).equals("int")){
        Report report = new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(node.get("line")), Integer.parseInt(node.get("column")), "Invalid Access index..."); //ReportType type, Stage stage, int line, int column, String message
        this.reports.add(report);
        return false;
    }
    return valid;
  }


  // Function to get the operation type of the node. For example if a node is of type "LESS" then it has a bool value
  private String getOperationType(JmmNode node){
    Type value;

    switch(node.getKind()){
        case "IDENTIFIER" :
            value = symbolTable.getVariableType(symbolTable.getParentMethod(node).getName(), node.get("Identifier"));
            return value.getName();
        case "NUM":
            return "int";
        case "LENGTH" :
            return "int";
        case "METHODCALL" :
            if(node.getChildren().get(0).getKind().equals("LESS")){
                return "bool";
            }
            return symbolTable.getMethodReturnType(node.getChildren().get(0).get("Identifier")).getName();
        case "DOTMETHOD":
            return getOperationType(node.getChildren().get(1));
        case "NEG" :
            return "bool";
        case "LESS" :
            return "bool";
        case "AND" :
            return "bool";
        case "ARRAYACCESS":
            return "int";
        default :
            String str = getOperationType(node.getChildren().get(0));

            if(str.equals(getOperationType(node.getChildren().get(1)))){
                return str;
            }

            System.out.println(node);
            return "False";
    }
  }

  // Checking if the condition is of the same type. We can't have comparison between int and bool
  private Boolean visitCondDeclaration(JmmNode node, Boolean valid){
    
    List<JmmNode> children = node.getChildren();
    
    // System.out.println(node.getChildren().get(0) + "=" + node.getChildren().get(1));

    System.out.println(getOperationType(node.getChildren().get(0)) + "=" + getOperationType(node.getChildren().get(1)));
    if(getOperationType(children.get(0)).equals(getOperationType(children.get(1)))){
        return true;
    }
    
    Report report = new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(node.get("line")), Integer.parseInt(node.get("column")), "Invalid Condition..."); //ReportType type, Stage stage, int line, int column, String message
    this.reports.add(report);

    return valid;
  }


  //Check if we are trying to get the length of an object that is not an array
  private Boolean visitLengthDeclaration(JmmNode node, Boolean valid){


    if(node.getChildren().get(0).getKind().equals("IDENTIFIER")){
          
      Type value = symbolTable.getVariableType(symbolTable.getParentMethod(node).getName(), node.getChildren().get(0).get("Identifier"));
      if(value.isArray()){
          return true;
      }

      Report report = new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(node.get("line")), Integer.parseInt(node.get("column")), "Length of a non array variable..."); //ReportType type, Stage stage, int line, int column, String message
      this.reports.add(report);
  }
  else{
    Report report = new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(node.get("line")), Integer.parseInt(node.get("column")), "Length of a non identifier variable..."); //ReportType type, Stage stage, int line, int column, String message
    this.reports.add(report);
  }


  return valid;

  }

}