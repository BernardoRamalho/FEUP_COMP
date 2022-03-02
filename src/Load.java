import org.specs.comp.ollir.ElementType;
import org.specs.comp.ollir.Method;
import org.specs.comp.ollir.Operand;

public class Load {

    private int virtualReg;
    private Operand operand;
    private Method method;

    // Class that loads variables
    public Load(int virtualReg, Operand operand, Method method) {
        this.virtualReg = virtualReg;
        this.operand = operand;
        this.method = method;
    }

    // translates do jasmin
    public String toJasmin(){
        // Ver types -> ElementType
        if(virtualReg >= 0 && virtualReg <= 3){
            if(method.getVarTable().get(operand.getName()).getVarType().getTypeOfElement() == ElementType.INT32 || method.getVarTable().get(operand.getName()).getVarType().getTypeOfElement() == ElementType.BOOLEAN ){
                return "iload_" + virtualReg + "\n";
            }
            else{
                return "aload_" + virtualReg + "\n";
            }
        }
        else{
            if(method.getVarTable().get(operand.getName()).getVarType().getTypeOfElement() == ElementType.INT32 || method.getVarTable().get(operand.getName()).getVarType().getTypeOfElement() == ElementType.BOOLEAN){
                return "iload " + virtualReg + "\n";
            }
            else{
                return "aload " + virtualReg + "\n";
            }
        }
    }
}
