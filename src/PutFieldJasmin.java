import org.specs.comp.ollir.*;

public class PutFieldJasmin {

    private PutFieldInstruction instruction;
    private Method method;
    private int stack;
    private int maxStack;
    private int maxReg;

    // Class that translates putfield instructions
    public PutFieldJasmin(PutFieldInstruction instruction, Method method) {
        this.instruction = instruction;
        this.method = method;
        stack = 0;
        maxStack = 0;
        maxReg = 0;
    }

    // translates to jasmin
    public String toJasmin(){
        StringBuilder builder = new StringBuilder();

        builder.append(UtilsJasmin.loadArgument(instruction.getFirstOperand(), method));
        maxReg = UtilsJasmin.changeMaxReg(maxReg, method, instruction.getFirstOperand());


        builder.append(UtilsJasmin.loadArgument(instruction.getThirdOperand(), method));
        maxReg = UtilsJasmin.changeMaxReg(maxReg, method, instruction.getThirdOperand());

        stack+=2;
        if(stack > maxStack){
            maxStack = stack;
        }

        builder.append("putfield ");

        if(instruction.getFirstOperand().isLiteral()){
            LiteralElement elem = (LiteralElement) instruction.getFirstOperand();

            builder.append(elem.getType() + "/");
        }
        else{
            Operand elem = (Operand) instruction.getFirstOperand();

            if(elem.getType().getTypeOfElement() == ElementType.THIS){
                builder.append(method.getOllirClass().getClassName() + "/");
            }
            else
                builder.append(elem.getType() + "/");
        }

        if(instruction.getSecondOperand().isLiteral()){
            LiteralElement elem = (LiteralElement) instruction.getSecondOperand();

            builder.append(elem.getLiteral() + " " + UtilsJasmin.getElemType(elem.getType())   + "\n");
        }
        else{
            Operand elem = (Operand) instruction.getSecondOperand();

            builder.append(elem.getName() + " " + UtilsJasmin.getElemType(elem.getType())   + "\n");
        }

        stack-=2;

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
