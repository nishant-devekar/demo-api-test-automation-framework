package utils;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class CommonUtils {

    public static final Map<String, Integer> mapTestCaseName = new HashMap<>();
    public static Logger logger = LogManager.getLogger(CommonUtils.class.getName());

    public static Properties loadPropertiesFile(final String propertyFileName) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = Files.newInputStream(Paths.get(propertyFileName));
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException ioException) {
            logger.error("Cannot open properties file: {} exception: \n{}", propertyFileName, ioException);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    logger.error("loadPropertiesFile(): \n{}", String.valueOf(ioException));
                }
            }
        }
        return properties;
    }

    public static String getTestCaseName(String featureName) {
        String[] testDetails = featureName.split("/");
        String testCaseName = testDetails[testDetails.length - 1].replace(".feature", "");

        if (mapTestCaseName.containsKey(testCaseName)) {
            mapTestCaseName.put(testCaseName, mapTestCaseName.get(testCaseName) + 1);
        } else {
            mapTestCaseName.put(testCaseName, 1);
        }
        return testCaseName;
    }

    public static String getJsonFileAsString(String jsonFileName) {
        String jsonPayload = "";
        try {
            FileReader fileReader = new FileReader(jsonFileName);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
            jsonPayload = jsonObject.toJSONString();
        } catch (IOException | ParseException ioException) {
            logger.error("Exception found while reading file [{}]. \n ERROR: {}", jsonPayload, ioException.getMessage());
        }
        return jsonPayload;
    }

    public static String generateValue(String valueType){
        if(System.getProperty(valueType)!=null){
            return System.getProperty(valueType);
        }
        StringBuilder stringBuilder;
        switch (valueType.toUpperCase()){
            case "BLANK":
                stringBuilder = new StringBuilder();
                break;
            case "SPACE":
                stringBuilder = new StringBuilder(" ");
                break;
            default:
                stringBuilder = new StringBuilder(valueType);
                break;
        }
        System.setProperty(valueType, stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static Response getResponse(String operationType, String request, String uri){
        Response response = null;
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("Content-Type", "application/json").body(request);
        switch (operationType.toUpperCase()){
            case "GET":
                response = httpRequest.request(Method.GET, uri);
                break;
            case "POST":
                response = httpRequest.request(Method.POST, uri);
                break;
            case "PUT":
                response = httpRequest.request(Method.PUT, uri);
                break;
            case "DELETE":
                response = httpRequest.request(Method.DELETE, uri);
                break;
        }
        return response;
    }
}
