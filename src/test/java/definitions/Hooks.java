package definitions;

import io.cucumber.java.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import utils.Reporter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Hooks {

    @BeforeAll
    public static void ConfigureExtentReport(){
        log.info("--------------------------- Test Suite Execution Has Started! ---------------------------");
        Reporter.config();
    }

    @Before
    public void createReport(Scenario scenario){
        List<String> tagNames = new ArrayList<>(scenario.getSourceTagNames());
        Reporter.createReport(scenario.getUri().toString(), scenario.getName());
        Reporter.assignCategory(tagNames.get(0).replace("@",""));
    }

    @After
    public static void closeReport(Scenario scenario){
        String scenarioResult = scenario.getStatus().toString().equalsIgnoreCase("PASSED") ? "Pass" :"Fail";
        String reportResult = Reporter.test.getStatus().getName().equalsIgnoreCase("PASS") ? "Pass" :"Fail";
        String status = scenarioResult.equalsIgnoreCase("PASS") && reportResult.equalsIgnoreCase("PASS") ? "Pass" : "Fail";
        Reporter.exitReport();
        if(!scenarioResult.equalsIgnoreCase(reportResult)){
            Assert.fail("Extent report result does not match to scenario output");
        }
    }

    @AfterAll
    public static void tearDown(){
        log.info("--------------------------- Test Suite Execution Has Ended! ---------------------------");
    }
}
