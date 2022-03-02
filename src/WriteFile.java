import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.jasmin.JasminUtils;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.comp.TestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WriteFile {

    private String fileName;
    private String directory;
    private String className;

    // class that writes on files
    public WriteFile(String fileName, String directory) {
        this.fileName = fileName;
        this.directory = directory;

        String[] paths = fileName.split("/");
        String[] classArr = paths[paths.length - 1].split("\\.");
        className = classArr[0];

    }

    public void writeFiles(){


        JmmParserResult result;

        String jmmCode = SpecsIo.getResource(fileName);
        result = TestUtils.parse(jmmCode);

        JmmNode root = result.getRootNode(); // returns reference to root node


        // Printing reports
        System.out.println(result.getReports().toString());

        String jsonFile = directory +"/"+ className  + ".json";

        String jsonTree = "";
        try {
            jsonTree = root.toJson();
            Files.deleteIfExists(Paths.get(jsonFile));
            Files.createFile(Paths.get(jsonFile));
            Files.write(Paths.get(jsonFile), jsonTree.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnalysisStage analysisStage = new AnalysisStage();
        JmmSemanticsResult semanticsResult = analysisStage.semanticAnalysis(result);
        System.out.println("\n\n\n"+ semanticsResult.getReports());

        String symbolsFile = directory +"/"+ className + ".symbols.txt";

/*
        try {
            String symbolTableFile = semanticsResult.getSymbolTable().print();
            Files.deleteIfExists(Paths.get(symbolsFile));
            Files.createFile(Paths.get(symbolsFile));
            Files.write(Paths.get(symbolsFile), symbolTableFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        //Ollir
        OptimizationStage optimizationStage = new OptimizationStage();
        OllirResult ollirResult = optimizationStage.toOllir(analysisStage.semanticAnalysis(result));

        String ollirFile = directory +"/"+ className + ".ollir";

        try {
            String ollirCode = ollirResult.getOllirCode();
            Files.deleteIfExists(Paths.get(ollirFile));
            Files.createFile(Paths.get(ollirFile));
            Files.write(Paths.get(ollirFile), ollirCode.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Jasmin
        BackendStage backendStage = new BackendStage();
        JasminResult jasminResult = backendStage.toJasmin(ollirResult);
        System.out.println(jasminResult.getJasminCode());

        String jasminFile = directory +"/"+ className + ".j";

        try {
            String jasmminCode = jasminResult.getJasminCode();
            Files.deleteIfExists(Paths.get(jasminFile));
            Files.createFile(Paths.get(jasminFile));
            Files.write(Paths.get(jasminFile), jasmminCode.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Run
        jasminResult.run();
        JasminUtils.assemble(new File(jasminFile), new File(directory));

    }

    public void writeFilesFromMain(JmmParserResult parserResult, JmmSemanticsResult semanticsResult, OllirResult ollirResult, JasminResult jasminResult){
        JmmNode root = parserResult.getRootNode();

        String jsonFile = directory +"/"+ className  + ".json";

        String jsonTree = "";
        try {
            jsonTree = root.toJson();
            Files.deleteIfExists(Paths.get(jsonFile));
            Files.createFile(Paths.get(jsonFile));
            Files.write(Paths.get(jsonFile), jsonTree.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String symbolsFile = directory +"/"+ className + ".symbols.txt";

/*        try {
            String symbolTableFile = semanticsResult.getSymbolTable().print();
            Files.deleteIfExists(Paths.get(symbolsFile));
            Files.createFile(Paths.get(symbolsFile));
            Files.write(Paths.get(symbolsFile), symbolTableFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        String ollirFile = directory +"/"+ className + ".ollir";

        try {
            String ollirCode = ollirResult.getOllirCode();
            Files.deleteIfExists(Paths.get(ollirFile));
            Files.createFile(Paths.get(ollirFile));
            Files.write(Paths.get(ollirFile), ollirCode.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jasminFile = directory +"/"+ className + ".j";

        try {
            String jasmminCode = jasminResult.getJasminCode();
            Files.deleteIfExists(Paths.get(jasminFile));
            Files.createFile(Paths.get(jasminFile));
            Files.write(Paths.get(jasminFile), jasmminCode.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }


        JasminUtils.assemble(new File(jasminFile), new File(directory));

    }

}
