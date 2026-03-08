package com.API;

import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class LeadManager_API_Test {

    private String baseURI = "https://v0-lead-manager-app.vercel.app";
    private String authToken;
    private JSONObject requestBody;

    @Test
    public void validateApiEndFlow(){

        String response = given().baseUri(baseURI).header("Content-Type", "application/json")
                .body("{\n" +
                        "     \"email\":\"admin@company.com\",\n" +
                        "     \"password\":\"Admin@123\"\n" +
                        "}").log().all()
                .when().post("/api/login")
                .then().assertThat().statusCode(200).body("success", equalTo(true) ).body("role",equalTo("admin")).log().all()
                .extract().response().asString();
        JsonPath jsonPath = new JsonPath(response);
        authToken = jsonPath.getString("token");

        // Getting leads
        String getLeads = given().baseUri(baseURI).header("Content-Type", "application/json").header("Authorization", "Bearer "+authToken).log().all()
                .when().get("/api/leads").then().log().all().assertThat().statusCode(200).extract().response().asString();
        JSONObject jsonResponse = new JSONObject(getLeads);
        JSONArray leadsArray = jsonResponse.getJSONArray("leads");
        Assert.assertTrue(leadsArray.length() > 0, "Leads array is empty");

      //Creating leads
        JSONObject requestBody = new JSONObject();

        requestBody.put("name", "Sudhir");
        requestBody.put("email", "sudhir@test.com");
        requestBody.put("priority", "High");
        requestBody.put("status", "New");
        requestBody.put("isQualified", true);
        requestBody.put("isVip", false);
        requestBody.put("description", "Test Lead");

       String leadCreated = given().baseUri(baseURI).header("Content-Type", "application/json").header("Authorization", "Bearer "+authToken).body(requestBody.toString()).log().all()
               .when().post("/api/leads").then().log().all().assertThat().statusCode(201).body("lead.name", equalTo("Sudhir")).extract().response().asString();
        System.out.println("leadCreated "+leadCreated);
    }

}
