import static org.junit.Assert.*;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;
import java.io.StringReader;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

import pt.up.fe.comp.jmm.JmmParser;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmAnalysis;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;


public class ExampleTest {


  public boolean analyseCode(String path, boolean mustFail){
    String jmmCode = SpecsIo.getResource(path);
    JmmSemanticsResult jmmResult = TestUtils.analyse(jmmCode);

    if(mustFail){
      try {
        TestUtils.mustFail(jmmResult.getReports());
        return true;
      } catch (Exception e) {
        return false;
      }
    }
    try {
      TestUtils.noErrors(jmmResult.getReports());
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Test
    public void FindMaximumTest() {
    assertTrue(analyseCode("fixtures/public/FindMaximum.jmm", false));
  }

  @Test
    public void LazySortTest() {
    assertTrue(analyseCode("fixtures/public/Lazysort.jmm", false));
  }

  @Test
    public void LifeTest() {
    assertTrue(analyseCode("fixtures/public/Life.jmm", false));
  }

  @Test
    public void MonteCarloPiTest() {
    assertTrue(analyseCode("fixtures/public/MonteCarloPi.jmm", false));
  }

  @Test
    public void QuickSortTest() {
    assertTrue(analyseCode("fixtures/public/QuickSort.jmm", false));
  }

  @Test
    public void SimpleTest() {
    assertTrue(analyseCode("fixtures/public/Simple.jmm", false));
  }

  @Test
    public void TicTacToeTest() {
    assertTrue(analyseCode("fixtures/public/TicTacToe.jmm", false));
  }

  @Test
    public void WhileAndIFTest() {
    assertTrue(analyseCode("fixtures/public/WhileAndIF.jmm", false));
  }
  
}
