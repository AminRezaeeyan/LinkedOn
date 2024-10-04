package org.linkedon.client.services;

import org.linkedon.client.exceptions.ApplicationException;
import org.linkedon.client.models.Education;
import org.linkedon.client.utils.HTTP;
import org.linkedon.client.utils.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EducationService {
    private static EducationService instance;

    private EducationService(){}

    public static EducationService getInstance(){
        if (instance == null) instance = new EducationService();
        return instance;
    }

    public String addEducation(Education education, String authToken) throws ApplicationException, IOException {
        String educationJson = Json.toJson(education);
        HashMap<String, String> response = HTTP.sendRequest("/educations", "POST", authToken, educationJson);
        return response.get("body");
    }

    public ArrayList<Education> fetchUserEducations(String userId, String authToken) throws ApplicationException, IOException {
        HashMap<String, String> response = HTTP.sendRequest(STR."/users/\{userId}/educations", "GET", authToken, (String) null);
        String educationsJson = response.get("body");
        return (ArrayList<Education>) Json.fromJsonArray(educationsJson, Education[].class);
    }
}
