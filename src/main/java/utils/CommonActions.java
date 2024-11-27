package utils;

import com.aventstack.extentreports.Status;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPathException;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import com.jayway.jsonpath.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class CommonActions {

    private Response response = null;
    private  String request = "";
    Configuration configuration = Configuration.defaultConfiguration();
    public static Logger logger = LogManager.getLogger(CommonUtils.class.getName());

    public void getJsonPayload(String payloadName){
        request = CommonUtils.getJsonFileAsString(payloadName);
    }

    public void updateJsonPayload(String jsonPathKey, String value, String jsonPath){
        String checkValue = "";
        try{
            if(StringUtils.isNotEmpty(value)){
                checkValue = System.getProperty(value);
            }
            if(checkValue == null){
                value = CommonUtils.generateValue(value);
            }else{
                value = checkValue;
            }
            Configuration configuration = Configuration.defaultConfiguration();
            DocumentContext context = com.jayway.jsonpath.JsonPath.using(configuration).parse(request);
            value = value.replace("'","");
            request = context.set(jsonPath, value).jsonString();
            String actualValue = com.jayway.jsonpath.JsonPath.read(request, jsonPath);
            if(actualValue.trim().isEmpty()){
                Reporter.log(Status.PASS, "Updated "+jsonPathKey+" as <b>Blank</b>"+" in payload");
            } else {
                Reporter.log(actualValue.equalsIgnoreCase(value),"Updated "+jsonPathKey+" as <b>"+value+"</b> in payload");
            }
        }catch(Exception exception){
            Reporter.log(Status.FAIL, "Field <b>"+jsonPathKey+"</b> is not present in payload", jsonPathKey, "No Such Element Present in Json Request Payload");
        }
    }

    public void verifyValueInPayload(String jsonPathKey, String expectedValue, String jsonPath){
        try{
            String actualValue = com.jayway.jsonpath.JsonPath.read(request, jsonPath).toString();
            Reporter.log(actualValue.equalsIgnoreCase(expectedValue), "Verify value of field "+jsonPathKey+" is <b>"+expectedValue+"</b> in payload");
        }catch(JsonPathException jsonPathException){
            logger.error("Exception found while reading json payload. \n ERROR: {}", jsonPathException.getMessage());
        }
    }

    public Response sendRequest(String operationType, String uri, int responseCode){
        Properties properties = CommonUtils.loadPropertiesFile(Constants.CONFIG);
        uri = properties.getProperty("applicationEndpoint")+uri;
        Reporter.logHeading("Sending API Request - "+uri);
        response = CommonUtils.getResponse(operationType, request, uri);
        logRequestResponse(operationType);
        int statusCode = response.getStatusCode();
        Reporter.logHeading("VERIFY RESPONSE:");
        Reporter.log(responseCode==statusCode,"Status Code Validation: ", String.valueOf(responseCode), String.valueOf(statusCode));
        return response;
    }

    public void validateResponseFields(String jsonPathKey, String expectedValue, String jsonPath){
        String actualValue=null;

        switch(expectedValue.toUpperCase())
        {
            case "SPACE":
                expectedValue = " ";
                break;
            case "BLANK":
            case "NOT PRESENT":
                expectedValue = "No Such Element Present in Json Response";
                break;
        }
        try{
            JsonPath responseJsonPath = response.jsonPath();
            actualValue = responseJsonPath.get(jsonPath).toString();
        }catch(NullPointerException | IllegalArgumentException exception) {
            if(exception.toString().contains("Cannot invoke method getAt() on null object")||exception.toString().contains("NullPointer")){
                if(expectedValue.equalsIgnoreCase("No Such Element Present in Json Response")){
                    Reporter.log(Status.PASS, jsonPathKey+" is not present in response", expectedValue, null);
                    return;
                }
                else {
                   throw exception;
                }
            }

        }
        Reporter.log(expectedValue.equalsIgnoreCase(actualValue),"value of field "+jsonPathKey+" is present in response", expectedValue, actualValue);
    }

    public void removeFieldFromRequest(String jsonPath){
        try{
            DocumentContext documentContext = com.jayway.jsonpath.JsonPath.using(configuration).parse(request);
            request = documentContext.delete(jsonPath).jsonString();
            Reporter.log(Status.PASS, "Removed [ "+jsonPath+" ] from base payload");
        }catch(Exception exception){
            Reporter.log(Status.FAIL, "[ "+jsonPath+" ] path not found. <br>" +exception.getMessage());
        }
    }

    public void logRequestResponse(String operationType){
        Reporter.logHeading("REQUEST and RESPONSE");
        if(operationType.equalsIgnoreCase("GET"))
        {
            Reporter.logRequestResponse("{\n" +
                    "    \"Note\":\"This GET operation doesn't have request JSON body\"\n" +
                    "}",response.getBody().asString());
        }
        else{
            Reporter.logRequestResponse(request,response.getBody().asString());
        }
    }

    public boolean  isJsonPathExist(String jsonPath)
    {
        return response.getBody().jsonPath().get(jsonPath);
    }

}
