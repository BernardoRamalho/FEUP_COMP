import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.ElementType;
import org.specs.comp.ollir.Method;
import org.specs.comp.ollir.Operand;

public class Store {

    private int virtualReg;
    private Operand operand;
    private Method method;

    // class that stores variables
    public Store(int virtualReg, Operand operand, Method method) {
        this.virtualReg = virtualReg;
        this.operand = operand;
        this.method = method;
    }

    // translates to jasmin
    public String toJasmin(){
        // Ver types -> ElementType
        if(virtualReg >= 0 && virtualReg <= 3){
            if(method.getVarTable().get(operand.getName()).getVarType().getTypeOfElement() == ElementType.INT32 || method.getVarTable().get(operand.getName()).getVarType().getTypeOfElement() == ElementType.BOOLEAN){
                return "istore_" + virtualReg + "\n";
            }
            else{
                return "astore_" + virtualReg + "\n";
            }
        }
        else{
            if(method.getVarTable().get(operand.getName()).getVarType().getTypeOfElement() == ElementType.INT32 || method.getVarTable().get(operand.getName()).getVarType().getTypeOfElement() == ElementType.BOOLEAN){
                return "istore " + virtualReg + "\n";
            }
            else{
                return "astore " + virtualReg + "\n";
            }
        }
    }
}
