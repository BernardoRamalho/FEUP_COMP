import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.Field;
import org.specs.comp.ollir.Method;

public class JasminBuilder {

    private ClassUnit ollirClass;

    // Class that translates the code to jasmin
    public JasminBuilder(ClassUnit ollirClass) {
        this.ollirClass = ollirClass;
    }

    // gets class directive
    public String classDirective(){
        return ".class public " + ollirClass.getClassName() + "\n";
    }

    // gets super directive
    public String superDirective(){
        return ".super " + ollirClass.getSuperClass() + "\n";
    }

    // gets private fields
    public String field(){
        StringBuilder builder = new StringBuilder();

        for(Field field : ollirClass.getFields()){
            builder.append(".field private '" + field.getFieldName() + "' " + UtilsJasmin.getElemType(field.getFieldType()) + "\n");
        }

        return builder.toString();
    }

    // translates to jasmin
    public String toJasmin(){

        StringBuilder builder = new StringBuilder();

        //source - optional

        builder.append(classDirective());

        if(ollirClass.getSuperClass() != null)
            builder.append(superDirective());
        else
            builder.append(".super java/lang/Object\n");


        builder.append(field());

        // for each method, translates it
        for(Method method : ollirClass.getMethods()){
            JasminMethodBuilder jasminMethod = new JasminMethodBuilder(method, ollirClass);
            builder.append(jasminMethod.methodToJasmin());
        }



        System.out.println(builder);

        return builder.toString();
    }


}
