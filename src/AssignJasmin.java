import org.specs.comp.ollir.*;

import java.util.ArrayList;

public class AssignJasmin {

    private AssignInstruction instruction;
    private ClassUnit ollirClass;
    private Method method;
    private int stack;
    private int maxStack;
    private int maxReg;

    // Class that translates the assign instruction
    public AssignJasmin(AssignInstruction instruction, ClassUnit ollirClass, Method method) {
        this.instruction = instruction;
        this.ollirClass = ollirClass;
        this.method = method;
        stack = 0;
        maxStack = 0;
        maxReg = 0;
    }

    public String toJasmin(){
        StringBuilder builder = new StringBuilder();

        Operand left = (Operand) instruction.getDest();

        boolean isLeftArray = false;

        
        if(method.getVarTable().get(left.getName()).getVarType().getTypeOfElement() == ElementType.ARRAYREF && left instanceof ArrayOperand){
            isLeftArray = true;
            int virtualReg = UtilsJasmin.getVirtualReg(method, left.getName());
            maxReg = UtilsJasmin.changeMaxReg(maxReg, virtualReg);
            Load load = new Load(virtualReg, left, method);
            String loadJasmin = load.toJasmin();
            builder.append(loadJasmin);
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }
            ArrayOperand leftArray = (ArrayOperand) left;
            ArrayList<Element> indexOp = leftArray.getIndexOperands();
            Element idx = indexOp.get(0);
            builder.append(UtilsJasmin.loadArgument(idx, method));
            maxReg = UtilsJasmin.changeMaxReg(maxReg, method, idx);
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }
        }

        Instruction right = instruction.getRhs();

        switch(right.getInstType()){
            case NOPER:

                SingleOpInstruction ins = (SingleOpInstruction) right;
                if(ins.getSingleOperand().isLiteral()){
                    String literal = ((LiteralElement) ins.getSingleOperand()).getLiteral();
                    Const constantJasmin = new Const(literal);
                    String iconst = constantJasmin.toJasmin();
                    builder.append(iconst);
                    stack++;
                    if(stack > maxStack){
                        maxStack = stack;
                    }
                }

                else{ 

                    Operand rightOp = (Operand) ins.getSingleOperand();
                    builder.append(UtilsJasmin.loadArgument(ins.getSingleOperand(), method));
                    maxReg = UtilsJasmin.changeMaxReg(maxReg, method, ins.getSingleOperand());
                    stack++;
                    if(stack > maxStack){
                        maxStack = stack;
                    }
                    ElementType elementType = method.getVarTable().get(rightOp.getName()).getVarType().getTypeOfElement();
                    if(elementType == ElementType.ARRAYREF && rightOp instanceof ArrayOperand){
                        ArrayOperand arrayRight = (ArrayOperand) rightOp;
                        ArrayList<Element> indexOp = arrayRight.getIndexOperands();
                        Element index = indexOp.get(0);
                        builder.append(UtilsJasmin.loadArgument(index, method));
                        maxReg = UtilsJasmin.changeMaxReg(maxReg, method, index);
                        stack++;
                        if(stack > maxStack){
                            maxStack = stack;
                        }
                        builder.append("iaload\n");
                        stack--;
                    }

                }
                break;

            case BINARYOPER:
                BinaryOperJasmin binaryJasmin = new BinaryOperJasmin((BinaryOpInstruction) right, method);
                binaryJasmin.setStack(stack);
                builder.append(binaryJasmin.toJasmin());
                stack = binaryJasmin.getStack();
                if(stack > maxStack)
                    maxStack = stack;
                if(maxStack < binaryJasmin.getMaxStack())
                    maxStack = binaryJasmin.getMaxStack();
                maxReg = UtilsJasmin.changeMaxReg(maxReg, binaryJasmin.getMaxReg());
                break;

            case CALL:
                CallJasmin callJasmin = new CallJasmin((CallInstruction) right, method, ollirClass, true);
                callJasmin.setStack(stack);
                builder.append(callJasmin.toJasmin());
                stack = callJasmin.getStack();
                if(stack > maxStack)
                    maxStack = stack;
                if(maxStack < callJasmin.getMaxStack())
                    maxStack = callJasmin.getMaxStack();
                maxReg = UtilsJasmin.changeMaxReg(maxReg, callJasmin.getMaxReg());
                break;

            case GETFIELD:
                GetFieldJasmin getFieldJasmin = new GetFieldJasmin((GetFieldInstruction) right, method);
                getFieldJasmin.setStack(stack);
                builder.append(getFieldJasmin.toJasmin());
                stack = getFieldJasmin.getStack();
                if(stack > maxStack)
                    maxStack = stack;
                if(maxStack < getFieldJasmin.getMaxStack())
                    maxStack = getFieldJasmin.getMaxStack();
                maxReg = UtilsJasmin.changeMaxReg(maxReg, getFieldJasmin.getMaxReg());
                break;

            case UNARYOPER:
                UnaryOperJasmin unaryOperJasmin = new UnaryOperJasmin((UnaryOpInstruction) right, method);
                unaryOperJasmin.setStack(stack);
                builder.append(unaryOperJasmin.toJasmin());
                stack = unaryOperJasmin.getStack();
                if(stack > maxStack)
                    maxStack = stack;
                if(maxStack < unaryOperJasmin.getMaxStack())
                    maxStack = unaryOperJasmin.getMaxStack();
                maxReg = UtilsJasmin.changeMaxReg(maxReg, unaryOperJasmin.getMaxReg());
                break;
        }


        if(!isLeftArray){
            int leftVirtualReg = UtilsJasmin.getVirtualReg(method, left.getName());
            maxReg = UtilsJasmin.changeMaxReg(maxReg, leftVirtualReg);
            Store store = new Store(leftVirtualReg, left, method);
            builder.append(store.toJasmin());
            stack--;
        }
        else{
            builder.append("iastore\n");
            stack -= 3;

        }

        return builder.toString();
    }

    public int getStack(){
        return stack;
    }

    public int getMaxStack(){
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
