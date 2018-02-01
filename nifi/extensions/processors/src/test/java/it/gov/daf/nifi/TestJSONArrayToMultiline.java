package it.gov.daf.nifi;

import it.gov.daf.nifi.processors.DafJSONArrayToMultiline;

import org.apache.commons.io.IOUtils;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


public class TestJSONArrayToMultiline {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void testJSONArray() throws Exception {
        final TestRunner runner = TestRunners.newTestRunner(new DafJSONArrayToMultiline());
        runner.setValidateExpressionUsage(false);
        runner.assertValid();

        ClassLoader classLoader = getClass().getClassLoader();
        String testJson = "";
        try {
            testJson = IOUtils.toString(classLoader.getResourceAsStream("json/testarray.json"));
        } catch (IOException e) {
            log.error("", e);
        }

        byte[] flowContent = testJson.getBytes();

        runner.enqueue(flowContent);
        runner.run();

        final List<MockFlowFile> successFlowFiles = runner.getFlowFilesForRelationship("success");
        MockFlowFile result = successFlowFiles.get(0);
        result.assertContentEquals(classLoader.getResourceAsStream("json/resultarray.json"));
    }

    @Test
    public void testJSONNotAnArray() throws Exception {
        final TestRunner runner = TestRunners.newTestRunner(new DafJSONArrayToMultiline());
        runner.setValidateExpressionUsage(false);
        runner.assertValid();

        ClassLoader classLoader = getClass().getClassLoader();
        String testJson = "";
        try {
            testJson = IOUtils.toString(classLoader.getResourceAsStream("json/test.json"));
        } catch (IOException e) {
            log.error("", e);
        }

        byte[] flowContent = testJson.getBytes();

        runner.enqueue(flowContent);
        runner.run();

        final List<MockFlowFile> successFlowFiles = runner.getFlowFilesForRelationship("success");
        MockFlowFile result = successFlowFiles.get(0);
        result.assertContentEquals(classLoader.getResourceAsStream("json/test.json"));
    }

}