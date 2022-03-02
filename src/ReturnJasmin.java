import org.specs.comp.ollir.Method;
import org.specs.comp.ollir.Operand;
import org.specs.comp.ollir.ReturnInstruction;

public class ReturnJasmin {

    private ReturnInstruction instruction;
    private Method method;
    private int stack;
    private int maxStack;
    private int maxReg;

    // class that translates return instructions
    public ReturnJasmin(ReturnInstruction instruction, Method method) {

        this.instruction = instruction;
        this.method = method;
        stack = 0;
        maxStack = 0;
        maxReg = 0;
    }

    // translates to jasmin
    public String toJasmin(){
        if(!instruction.hasReturnValue()){
            return "return\n";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(UtilsJasmin.loadArgument(instruction.getOperand(), method));
        stack++;
        if(maxStack < stack){
            maxStack = stack;
        }
        maxReg = UtilsJasmin.changeMaxReg(maxReg, method, instruction.getOperand());
        if(!instruction.getOperand().isLiteral()){
            Operand operand = (Operand) instruction.getOperand();
            if(operand.getType().toString().equals("INT32") || operand.getType().toString().equals("BOOLEAN")){
                builder.append("ireturn\n");
            }
            else builder.append("areturn\n");
        }
        else{
            builder.append("ireturn\n");
        }

        return builder.toString();

    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getStack() {
        return stack;
    }

    public void setStack(int stack) {
        this.stack = stack;
        if(maxStack < stack)
            maxStack = stack;
    }

    public int getMaxReg() {
        return maxReg;
    }
}
