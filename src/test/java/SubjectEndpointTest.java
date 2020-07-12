import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import api.App;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.*;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nyu.SubjectCode; 
import io.javalin.plugin.json.JavalinJson;

class EndpointTests {
    private static Javalin app = App.runTest();
    // private String usersJson = JavalinJson.toJson(UserController.users);

    @Test
    @DisplayName("Testing Subject Endpoint")
    public void TestSubjectEndpoint() {
        String subjectJson = JavalinJson.toJson(SubjectCode.getAvailableSubjectInfo());
        HttpResponse response = Unirest.get("http://localhost:8000/subjects").asString();
        assertEquals(response.getStatus(), 200, "Response status is the same for subject endpoint");
        assertEquals(response.getBody(), subjectJson, "Response is the same");

    }

    @Test
    @DisplayName("Testing School Endpoint")
    public void TestSchoolEndpoint() {
        String schoolJson = JavalinJson.toJson(SubjectCode.allSchools());
        HttpResponse response = Unirest.get("http://localhost:8000/schools").asString();
        assertEquals(response.getStatus(), 200, "Response status is the same for subject endpoint");
        assertEquals(response.getBody(), schoolJson, "Response is the same");
    }

    // Mkaking request to course then using reg number to make
    // request to section endpoints and verify that.
    @Test
    @DisplayName("Testing Course Endpoint")
    public void TestCourseEndpoint() {
        String subjectCode = "CSCI";
        String schoolCode = "UA";
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8000/2020/fa/UA/CSCI").asJson();
        assertEquals(response.getStatus(), 200, "Response status is the same for subject endpoint");
        JSONObject myObj = response.getBody().getObject();
        // extract fields from the object
        System.out.println(myObj);
        // String msg = myObj.getString("");
        // JSONArray results = myObj.getJSONArray();
    }
    
}