import org.specs.comp.ollir.GotoInstruction;

public class GoToJasmin {

    private GotoInstruction instruction;

    //Class that translates go to instructions
    public GoToJasmin(GotoInstruction instruction) {
        this.instruction = instruction;
    }

    public String toJasmin(){
        StringBuilder builder = new StringBuilder();
        builder.append("goto ");
        builder.append(instruction.getLabel() + "\n");
        return builder.toString();
    }

}
