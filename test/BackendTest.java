
/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.specs.util.SpecsIo;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BackendTest {


    @Test
    public void testSimple() { //working

        String jmmCode = SpecsIo.getResource("fixtures/public/Simple.jmm");
        OptimizationStage opt = new OptimizationStage();
        OllirResult res=opt.toOllir(TestUtils.analyse(TestUtils.parse(jmmCode)));
        BackendStage result= new BackendStage();
        JasminResult jasmin = result.toJasmin(res);
        jasmin.run();

        WriteFile fileWriter = new WriteFile("fixtures/public/Simple.jmm", "fixtures/public/generated");
    
        fileWriter.writeFiles();
        assertTrue(true);
    }

    @Test
    public void testWhileAndIF(){ //working

        String jmmCode = SpecsIo.getResource("fixtures/public/WhileAndIF.jmm");
        OptimizationStage opt = new OptimizationStage();
        OllirResult res=opt.toOllir(TestUtils.analyse(TestUtils.parse(jmmCode)));
        BackendStage result= new BackendStage();
        JasminResult jasmin = result.toJasmin(res);
        jasmin.run();

        assertTrue(true);
    }

    @Test
    public void testHelloWorld(){ //working
        String jmmCode = SpecsIo.getResource("fixtures/public/HelloWorld.jmm");
        OptimizationStage opt = new OptimizationStage();
        OllirResult res=opt.toOllir(TestUtils.analyse(TestUtils.parse(jmmCode)));
        BackendStage result= new BackendStage();
        JasminResult jasmin = result.toJasmin(res);
        jasmin.run();

        assertTrue(true);
    }

    @Test
    public void testMyClass(){ //working
        String jmmCode = SpecsIo.getResource("fixtures/public/MyClass.jmm");
        OptimizationStage opt = new OptimizationStage();
        OllirResult res=opt.toOllir(TestUtils.analyse(TestUtils.parse(jmmCode)));
        BackendStage result= new BackendStage();
        JasminResult jasmin = result.toJasmin(res);
        jasmin.run();

        assertTrue(true);
    }

    @Test
    public void testCondition(){ //working
        String jmmCode = SpecsIo.getResource("fixtures/public/HelloWorld2.jmm");
        OptimizationStage opt = new OptimizationStage();
        OllirResult res=opt.toOllir(TestUtils.analyse(TestUtils.parse(jmmCode)));
        BackendStage result= new BackendStage();
        JasminResult jasmin = result.toJasmin(res);
        jasmin.run();

        assertTrue(true);
    }


    @Test
    public void testFindMaximum(){ // working 
        String jmmCode = SpecsIo.getResource("fixtures/public/FindMaximum.jmm");
        OptimizationStage opt = new OptimizationStage();
        OllirResult res = opt.toOllir(TestUtils.analyse(TestUtils.parse(jmmCode)));
        BackendStage result = new BackendStage();
        JasminResult jasmin = result.toJasmin(res);
        jasmin.run();

        assertTrue(true);
    }

    @Test
    public void testMonteCarloPi(){ // working :)
        String jmmCode = SpecsIo.getResource("fixtures/public/MonteCarloPi.jmm");
        OptimizationStage opt = new OptimizationStage();
        OllirResult res=opt.toOllir(TestUtils.analyse(TestUtils.parse(jmmCode)));
        BackendStage result= new BackendStage();
        JasminResult jasmin = result.toJasmin(res);
        jasmin.run();

        assertTrue(true);
    }


    @Test
    public void testQuicksort(){ //working
        String jmmCode = SpecsIo.getResource("fixtures/public/QuickSort.jmm");
        OptimizationStage opt = new OptimizationStage();
        OllirResult res=opt.toOllir(TestUtils.analyse(TestUtils.parse(jmmCode)));
        BackendStage result= new BackendStage();
        JasminResult jasmin = result.toJasmin(res);
        jasmin.run();

        assertTrue(true);
    }


    @Test
    public void testLazySort(){ //working
        String jmmCode = SpecsIo.getResource("fixtures/public/Lazysort.jmm");
        OptimizationStage opt = new OptimizationStage();
        OllirResult res=opt.toOllir(TestUtils.analyse(TestUtils.parse(jmmCode)));
        BackendStage result= new BackendStage();
        JasminResult jasmin = result.toJasmin(res);
        jasmin.run();


        assertTrue(true);
    }



    
}
