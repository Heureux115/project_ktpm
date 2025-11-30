package com.example.demo4.dao;

import com.example.demo4.Database;
import com.example.demo4.models.Household;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HouseholdDao {

    // Lấy tất cả hộ khẩu
    public static List<Household> findAll() throws SQLException {
        List<Household> list = new ArrayList<>();
        String sql = "SELECT household_id, head_name, address, owner FROM households";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // Lấy các hộ khẩu mà owner = userId (customer)
    public static List<Household> findByOwner(int ownerUserId) throws SQLException {
        List<Household> list = new ArrayList<>();
        String sql = "SELECT household_id, head_name, address, owner FROM households WHERE owner = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ownerUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // Thêm hộ khẩu
    public static void insert(Household h) throws SQLException {
        String sql = "INSERT INTO households (household_id, head_name, address, owner) VALUES (?,?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, h.getHouseholdId());
            ps.setString(2, h.getHeadName());
            ps.setString(3, h.getAddress());
            ps.setString(4, h.getOwner());
            ps.executeUpdate();
        }
    }

    // Cập nhật
    public static void update(Household h) throws SQLException {
        String sql = "UPDATE households SET head_name=?, address=? WHERE household_id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, h.getHeadName());
            ps.setString(2, h.getAddress());
            ps.setString(3, h.getHouseholdId());
            ps.executeUpdate();
        }
    }

    // Xoá
    public static void deleteById(String householdId) throws SQLException {
        String sql = "DELETE FROM households WHERE household_id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, householdId);
            ps.executeUpdate();
        }
    }

    // Map ResultSet -> Household
    private static Household mapRow(ResultSet rs) throws SQLException {
        String id = rs.getString("household_id");
        String head = rs.getString("head_name");
        String address = rs.getString("address");
        String owner = rs.getString("owner");
        return new Household(id, head, address, owner);
    }
}
