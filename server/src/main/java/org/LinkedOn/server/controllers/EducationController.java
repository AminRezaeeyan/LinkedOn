package org.LinkedOn.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.LinkedOn.server.DAO.EducationDAO;
import org.LinkedOn.server.DAO.impl.EducationDAOImpl;
import org.LinkedOn.server.models.Education;
import org.LinkedOn.server.models.User;
import org.LinkedOn.server.utils.Json;

import java.sql.SQLException;
import java.util.ArrayList;

public class EducationController {
    private static EducationController instance;
    private final EducationDAO educationDAO;

    private EducationController(){
        educationDAO = EducationDAOImpl.getInstance();
    }

    public synchronized static EducationController getInstance(){
        if (instance == null) instance = new EducationController();
        return instance;
    }

    public synchronized String addEducation(Education education) throws SQLException {
        return educationDAO.addEducation(education);
    }

    public synchronized String fetchEducation(String id) throws SQLException, JsonProcessingException {
        Education education = educationDAO.fetchEducation(id);
        return Json.toJson(education);
    }

    public synchronized String fetchUserEducations(String userId) throws SQLException, JsonProcessingException {
        ArrayList<Education> educations = educationDAO.fetchUserEducations(userId);
        return Json.toJsonArray(educations);
    }

    public synchronized String searchInstituteUsers(Education.Institute institute) throws SQLException, JsonProcessingException {
        ArrayList<User> users = educationDAO.searchInstituteUsers(institute, 10);
        return Json.toJsonArrayWithView(users, Json.Views.Brief.class);
    }

    public synchronized void updateEducation(Education education) throws SQLException{
        educationDAO.updateEducation(education);
    }

    public synchronized void removeEducation(String id) throws SQLException {
        educationDAO.removeEducation(id);
    }
}
