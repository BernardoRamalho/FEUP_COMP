import org.specs.comp.ollir.*;

import java.util.ArrayList;
import java.util.Iterator;

public class CallJasmin {

    private CallInstruction instruction;
    private Method method;
    private ClassUnit ollirClass;
    private int stack;
    private int maxStack;
    private int maxReg;
    private Boolean isAssign;

    // Class that translates the call instruction to jasmin
    public CallJasmin(CallInstruction instruction, Method method, ClassUnit ollirClass, Boolean isAssign) {
        this.instruction = instruction;
        this.method = method;
        this.ollirClass = ollirClass;
        this.isAssign = isAssign;
        stack = 0;
        maxStack = 0;
        maxReg = 0;
    }

    // gets imports from class
    public String getClassImport(String className){ 

        for(String imp : ollirClass.getImports()){
            String[] imports = imp.split(".");
            if(imports.length == 0)
                return className;
            if(imports[imports.length - 1].equals(className)){
                return String.join("/", imports);
            }
        }
        return  className;
    }

    // gets arguments type
    public String getArgumentsType(){
        StringBuilder builder = new StringBuilder();

        ArrayList<Element> otherArgs = instruction.getListOfOperands();
        Iterator var4 = otherArgs.iterator();

        while(var4.hasNext()) {
            Element arg = (Element)var4.next();
            builder.append(UtilsJasmin.getElemType(arg));
        }

        return builder.toString();
    }

    // deals with invoke virtual calls
    public String dealInvokeVirtual(){ 
        StringBuilder builder = new StringBuilder();
 

        builder.append(UtilsJasmin.loadArgument(instruction.getFirstArg(), method));
        maxReg = UtilsJasmin.changeMaxReg(maxReg, method, instruction.getFirstArg());
        stack++;
        if(stack > maxStack){
            maxStack = stack;
        }

        ArrayList<Element> otherArgs = instruction.getListOfOperands();
        Iterator var4 = otherArgs.iterator();

        while(var4.hasNext()) {
            Element arg = (Element)var4.next();
            builder.append(UtilsJasmin.loadArgument(arg, method));
            maxReg = UtilsJasmin.changeMaxReg(maxReg, method, arg);
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }
        }



        String className = ((ClassType) instruction.getFirstArg().getType()).getName();


        String methodName = ((LiteralElement) instruction.getSecondArg()).getLiteral();
        builder.append("invokevirtual " + getClassImport(className) + "." + methodName.substring(1, methodName.length()-1) + "(" + getArgumentsType() + ")" + UtilsJasmin.getElemType(instruction.getReturnType()) + "\n");

        stack -= (instruction.getListOfOperands().size() + 1);

