
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmAnalysis;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.ast.examples.ExamplePostorderVisitor;
import pt.up.fe.comp.jmm.ast.examples.ExamplePreorderVisitor;
import pt.up.fe.comp.jmm.ast.examples.ExamplePrintVariables;
import pt.up.fe.comp.jmm.ast.examples.ExampleVisitor;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;


public class AnalysisStage implements JmmAnalysis {

    @Override
    public JmmSemanticsResult semanticAnalysis(JmmParserResult parserResult) {

        if (TestUtils.getNumReports(parserResult.getReports(), ReportType.ERROR) > 0) {
            var errorReport = new Report(ReportType.ERROR, Stage.SEMANTIC, -1,
                    "Started semantic analysis but there are errors from previous stage");
            return new JmmSemanticsResult(parserResult, null, Arrays.asList(errorReport));
        }

        if (parserResult.getRootNode() == null) {
            var errorReport = new Report(ReportType.ERROR, Stage.SEMANTIC, -1,
                    "Started semantic analysis but AST root node is null");
            return new JmmSemanticsResult(parserResult, null, Arrays.asList(errorReport));
        }


        JmmNode node = parserResult.getRootNode();

        CustomSymbolTable symbolTable = new CustomSymbolTable();

        List<Report> reports = new ArrayList<>();

        FillSymbolTableVisitor visitor = new FillSymbolTableVisitor();
        


        // The visitor will use the symbolTable and the root node to fill the array List of reports. If the array is empty then no semantic errors have occured
        visitor.visit(node, symbolTable);

        // System.out.println(symbolTable.getClassName());
        // System.out.println(symbolTable.getSuper());
        // System.out.println(symbolTable.getImports());
        // System.out.println(symbolTable.getMethods());
        // System.out.println(symbolTable.getPureMethods());
        // System.out.println(symbolTable.getFields());


        AnalysisVisitor anaVisitor = new AnalysisVisitor(symbolTable, reports);

        anaVisitor.visit(node, false);

        System.out.println(reports);
    
        return new JmmSemanticsResult(parserResult, symbolTable, reports);

    }

}