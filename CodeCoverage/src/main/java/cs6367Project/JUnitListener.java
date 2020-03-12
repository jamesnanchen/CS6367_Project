package cs6367Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class JUnitListener extends RunListener {

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);
        CoverageCollection.testSuite = new HashMap<String, HashMap<String, LinkedHashSet<Integer>>>();
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        String dir = "coverageLog";
        String logPath = dir + File.separator + "stmt-cov.txt";
        try {
            File directory = new File(dir);
            if (! directory.exists()){
                directory.mkdir();
            }

            File file = new File(logPath);
            if (!file.exists())
                file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            StringBuilder sb = new StringBuilder();
            for (String testName : CoverageCollection.testSuite.keySet()) {
                sb.append(testName + "\n");
                HashMap<String, LinkedHashSet<Integer>> testCase = CoverageCollection.testSuite.get(testName);

                for (String className : testCase.keySet()) {
                    for(int i : testCase.get(className)){
                        sb.append(className + ":" + i + "\n");
                    }
                }
            }
            bw.write(sb.toString());
            bw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testStarted(Description description) throws Exception {
        super.testStarted(description);
        CoverageCollection.testName = "[TEST] " + description.getClassName() + ":" + description.getMethodName();
        CoverageCollection.testCase = new HashMap<String, LinkedHashSet<Integer>>();
    }

    @Override
    public void testFinished(Description description) throws Exception {
        CoverageCollection.testSuite.put(CoverageCollection.testName,CoverageCollection.testCase);
        super.testFinished(description);

    }

}