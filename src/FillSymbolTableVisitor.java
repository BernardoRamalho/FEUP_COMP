import pt.up.fe.comp.jmm.analysis.table.*;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

public class FillSymbolTableVisitor extends PreorderJmmVisitor<CustomSymbolTable, CustomSymbolTable> {

    public FillSymbolTableVisitor(){
        addVisit("CLASS", this::visitClassDeclaration);
        addVisit("IMPORT", this::visitImportDeclaration);
        addVisit("OTHERMETHOD", this::visitMethodDeclaration);
        addVisit("MAINMETHOD", this::visitMethodDeclaration);
        addVisit("VARIABLE", this::visitVariableDeclaration);
         
    }

    private CustomSymbolTable visitClassDeclaration (JmmNode node, CustomSymbolTable symbolTable){


        // get the class name
        String name = node.getChildren().get(0).get("Identifier");
        symbolTable.setClassName(name);

        // verify if the class extends any other
        if(node.getChildren().size() > 1 && node.getChildren().get(1).getKind().equals("EXTENDS")){
            symbolTable.setSuper(node.getChildren().get(1).getChildren().get(0).get("Identifier"));
        }

        return symbolTable;
    }

    private CustomSymbolTable visitImportDeclaration (JmmNode node, CustomSymbolTable symbolTable){
        String importString;
        importString = "";

        // iterate over the imports one by one 
        for (var imp :  node.getChildren()){
            importString += imp.get("Identifier") + ".";
        }

        //remove the last dot from the import
        symbolTable.addImport(importString.substring(0, importString.length() - 1));

        return symbolTable;
    }

    private CustomSymbolTable visitMethodDeclaration (JmmNode node, CustomSymbolTable symbolTable){

        ClassMethod method = new ClassMethod();

        //System.out.println(node);

        method.setName(node.get("MethodName"));

        if(node.getAttributes().contains("args")){
            String[] args = node.get("args").split(",");
            String type, name;

            if(node.getKind().equals("MAINMETHOD")){
                method.addParameter(new CustomSymbol(new Type("String", true), args[0]));
            }
            else{
                for(var i = 0; i < args.length; i++){
                    name = args[i];
                    type = node.getChildren().get(i).get("type");
                    Type argType;
                    if(type.equals("intArray")){
                        argType = new Type("intArray", true);
                    }else{
                        argType = new Type(type, false);
                    }
    
                    method.addParameter(new CustomSymbol(argType, name));
                }
            }
            
        }
  

        if(node.getAttributes().contains("ReturnType")){
            var returnType = node.get("ReturnType");

            if(returnType.equals("intArray")){
                method.setReturnType(new Type("intArray", true));
            }else{
                method.setReturnType(new Type(returnType, false));
            }
        
        }

        symbolTable.addMethod(method);

        return symbolTable;
    }


    private CustomSymbolTable visitVariableDeclaration (JmmNode node, CustomSymbolTable symbolTable){

        var children = node.getChildren();

        var name = children.get(1).get("Identifier");

        var kind = children.get(0).getKind();

        JmmNode parent = node.getParent();

        var classVariable = parent.getKind().equals("CLASS");

        if(!classVariable){
            while(!parent.getKind().equals("OTHERMETHOD") && !parent.getKind().equals("MAINMETHOD")){
                parent = parent.getParent();
            }
        }
    

        if(kind.equals("IDENTIFIER")){
            var type = children.get(0).get("Identifier");

            if(classVariable){
                symbolTable.addSymbol(new CustomSymbol(new Type(type, false), name));
            }
            else{
                symbolTable.getPureMethods().get(parent.get("MethodName")).addVariable(new CustomSymbol(new Type(type, false), name));
            }

            return symbolTable;
        }


        var type = children.get(0).get("type");


        if(type.equals("intArray")){
            if(classVariable){
                symbolTable.addSymbol(new CustomSymbol(new Type("intArray", true), name));
            }
            else{
                symbolTable.getPureMethods().get(parent.get("MethodName")).addVariable(new CustomSymbol(new Type("intArray", true), name));
            }
            return symbolTable;
        }

        if(classVariable){
            symbolTable.addSymbol(new CustomSymbol(new Type(type, false), name));
        }
        else{
            symbolTable.getPureMethods().get(parent.get("MethodName")).addVariable(new CustomSymbol(new Type(type, false), name));

        }
        return symbolTable;
    }

}