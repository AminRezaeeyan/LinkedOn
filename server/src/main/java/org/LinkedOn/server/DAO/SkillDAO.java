package org.LinkedOn.server.DAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SkillDAO {
    void addSkill(String userId, String skill) throws SQLException;

    ArrayList<String> fetchUserSkills(String userId) throws SQLException;

    ArrayList<String> fetchSkills() throws SQLException;

    void removeSkill(String userId, String skill) throws SQLException;
}
