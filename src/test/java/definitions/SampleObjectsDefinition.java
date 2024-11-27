package definitions;

import com.aventstack.extentreports.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import jsonKeyMappings.ObjectKeys;
import utils.CommonActions;
import utils.CommonUtils;
import utils.Constants;
import utils.Reporter;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SampleObjectsDefinition {

    CommonActions commonActions = new CommonActions();
    Properties properties = CommonUtils.loadPropertiesFile(Constants.CONFIG);
    String objectsURI = properties.getProperty("objects");
    String basePayloadName="";
    String generatedID = "";
    Response response;

    @Given("{string} list of all objects")
    public void listOfAllObjects(String operationType) {
        commonActions.sendRequest(operationType,objectsURI,200);
    }

    @When("{string} list of objects by ID's {string}")
    public void listOfObjectsByIDS(String operationType, String objectIDs) {
        objectIDs = "?id="+objectIDs.replace(",","&id=");
        commonActions.sendRequest(operationType,objectsURI+objectIDs,200);
    }

    @Then("Verify {string} present in response")
    public void verifyArePresentInResponse(String objectIDs) {
        if(objectIDs.contains(",")) {
            List<String> IDs = Arrays.asList(objectIDs.split(","));
            for(int i=0;i<IDs.size();i++) {
                commonActions.validateResponseFields("id",IDs.get(i),"["+i+"].id");
            }
        }else{
            commonActions.validateResponseFields("id",objectIDs,"id");
        }

    }

    @Then("{string} single object by {string}")
    public void searchSingleObject(String operationType, String objectID) {
        String URI = objectsURI+"/"+objectID;
        commonActions.sendRequest(operationType,URI,200);
    }

    @Given("Take base payload of {string}")
    public void takeBasePayload(String payloadName) {
        basePayloadName = payloadName;
        if(payloadName.equalsIgnoreCase("object")) {
            commonActions.getJsonPayload(Constants.objectBasePayload);
        }else{
            Reporter.log(Status.FAIL,"Please enter valid base payload name (file name of payload)");
        }
    }

    @And("Update {string} with {string} as {string}")
    public void updatePayload(String basePayloadName,String objectFieldName, String objectFieldValue) {
        String jsonPath = "";
        switch(basePayloadName.toUpperCase()){
            case "OBJECT":
                jsonPath = ObjectKeys.valueOf(objectFieldName).getPath();
                break;
        }
        commonActions.updateJsonPayload(objectFieldName,objectFieldValue,jsonPath);
    }

    @And("{string} add an object")
    public void addAnObject(String operationType) {
        response = commonActions.sendRequest(operationType,objectsURI,200);
        generatedID = response.jsonPath().get("id").toString();

    }

    @Then("Verify {string} present as {string} in response")
    public void verifyPresentAsInResponse(String objectFieldName, String objectFieldValue) {
        String jsonPath = "";
        switch(basePayloadName.toUpperCase()){
            case "OBJECT":
                jsonPath = ObjectKeys.valueOf(objectFieldName).getPath();
                break;
        }
        if(objectFieldValue.contains("GENERATED_ID"))
        {
            objectFieldValue = objectFieldValue.replace("GENERATED_ID",generatedID);
        }
        commonActions.validateResponseFields(objectFieldName,objectFieldValue,jsonPath);
    }

    @And("{string} update an object of {string}")
    public void updateAnObjectOfID(String operationType, String objectID) {
        if(objectID.equalsIgnoreCase("GENERATED_ID"))
        {
            objectID = generatedID;
        }
        commonActions.sendRequest(operationType,objectsURI+"/"+objectID,200);

    }

    @And("{string} object of {string}")
    public void deleteObjectOfID(String operationType, String objectID) {
        if(objectID.equalsIgnoreCase("GENERATED_ID"))
        {
            objectID = generatedID;
        }
        commonActions.sendRequest(operationType,objectsURI+"/"+objectID,200);
    }


}
