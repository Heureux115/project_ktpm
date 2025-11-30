package com.example.demo4.dao;

import com.example.demo4.Database;
import com.example.demo4.models.Citizen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitizenDao {

    /**
     * Lấy tất cả công dân trong bảng citizens.
     */
    public static List<Citizen> findAll() throws SQLException {
        List<Citizen> list = new ArrayList<>();
        String sql = "SELECT id, full_name, relation, dob, cccd, job, household_id FROM citizens";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /**
     * Lấy danh sách công dân thuộc một hộ khẩu (household_id).
     */
    public static List<Citizen> findByHousehold(String householdId) throws SQLException {
        List<Citizen> list = new ArrayList<>();
        String sql = "SELECT id, full_name, relation, dob, cccd, job, household_id " +
                "FROM citizens WHERE household_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, householdId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /**
     * Tìm 1 công dân theo id.
     */
    public static Citizen findById(int id) throws SQLException {
        String sql = "SELECT id, full_name, relation, dob, cccd, job, household_id " +
                "FROM citizens WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /**
     * Thêm công dân mới vào một hộ khẩu.
     *  - id để DB tự tăng (AUTO_INCREMENT).
     *  - householdId truyền riêng vì model Citizen không có field này.
     */
    public static void insert(Citizen c, String householdId) throws SQLException {
        String sql = "INSERT INTO citizens(full_name, relation, dob, cccd, job, household_id) " +
                "VALUES(?,?,?,?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getFullName());
            ps.setString(2, c.getRelation());
            ps.setString(3, c.getDob());
            ps.setString(4, c.getCccd());
            ps.setString(5, c.getJob());
            ps.setString(6, householdId);
            ps.executeUpdate();
        }
    }

    /**
     * Cập nhật thông tin công dân (không đổi household_id).
     */
    public static void update(Citizen c) throws SQLException {
        String sql = "UPDATE citizens SET full_name=?, relation=?, dob=?, cccd=?, job=? " +
                "WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getFullName());
            ps.setString(2, c.getRelation());
            ps.setString(3, c.getDob());
            ps.setString(4, c.getCccd());
            ps.setString(5, c.getJob());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Xoá công dân theo id.
     */
    public static void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM citizens WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Map 1 dòng ResultSet -> 1 đối tượng Citizen.
     * (household_id vẫn đọc trong SQL nhưng không đưa vào model).
     */
    private static Citizen mapRow(ResultSet rs) throws SQLException {
        return new Citizen(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("relation"),
                rs.getString("dob"),
                rs.getString("cccd"),
                rs.getString("job")
        );
    }
}
