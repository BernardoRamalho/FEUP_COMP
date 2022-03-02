import org.specs.comp.ollir.CondBranchInstruction;
import org.specs.comp.ollir.ElementType;
import org.specs.comp.ollir.Method;
import org.specs.comp.ollir.Operation;

public class BranchJasmin {

    private CondBranchInstruction instruction;
    private Method method;
    private int stack;
    private int maxStack;
    private int maxReg;

    // class that translates the branch instruction
    public BranchJasmin(CondBranchInstruction instruction, Method method) { 
        this.instruction = instruction;
        this.method = method;
        stack = 0;
        maxStack = 0;
        maxReg = 0;
    }

    // translates operations with int (>, <, etc)
    public String intOperationToJasmin(Operation operation){
        stack -= 2;
        switch(operation.getOpType()){
            case LTH:
                return "if_icmplt ";
            case GTH:
                return "if_icmpgt ";
            case EQ:
                return "if_icmpeq ";
            case NEQ:
                return "if_icmpne ";
            case GTE:
                return "if_icmpge ";
            case LTE:
                return "if_icmple ";
        }

        return "";
    }

    //translates operations with bool (&&)
    public String boolOperationToJasmin(Operation operation){
        stack -= 1;
        return "ifne ";
    }


    // translates operations with objects (==, !=)
    public String objOperationToJasmin(Operation operation){
        stack-=2;
        switch(operation.getOpType()){
            case EQ:
                return "if_acmpeq ";
            case NEQ:
                return "if_acmpne ";
        }
        return "";
    }

    // translates the instruction
    public String toJasmin(){
        StringBuilder builder = new StringBuilder();


        builder.append(UtilsJasmin.loadArgument(instruction.getLeftOperand(), method));
        maxReg = UtilsJasmin.changeMaxReg(maxReg, method, instruction.getLeftOperand());
        stack++;
        if(stack > maxStack){
            maxStack = stack;
        }

        String secondArg = UtilsJasmin.loadArgument(instruction.getRightOperand(), method);
        if(!secondArg.equals("iconst_1\n") && instruction.getLeftOperand().getType().getTypeOfElement() != ElementType.BOOLEAN){
            builder.append(UtilsJasmin.loadArgument(instruction.getRightOperand(), method));
            maxReg = UtilsJasmin.changeMaxReg(maxReg, method, instruction.getRightOperand());
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }
        }


        if(instruction.getLeftOperand().getType().toString().equals("INT32")){
            builder.append(intOperationToJasmin(instruction.getCondOperation()));
            builder.append(instruction.getLabel() + "\n");
        }
        else if(instruction.getLeftOperand().getType().getTypeOfElement() == ElementType.BOOLEAN){

            builder.append(boolOperationToJasmin(instruction.getCondOperation()));
            builder.append(instruction.getLabel() + "\n");
        }
        else{
            builder.append(objOperationToJasmin(instruction.getCondOperation()));
            builder.append(instruction.getLabel() + "\n");
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
