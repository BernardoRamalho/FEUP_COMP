import org.specs.comp.ollir.Method;
import org.specs.comp.ollir.UnaryOpInstruction;

public class UnaryOperJasmin {

    private UnaryOpInstruction instruction;
    private Method method;
    private int stack;
    private int maxStack;
    private int maxReg;

    // Class that translates unary oper instructions
    public UnaryOperJasmin(UnaryOpInstruction instruction, Method method) {
        this.instruction = instruction;
        this.method = method;
        stack = 0;
        maxStack = 0;
        maxReg = 0;
    }

    // translates to jasmin
    public String toJasmin(){ 
        StringBuilder builder = new StringBuilder();

        builder.append(UtilsJasmin.loadArgument(instruction.getRightOperand(), method));
        maxReg = UtilsJasmin.changeMaxReg(maxReg, method, instruction.getRightOperand());
        stack++;
        if(stack > maxStack){
            maxStack = stack;
        }
        builder.append("ifne UnaryOperL1\n");
        stack--;
        builder.append("iconst_1\n");
        stack++;
        if(stack > maxStack){
            maxStack = stack;
        }
        builder.append("goto UnaryOperL2\n");
        builder.append("UnaryOperL1:\n");
        builder.append("    iconst_0\n");
        stack++;
        if(stack > maxStack){
            maxStack = stack;
        }
        builder.append("UnaryOperL2:\n");

        return builder.toString();
    }

    public int getStack() {
        return stack;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getMaxReg() {
        return maxReg;
    }

    public void setStack(int stack){
        this.stack = stack;
        if(maxStack < stack)
            maxStack = stack;
    }
}
