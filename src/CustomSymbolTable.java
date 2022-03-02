import pt.up.fe.comp.jmm.analysis.table.*;
import pt.up.fe.comp.jmm.JmmNode;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CustomSymbolTable implements SymbolTable{
    
    private String className;
    private List<String> imports;
    private String superClass;
    private ArrayList<Symbol> fields;
    private Map<String, ClassMethod> methods;
    

    public CustomSymbolTable(){
        imports = new ArrayList<>();
        fields = new ArrayList<>();
        methods = new HashMap<String, ClassMethod>();
        return;
    }
    

    //Increments / adds

    public void addImport(String imp){
        this.imports.add(imp);
    }

    public void addMethod(ClassMethod method){
        this.methods.put(method.getName(), method);
    }

    public void addSymbol(CustomSymbol symbol){
        this.fields.add(symbol);
    }

    //Sets

    public void setClassName(String className) {
        this.className = className;
    }

    public void setSuper(String superClass) {
        this.superClass = superClass;
    }


    // Gets
    public List<String> getImports(){
        return this.imports;
    }

    
    public String getClassName(){
        return this.className;
    }

    
    public String getSuper(){
        return this.superClass;
    }

    
    public ArrayList<Symbol> getFields(){
        return this.fields;
    }

    
    public List<String> getMethods(){
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, ClassMethod> entry : methods.entrySet())
            list.add(entry.getKey());

        return list;
    }

    public Map<String, ClassMethod> getPureMethods(){
        return this.methods;
    }

    
    public Type getReturnType(String methodName){
        return methods.get(methodName).getReturnType();
    }

    
    public List<Symbol> getParameters(String methodName){
        return null ; //methods.get(methodName).getParameters();
    }

    
    public List<Symbol> getLocalVariables(String methodName){
        return null ;//methods.get(methodName).getLocalVariables();
    }

    public Type getVariableType(String methodName, String variableName){
        if(this.imports.contains(variableName)){
            return null;
        }
        
        for(Symbol s : this.fields){
            if(variableName.equals(s.getName())){
                return s.getType();
            }
        }

        return methods.get(methodName).getVariableType(variableName);
    }
    
    public ClassMethod getParentMethod(JmmNode node){
        JmmNode parent = node.getParent();

        while(!parent.getKind().equals("OTHERMETHOD") && !parent.getKind().equals("MAINMETHOD")){
            parent = parent.getParent();
        }

        return methods.get(parent.get("MethodName"));
    }

    public boolean isMethod(String function){
        return methods.containsKey(function);
    }

    public boolean isField(String varName){ 
        if(fields.isEmpty()){
            return false;
        }
            
        for(Symbol symbol: fields){
            if(symbol.getName().equals(varName))
                return true;
        }
        return false;
    }

        public boolean isClass(String name){
        if(className.equals(name)){
            return true;
        }

        for(String s : imports){
            if(s.contains(name)){
                return true;
            }
        }
        return false;
    }

    public Type getMethodReturnType(String methodName){
        ClassMethod method = methods.get(methodName);
        if(method == null){
            return new Type("V", false);
        }
        return method.getReturnType();

    }
}