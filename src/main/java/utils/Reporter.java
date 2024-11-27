package utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.aventstack.extentreports.ExtentReports;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class Reporter {

    public static ExtentReports extentReports = new ExtentReports();
    public static ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(Constants.REPORTER);
    public static ExtentTest test;

    public static void config() {
        extentSparkReporter.config().setTheme(Theme.STANDARD);
        extentSparkReporter.config().setDocumentTitle("Automation Execution Report");
        extentSparkReporter.config().setTimelineEnabled(false);
        extentSparkReporter.config().setReportName("API Test Automation Framework");
        extentSparkReporter.config().setCss("css-string");
        extentSparkReporter.config().setEncoding("ISO-8859-1");
        extentReports.attachReporter(extentSparkReporter);
        try {
            Reporter.extentReports.setSystemInfo("User Name", System.getenv().get("USERNAME"));
            Reporter.extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            Reporter.extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            Reporter.extentReports.setSystemInfo("Host Name", InetAddress.getLocalHost().getHostName());
            Reporter.extentReports.setSystemInfo("Executed Tags", System.getProperty("cucumber.filter.tags"));
        } catch(UnknownHostException unknownHostException) {
            log.error("Error Configuring Extent Report. Error message: \n {}", unknownHostException.getMessage());
        }
    }

    public static void createReport(String testCaseName, String scenarioDescription){
        test = extentReports.createTest(CommonUtils.getTestCaseName(testCaseName), "<b>Test Case Scenario: </b>" + scenarioDescription);
    }

    public static void assignCategory(String category){
        test.assignCategory(category);
    }

    public static void logHeading(String heading){
        test.info("<span style=\"color:blue;font-weight:bold\">" + heading + "</td><td>");
    }

    public static synchronized  void log(Status logStatus, String details){
        details = details.replaceAll("\\[","<b>[").replaceAll("]", "]</b>");
        switch (logStatus){
            case PASS:
                test.pass("<span style=\"color:black\"><div class=\"myDIV\">" + details + "</div>");
                break;
            case FAIL:
                test.fail("<span style=\"color:red\"><div class=\"myDIV\">" + details + "</div>");
                log.error(details);
                break;
            case INFO:
                test.info("<span style=\"color:blue\"><div class=\"myDIV\">" + details + "</div>");
                break;
            default:
                test.skip("<span style=\"color:yellow\"><div class=\"myDIV\">" + details + "</div>");
                break;
        }
    }

    public static void log(Status logStatus, String stepDetails, String expectedResult, String actualResult){
        String details = stepDetails + " <b>EXPECTED [</b>" + expectedResult + "<b>] ACTUAL [</b>" + actualResult + "<b>]</b>";
        if(stepDetails.length()>150){
            details = details.replace("<b>EXPECTED", "<br><b>EXPECTED");
        }
        switch(logStatus){
            case PASS:
                test.pass("<span style=\"color:black\">" + details);
                break;
            case FAIL:
                test.fail("<span style=\"color:red\">" + details);
                break;
            case INFO:
                test.info("<span style=\"color:blue\">" + details);
                break;
            case SKIP:
                test.skip("<span style=\"color:yellow\">" + details);
                break;
        }
    }

    public static void log(Boolean logStatus, String details, String expectedResult, String actualResult){
        if(logStatus){
            log(Status.PASS, details, expectedResult, actualResult);
        } else {
            log(Status.FAIL, details, expectedResult, actualResult);
        }
    }

    public static void log(Boolean logStatus, String details){
        if(logStatus){
            log(Status.PASS, details);
        } else {
            log(Status.FAIL, details);
        }
    }

    public static void logRequestResponse(String request, String response){
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode jsonNode = mapper.readTree(request);
            request = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            jsonNode = mapper.readTree(response);
            response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            test.info(MarkupHelper.createCodeBlock(request,response));
        } catch(JsonProcessingException jsonProcessingException) {
            Reporter.log(Status.INFO, jsonProcessingException.getMessage());
        }
    }

    public static void exitReport(){
        extentReports.flush();
    }
}
