public class Const {

    private String literal;

    // Loads constants
    public Const(String literal) {
        this.literal = literal;
    }

    // translates to jasmin
    public String toJasmin(){
        int constant = Integer.valueOf(literal);
        if(constant >= -1 && constant <= 5)
            return "iconst_" + literal.trim() + "\n";
        else if(constant >= -128 && constant <= 127)
            return "bipush " + literal.trim() + "\n";
        else if(constant >= -32768 && constant <= 32767)
            return "sipush " + literal.trim() + "\n";
        else
            return "ldc " +  literal.trim() + "\n";
    }
}
