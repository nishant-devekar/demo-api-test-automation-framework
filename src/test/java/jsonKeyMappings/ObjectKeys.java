package jsonKeyMappings;

public enum ObjectKeys {
    id("id"),
    name("name"),
    message("message");

    private final String jsonPath;
    ObjectKeys(String jsonPath){
        this.jsonPath = jsonPath;
    }

    public String getPath(){
        return jsonPath;
    }
}
