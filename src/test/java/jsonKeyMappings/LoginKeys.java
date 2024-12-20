package jsonKeyMappings;

public enum LoginKeys {
    test("test");

    private final String jsonPath;
    LoginKeys(String jsonPath){
        this.jsonPath = jsonPath;
    }

    public String getPath(){
        return jsonPath;
    }
}
