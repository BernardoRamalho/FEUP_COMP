import org.specs.comp.ollir.*;

public class BinaryOperJasmin {

    private BinaryOpInstruction instruction;
    private Method method;
    private int stack;
    private int maxStack;
    private int maxReg;

    // Class that translates the binary oper instruction to jasmin
    public BinaryOperJasmin(BinaryOpInstruction instruction, Method method) { 

        this.instruction = instruction;
        this.method = method;
        stack = 0;
        maxStack = 0;
        maxReg = 0;
    }

    // loads the arguments
    public String dealWithOperand(Element operand){
        if(operand.isLiteral()){
            String literal = ((LiteralElement) operand).getLiteral();
            Const constantJasmin = new Const(literal);
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }
            return constantJasmin.toJasmin();
        }
        else{
            String varName = ((Operand) operand).getName();
            String result = UtilsJasmin.loadArgument(operand, method);
            maxReg = UtilsJasmin.changeMaxReg(maxReg, method, operand);
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }
            return result;
        }

    }

    // translates it 
    public String toJasmin(){ 
        StringBuilder builder = new StringBuilder();
        String left = dealWithOperand(instruction.getLeftOperand());
        String right = dealWithOperand(instruction.getRightOperand());

        builder.append(left);
        builder.append(right);

        builder.append(UtilsJasmin.getOperation(instruction.getUnaryOperation()));
        stack--;
        if(stack > maxStack){
            maxStack = stack;
        }

        return builder.toString();
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getStack() {
        return stack;
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
