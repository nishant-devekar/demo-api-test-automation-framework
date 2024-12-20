package definitions;

import com.aventstack.extentreports.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import jsonKeyMappings.LoginKeys;
import utils.CommonActions;
import utils.CommonUtils;
import utils.Constants;
import utils.Reporter;

import java.util.Properties;

public class StepDefinition {

    CommonActions commonActions = new CommonActions();
    Properties properties = CommonUtils.loadPropertiesFile(Constants.CONFIG);
    String basePayloadName = "";
    Response response;
    String token;


    @Given("Take base payload of {string}")
    public void takeBasePayload(String payloadName) {
        basePayloadName = payloadName;
        if (payloadName.equalsIgnoreCase("login")) {
            commonActions.getJsonPayload(Constants.loginPayload);
        } else {
            Reporter.log(Status.FAIL, "Please enter valid base payload name (file name of payload)");
        }
    }

    @And("Update {string} with {string} as {string}")
    public void updatePayload(String basePayloadName, String objectFieldName, String objectFieldValue) {
        String jsonPath = "";
        try {
            switch (basePayloadName.toUpperCase()) {
                case "LOGIN":
                    jsonPath = LoginKeys.valueOf(objectFieldName).getPath();
                    commonActions.updateJsonPayload(objectFieldName, objectFieldValue, jsonPath);
                    break;
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            commonActions.updateJsonPayload(objectFieldName, objectFieldValue, objectFieldName);

        }
    }

    @When("User sends {string} API request and get response code as {int}")
    public void sendAPIRequest(String payloadType, int responseCode) {
        switch (payloadType.toUpperCase()) {
            case "LOGIN":
                String loginURI = properties.getProperty("login");
                response = commonActions.sendRequest("POST", loginURI, responseCode);
                token = response.jsonPath().get("token").toString();
                break;
            case "LOGOUT":
                String logoutURI = properties.getProperty("logout");
                commonActions.sendRequest("POST", logoutURI+token, responseCode);
        }

    }

    @Then("Verify {string} present as {string} in response")
    public void verifyPresentAsInResponse(String objectFieldName, String objectFieldValue) {
        String jsonPath = "";
        try {
            switch (basePayloadName.toUpperCase()) {
                case "LOGIN":
                    jsonPath = LoginKeys.valueOf(objectFieldName).getPath();
                    commonActions.validateResponseFields(objectFieldName, objectFieldValue, jsonPath);
                    break;
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            commonActions.validateResponseFields(objectFieldName, objectFieldValue, objectFieldName);

        }
    }

}
