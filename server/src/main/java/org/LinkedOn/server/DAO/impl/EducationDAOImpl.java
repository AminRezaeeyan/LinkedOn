package org.LinkedOn.server.DAO.impl;

import org.LinkedOn.server.DAO.EducationDAO;
import org.LinkedOn.server.models.Education;
import org.LinkedOn.server.models.User;
import org.LinkedOn.server.utils.Database;
import org.LinkedOn.server.utils.UUIDUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class EducationDAOImpl implements EducationDAO {
    private static EducationDAOImpl instance;
    private final Connection connection;

    private EducationDAOImpl() {
        connection = Database.connect();
    }

    public static EducationDAOImpl getInstance() {
        if (instance == null) instance = new EducationDAOImpl();
        return instance;
    }

    @Override
    public String addEducation(Education education) throws SQLException {
        String query = "INSERT INTO educations (id, user_id, school, degree, field, grade, start_date, end_date, description, activities) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            String educationId = UUIDUtil.generate();
            ps.setBytes(1, UUIDUtil.UUIDToBytes(educationId));
            ps.setBytes(2, UUIDUtil.UUIDToBytes(education.getUserId()));
            ps.setString(3, education.getInstitute().toString());
            ps.setString(4, education.getDegree().toString());
            ps.setString(5, education.getField());
            ps.setString(6, education.getGrade());
            ps.setDate(7, education.getStartDate());
            ps.setDate(8, education.getEndDate());
            ps.setString(9, education.getDescription());
            ps.setString(10, education.getActivities());
            ps.executeUpdate();
            return educationId;
        }
    }

    @Override
    public Education fetchEducation(String id) throws SQLException {
        String query = "SELECT * FROM educations WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(id));
            try (ResultSet rs = ps.executeQuery()) {
                List<Education> educations = Database.mapResultSetToList(rs, this::mapRowToEducation);
                if (educations.isEmpty()) return null;
                return educations.get(0);
            }
        }
    }

    @Override
    public ArrayList<Education> fetchUserEducations(String userId) throws SQLException {
        String query = "SELECT * FROM educations WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(userId));
            try (ResultSet rs = ps.executeQuery()) {
                List<Education> educations = Database.mapResultSetToList(rs, this::mapRowToEducation);
                return (ArrayList<Education>) educations;
            }
        }
    }

    @Override
    public ArrayList<User> searchInstituteUsers(Education.Institute institute, int limit) throws SQLException {
        String query = "SELECT * FROM users JOIN educations ON users.id = educations.user_id " +
                "WHERE school = ? LIMIT ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, institute.name());
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<User> users = Database.mapResultSetToList(rs, UserDAOImpl.getInstance()::mapRowToUser);
                return (ArrayList<User>) users;
            }
        }
    }

    @Override
    public void updateEducation(Education education) throws SQLException {
        String query = "UPDATE educations SET school = ?, degree = ?, field = ?, grade = ?, start_date = ?, end_date = ?, description = ?, activities = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, education.getInstitute().toString());
            ps.setString(2, education.getDegree().toString());
            ps.setString(3, education.getField());
            ps.setString(4, education.getGrade());
            ps.setDate(5, education.getStartDate());
            ps.setDate(6, education.getEndDate());
            ps.setString(7, education.getDescription());
            ps.setString(8, education.getActivities());
            ps.setBytes(9, UUIDUtil.UUIDToBytes(education.getId()));
            ps.executeUpdate();
        }
    }

    @Override
    public synchronized void removeEducation(String id) throws SQLException {
        String query = "DELETE FROM educations WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBytes(1, UUIDUtil.UUIDToBytes(id));
            ps.executeUpdate();
        }
    }

    protected synchronized Education mapRowToEducation(ResultSet rs) {
        try {
            return new Education(
                    UUIDUtil.bytesToUUID(rs.getBytes("id")),
                    UUIDUtil.bytesToUUID(rs.getBytes("user_id")),
                    rs.getString("field"),
                    rs.getString("grade"),
                    rs.getString("description"),
                    rs.getString("activities"),
                    Education.Degree.valueOf(rs.getString("degree")),
                    Education.Institute.valueOf(rs.getString("school")),
                    rs.getDate("start_date"),
                    rs.getDate("end_date")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Failed to map resultSet to Education object", e);
        }
    }
}
