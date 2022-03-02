import org.specs.comp.ollir.*;

import java.util.HashMap;

public final class UtilsJasmin {

    // gets virtual reg from var name
    public static int getVirtualReg(Method method, String varName){

        HashMap<String, Descriptor> hashMap = method.getVarTable();
        return hashMap.get(varName).getVirtualReg();
    }

    // gets virtual reg from elem
    public static int getVirtualReg(Method method, Element element){
        HashMap<String, Descriptor> hashMap = method.getVarTable();
        if(element.isLiteral())
            return -1;
        String varName = ((Operand) element).getName();
        if(!hashMap.containsKey(varName))
            return -1;
        return getVirtualReg(method, ((Operand) element).getName());
    }

    // changes max reg number
    public static int changeMaxReg(int current, int possible ){
        if(current > possible)
            return current;
        else return possible;
    }

    // changes max reg number
    public static int changeMaxReg(int current, Method method, Element element){
        int possible = getVirtualReg(method, element);
        return changeMaxReg(current, possible);
    }

    // translates operations
    public static String getOperation(Operation operation){ 

        switch(operation.getOpType()){
            case ADD:
                return "iadd\n";
            case SUB:
                return "isub\n";
            case MUL:
                return "imul\n";
            case DIV:
                return "idiv\n";
            case NOTB:
                return "\n";
            case AND:
                return "iand\n";
            case ANDB:
                return "iand\n";
            case ANDI32:
                return "iand\n";
            case OR:
                return "ior\n";
            case ORB:
                return "ior\n";
            case ORI32:
                return "ior\n";
        }

        return "";
    }

    // translates elem types by elements
    public static String getElemType(Element element){ 
        switch(element.getType().getTypeOfElement()){
            case INT32:
                return "I";
            case BOOLEAN:
                return "Z";
            case ARRAYREF: 
                return "[I";
            case THIS:
                break;
            case STRING:
                return "Ljava/lang/String;";
            case VOID:
                return "V";
            default:
                return element.getType().getTypeOfElement().toString();
        }
        return "";
    }

    // translates elem types by types
    public static String getElemType(Type type){
        switch(type.getTypeOfElement()){
            case INT32:
                return "I";
            case BOOLEAN:
                return "Z";
            case ARRAYREF: 
                return "[I";
            case THIS:
                break;
            case STRING:
                return "Ljava/lang/String;";
            case VOID:
                return "V";
        }
        return "";
    }

    // loads variables
    public static String loadArgument(Element argument, Method method){
        if(argument.isLiteral()){
            String literal = ((LiteralElement) argument).getLiteral();
            Const constantJasmin = new Const(literal);
            return constantJasmin.toJasmin();
        }
        else{
            String varName = ((Operand) argument).getName();
            if(varName.equals("true")){
                return "iconst_1\n";
            }
            else if(varName.equals("false"))
                return "iconst_0\n";
            int virtualReg = UtilsJasmin.getVirtualReg(method, varName);
            Load load = new Load(virtualReg, (Operand) argument, method);
            return load.toJasmin();
        }
    }
}
