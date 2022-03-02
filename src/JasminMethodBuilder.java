import org.specs.comp.ollir.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JasminMethodBuilder {

    private Method method;
    private ClassUnit ollirClass;
    private Boolean returned;
    private int stack;
    private int maxStack;
    private int maxReg;

    // Class that translates a method
    public JasminMethodBuilder(Method method, ClassUnit ollirClass) {
        this.method = method;
        this.ollirClass = ollirClass;
        this.returned = false;
        stack = 0;
        maxStack = 0;
        maxReg = 0;
    }

    // translates parameters
    public String paramToJasmin(){
        StringBuilder builder = new StringBuilder();

        ArrayList<Element> params = method.getParams();
        Iterator var4 = params.iterator();

        while(var4.hasNext()) {
            Element arg = (Element)var4.next();
            builder.append(UtilsJasmin.getElemType(arg));
        }

        return builder.toString();
    }

    // translates the method definition
    public String definitionToJasmin(){

        StringBuilder builder = new StringBuilder();

        builder.append(".method public ");

        if(method.isStaticMethod()){
            builder.append("static ");
        }

        if(method.isFinalMethod()){
            builder.append("final ");
        }

        if(!method.getMethodName().equals("main"))
            builder.append(method.getMethodName() + "(" + paramToJasmin()  + ")" +  UtilsJasmin.getElemType(method.getReturnType()) + "\n");
        else builder.append("main([Ljava/lang/String;)V\n");


        return builder.toString();
    }

    // translates a construct method
    public String dealConstructMethod(){
        StringBuilder builder = new StringBuilder();
        builder.append("    .method public <init>()V\n" +
                        "aload_0\n");
        builder.append("invokespecial ");
        if(ollirClass.getSuperClass() == null){
            builder.append("java/lang/Object");
        }
        else
            builder.append(ollirClass.getSuperClass());
        builder.append("/<init>()V\n");
        builder.append("return\n" +
                ".end method\n");
        return builder.toString();
    }

    // gets limit locals
    public String getLimitLocal(){
        int localVar = maxReg + 1;
        return ".limit locals " + localVar + "\n";
    }

    // gets limit stack
    public String getLimitStack(){
        return ".limit stack " + maxStack + "\n";
    }

    // translates by instruction
    public String instructionToJasmin(Instruction instruction){

        switch(instruction.getInstType()){
            case ASSIGN:
                AssignJasmin assignJasmin = new AssignJasmin((AssignInstruction) instruction, ollirClass, method);
                assignJasmin.setStack(stack);
                String assignString = assignJasmin.toJasmin();
                stack = assignJasmin.getStack();
                if(stack > maxStack)
                    maxStack = stack;
                if(maxStack < assignJasmin.getMaxStack())
                    maxStack = assignJasmin.getMaxStack();
                maxReg = UtilsJasmin.changeMaxReg(maxReg, assignJasmin.getMaxReg());
                return assignString;

            case CALL:
                CallJasmin callJasmin = new CallJasmin((CallInstruction) instruction, method, ollirClass, false);
                callJasmin.setStack(stack);
                String callString = callJasmin.toJasmin();
                stack = callJasmin.getStack();
                if(stack > maxStack)
                    maxStack = stack;
                if(maxStack < callJasmin.getMaxStack())
                    maxStack = callJasmin.getMaxStack();
                maxReg = UtilsJasmin.changeMaxReg(maxReg, callJasmin.getMaxReg());
                return callString;

            case GOTO:
                GoToJasmin goToJasmin = new GoToJasmin((GotoInstruction) instruction);
                return goToJasmin.toJasmin();

            case BRANCH:
                BranchJasmin branchJasmin = new BranchJasmin((CondBranchInstruction) instruction, method);
                branchJasmin.setStack(stack);
                String branchString = branchJasmin.toJasmin();
                stack = branchJasmin.getStack();
                if(stack > maxStack)
                    maxStack = stack;
                if(maxStack < branchJasmin.getMaxStack())
                    maxStack = branchJasmin.getMaxStack();
                maxReg = UtilsJasmin.changeMaxReg(maxReg, branchJasmin.getMaxReg());
                return branchString;

            case RETURN:
                ReturnJasmin returnJasmin = new ReturnJasmin((ReturnInstruction) instruction, method);
                returnJasmin.setStack(stack);
                String returnString = returnJasmin.toJasmin();
                stack = returnJasmin.getStack();
                if(stack > maxStack)
                    maxStack = stack;
                if(maxStack < returnJasmin.getMaxStack())
                    maxStack = returnJasmin.getMaxStack();
                returned = true;
                maxReg = UtilsJasmin.changeMaxReg(maxReg, returnJasmin.getMaxReg());
                return returnString;

            case PUTFIELD:
                PutFieldJasmin putFieldJasmin = new PutFieldJasmin((PutFieldInstruction) instruction, method);
                putFieldJasmin.setStack(stack);
                String putfieldString = putFieldJasmin.toJasmin();
                stack = putFieldJasmin.getStack();
                if(stack > maxStack)
                    maxStack = stack;
                if(maxStack < putFieldJasmin.getMaxStack())
                    maxStack = putFieldJasmin.getMaxStack();
                maxReg = UtilsJasmin.changeMaxReg(maxReg, putFieldJasmin.getMaxReg());
                return putfieldString;

            case BINARYOPER:
                BinaryOperJasmin binaryOperJasmin = new BinaryOperJasmin((BinaryOpInstruction) instruction, method);
                binaryOperJasmin.setStack(stack);
                String binaryString = binaryOperJasmin.toJasmin();
                stack = binaryOperJasmin.getStack();
                if(stack > maxStack)
                    maxStack = stack;
                if(maxStack < binaryOperJasmin.getMaxStack())
                    maxStack = binaryOperJasmin.getMaxStack();
                maxReg = UtilsJasmin.changeMaxReg(maxReg, binaryOperJasmin.getMaxReg());
                return binaryString;
        }
        return "";
    }

    // gets label by instruction
    public String getInstructionLabels(Instruction instruction){
        StringBuilder builder = new StringBuilder();

        List<String> labels = method.getLabels(instruction);

        if(labels.isEmpty())
            return "";

        for(String label : labels){
            builder.append(label + ":\n");
        }

        return builder.toString();
    }

    // translates a method to jasmin
    public String methodToJasmin(){
        StringBuilder builder = new StringBuilder();
        StringBuilder aux = new StringBuilder();

        if(method.isConstructMethod()){
            builder.append(dealConstructMethod());
            return builder.toString();
        }

        builder.append(definitionToJasmin());

        for(Instruction instruction : method.getInstructions()){

            aux.append(getInstructionLabels(instruction));
            aux.append(instructionToJasmin(instruction));

        }

        builder.append(getLimitLocal());
        builder.append(getLimitStack());
        builder.append(aux);

        if(!returned)
            builder.append("return\n");

        builder.append(".end method\n");


        return builder.toString();
    }
}
