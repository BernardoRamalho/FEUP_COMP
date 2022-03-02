
import pt.up.fe.comp.jmm.JmmParser;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.report.Report;



import java.util.Arrays;
import java.util.ArrayList;
import java.io.StringReader;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.specs.util.SpecsIo;

import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmAnalysis;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;

public class Main implements JmmParser {

	public JmmParserResult parse(String jmmCode) {

        try{
            JavaParser parser = new JavaParser(new StringReader(jmmCode));

            SimpleNode root = parser.parse();

            root.dump("");

            System.out.println("ERRO");
            String json = root.toJson();
            System.out.println("ERRO");
            FileWriter file = new FileWriter("src/ast.json");
            
            file.write(json);
            file.close();
            System.out.println(json);

            return new JmmParserResult(root, new ArrayList<Report>());
            
        }catch(Exception e){
            System.out.println("Error:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException , ParseException{

        System.out.println(args[0]);

        String jmmCode = SpecsIo.read(args[0]);
        // String jmmCode = Files.readString(Path.of(args[0]));
        
        System.out.println(jmmCode);

        JmmParser parser = new Main();

        JmmParserResult parserResult = parser.parse(jmmCode);

        AnalysisStage analyse = new AnalysisStage();

        JmmSemanticsResult sematicRes = analyse.semanticAnalysis(parserResult);
        
        //JmmParserResult parserRes =  TestUtils.parse(jmmCode);

        OptimizationStage opt = new OptimizationStage();
        
        OllirResult res = opt.toOllir(sematicRes);

        BackendStage result = new BackendStage();
        
        JasminResult jasmin = result.toJasmin(res);

        //jasmin.run();

		// JavaParser parser = new JavaParser(new StringReader(SpecsIo.getResource(args[0])));

        // SimpleNode root = parser.parse();

        // root.dump("");
        WriteFile fileWriter = new WriteFile("../" + args[0], "test/fixtures/public/generated");
        fileWriter.writeFilesFromMain(parserResult, sematicRes, res, jasmin);

    }

}