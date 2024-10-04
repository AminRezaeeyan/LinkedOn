package org.LinkedOn.server.DAO;

import org.LinkedOn.server.models.Education;
import org.LinkedOn.server.models.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface EducationDAO {

    String addEducation(Education education) throws SQLException;  // Returns the id of education (UUID)

    Education fetchEducation(String id) throws SQLException;

    ArrayList<Education> fetchUserEducations(String userId) throws SQLException;

    ArrayList<User> searchInstituteUsers(Education.Institute institute, int limit) throws SQLException;

    void updateEducation(Education education) throws SQLException;

    void removeEducation(String id) throws SQLException;
}
