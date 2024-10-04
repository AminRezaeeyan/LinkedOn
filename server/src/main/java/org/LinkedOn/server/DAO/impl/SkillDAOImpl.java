package org.LinkedOn.server.DAO.impl;

import org.LinkedOn.server.DAO.SkillDAO;
import org.LinkedOn.server.utils.Database;
import org.LinkedOn.server.utils.UUIDUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SkillDAOImpl implements SkillDAO {
    private static SkillDAOImpl instance;
    private final Connection connection;

    private SkillDAOImpl() {
        connection = Database.connect();
    }

    public static synchronized SkillDAOImpl getInstance() {
        if (instance == null) instance = new SkillDAOImpl();
        return instance;
    }

    @Override
    public synchronized void addSkill(String userId, String skill) throws SQLException {
        String query = "INSERT INTO user_skills VALUES (?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            int skillId = fetchSkillId(skill);
            if (skillId == -1) return;

            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            ps.setInt(2, skillId);
            ps.executeUpdate();
        }
    }

    @Override
    public synchronized ArrayList<String> fetchUserSkills(String userId) throws SQLException {
        ArrayList<String> skills = new ArrayList<>();
        String query = "SELECT skill_name FROM skills " +
                "JOIN user_skills USING(skill_id) WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    skills.add(rs.getString("skill_name"));
                }
                return skills;
            }
        }
    }


    @Override
    public synchronized ArrayList<String> fetchSkills() throws SQLException {
        ArrayList<String> skills = new ArrayList<>();
        String query = "SELECT skill_name FROM skills ORDER BY skill_name";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    skills.add(rs.getString("skill_name"));
                }
                return skills;
            }
        }
    }

    @Override
    public void removeSkill(String userId, String skill) throws SQLException {
        String query = "DELETE FROM user_skills " +
                "WHERE user_id = ? AND skill_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {

            int skillId = fetchSkillId(skill);
            if (skillId == -1) return;

            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            ps.setInt(2, skillId);
            ps.executeUpdate();
        }
    }

    private int fetchSkillId(String skill) throws SQLException {
        String query = "SELECT skill_id FROM skills WHERE skill_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, skill);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("skill_id");
            }
            return -1;
        }
    }
}
