import pt.up.fe.comp.jmm.analysis.table.*;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ClassMethod {

    private String name;
    private Type returnType;
    private List<CustomSymbol> parameters;
    private List<CustomSymbol> localVariables;

    public ClassMethod(){
        localVariables = new ArrayList<>();
        parameters = new ArrayList<>();
    }
    
    public String getName(){
        return name;
    }
    
    public Type getReturnType(){
        return returnType;
    }
    
    
    public List<CustomSymbol> getLocalVariables() {
        return this.localVariables;
    }
    
    public List<CustomSymbol> getParameters(){
        return this.parameters;
    }

    public void setLocalVariables(List<CustomSymbol> localVariables) {
        this.localVariables = localVariables;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParameters(List<CustomSymbol> parameters) {
        this.parameters = parameters;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public void addVariable(CustomSymbol var){
        localVariables.add(var);
    }

    public void addParameter(CustomSymbol var){
        parameters.add(var);
    }

    public Type getVariableType(String identifier){
        for(CustomSymbol symbol: parameters){
            if(symbol.getName().equals(identifier)){
                return symbol.getType();
            }
        }

        for(CustomSymbol symbol: localVariables){
            if(symbol.getName().equals(identifier)){
                return symbol.getType();
            }
        }

        return null;
    }

    public String getParameterIndex(String identifier){
        for(int i = 0; i < parameters.size(); i++){
            if(identifier.equals(parameters.get(i).getName())){
                return "" + ++i;
            }
        }

        return "-1";
    }

}