        if(instruction.getReturnType().getTypeOfElement() != ElementType.VOID){
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }

        }
        return builder.toString();
    }

     // deals with invoke special calls
    public String dealInvokeSpecial(){
        StringBuilder builder = new StringBuilder();

        builder.append(UtilsJasmin.loadArgument(instruction.getFirstArg(), method));
        maxReg = UtilsJasmin.changeMaxReg(maxReg, method, instruction.getFirstArg());
        stack++;
        if(stack > maxStack){
            maxStack = stack;
        }

        String className = ((ClassType) instruction.getFirstArg().getType()).getName();

        builder.append("invokespecial " + getClassImport(className)+".<init>()V\n");

        stack--;

        return builder.toString();
    }

     // deals with invoke static calls
    public String dealInvokeStatic(){
        StringBuilder builder = new StringBuilder();

        ArrayList<Element> otherArgs = instruction.getListOfOperands();
        Iterator var4 = otherArgs.iterator();

        while(var4.hasNext()) {
            Element arg = (Element)var4.next();
            builder.append(UtilsJasmin.loadArgument(arg, method));
            maxReg = UtilsJasmin.changeMaxReg(maxReg, method, arg);
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }
        }

        String firstArgName = ((Operand) instruction.getFirstArg()).getName();
        String methodName = ((LiteralElement) instruction.getSecondArg()).getLiteral();

        builder.append("invokestatic " + getClassImport(firstArgName) + "." + methodName.substring(1, methodName.length()-1) + "(" + getArgumentsType() + ")" + UtilsJasmin.getElemType(instruction.getReturnType()) + "\n");

        stack -= instruction.getListOfOperands().size() - 1;


        return builder.toString();
    }

     // deals with new array calls
    public String dealNewArray(){ 
        StringBuilder builder = new StringBuilder();

        ArrayList<Element> otherArgs = instruction.getListOfOperands();
        Iterator var4 = otherArgs.iterator();

        while(var4.hasNext()) {
            Element arg = (Element)var4.next();
            builder.append(UtilsJasmin.loadArgument(arg, method));
            maxReg = UtilsJasmin.changeMaxReg(maxReg, method, arg);
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }

        }

        builder.append("newarray int\n");
        stack -= instruction.getListOfOperands().size() - 1;

        return builder.toString();
    }

     // deals with new object calls
    public String dealNewObject(){
        StringBuilder builder = new StringBuilder();

        ArrayList<Element> otherArgs = instruction.getListOfOperands();
        Iterator var4 = otherArgs.iterator();

        while(var4.hasNext()) {
            Element arg = (Element)var4.next();
            builder.append(UtilsJasmin.loadArgument(arg, method));
            maxReg = UtilsJasmin.changeMaxReg(maxReg, method, arg);
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }

        }

        String className = ((ClassType) instruction.getFirstArg().getType()).getName();

        builder.append("new " + className + "\n");
        stack++;
        if(stack > maxStack){
            maxStack = stack;
        }


        return builder.toString();
    }

    // deals with method invocation
    public String dealMethodInvocation(){
        StringBuilder builder = new StringBuilder();

        if(instruction.getNumOperands() > 0){

            builder.append(UtilsJasmin.loadArgument(instruction.getFirstArg(), method));
            maxReg = UtilsJasmin.changeMaxReg(maxReg, method, instruction.getFirstArg());
/*            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }*/


            if(instruction.getNumOperands() > 1){

                if(instruction.getInvocationType() != CallType.NEW){
                    builder.append(UtilsJasmin.loadArgument(instruction.getSecondArg(), method));
                    maxReg = UtilsJasmin.changeMaxReg(maxReg,method, instruction.getSecondArg());
/*
                    stack++;
                    if(stack > maxStack){
                        maxStack = stack;
                    }
*/

                    ArrayList<Element> otherArgs = instruction.getListOfOperands();
                    Iterator var4 = otherArgs.iterator();

                    while(var4.hasNext()) {
                        Element arg = (Element)var4.next();
                        builder.append(UtilsJasmin.loadArgument(arg, method));
                        maxReg = UtilsJasmin.changeMaxReg(maxReg, method, arg);
/*                        stack++;
                        if(stack > maxStack){
                            maxStack = stack;
                        }*/
                    }
                }
            }
        }

        builder.append(instruction.getInvocationType() + "\n");

        return builder.toString();
    }

    // translates to jasmin
    public String toJasmin(){
        StringBuilder builder = new StringBuilder();

        if(instruction.getInvocationType().toString().equals("invokevirtual")){

            builder.append(dealInvokeVirtual());

        }
        else if(instruction.getInvocationType().toString().equals("invokespecial")){

            builder.append(dealInvokeSpecial());

        }
        else if(instruction.getInvocationType().toString().equals("invokestatic")){

            builder.append(dealInvokeStatic());
        }
        else if(instruction.getInvocationType().toString().equals("NEW")){

            if(instruction.getFirstArg().getType().getTypeOfElement() == ElementType.ARRAYREF)
                builder.append(dealNewArray());
            else
                builder.append(dealNewObject());
        }
        else{


            builder.append(dealMethodInvocation());
            stack++;
            if(stack > maxStack){
                maxStack = stack;
            }

        }

        if(!isAssign && instruction.getReturnType().getTypeOfElement() != ElementType.VOID){
            builder.append("pop\n");
            stack--;
        }

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
