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
        String sql =
                "SELECT household_id, head_citizen_id, address, owner_user_id " +
                        "FROM households";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // Lấy các hộ khẩu mà owner_user_id = userId
    public static List<Household> findByOwner(int ownerUserId) throws SQLException {
        List<Household> list = new ArrayList<>();
        String sql =
                "SELECT household_id, head_citizen_id, address, owner_user_id " +
                        "FROM households WHERE owner_user_id = ?";

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

    // Thêm hộ khẩu (auto IDENTITY)
    public static void insert(Household h) throws SQLException {
        String sql =
                "INSERT INTO households (head_citizen_id, address, owner_user_id) " +
                        "VALUES (?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (h.getHeadCitizenId() != null)
                ps.setInt(1, h.getHeadCitizenId());
            else
                ps.setNull(1, Types.INTEGER);

            ps.setString(2, h.getAddress());

            if (h.getOwnerUserId() != null)
                ps.setInt(3, h.getOwnerUserId());
            else
                ps.setNull(3, Types.INTEGER);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    h.setHouseholdId(rs.getInt(1));
                }
            }
        }
    }

    // Cập nhật hộ khẩu
    public static void update(Household h) throws SQLException {
        String sql =
                "UPDATE households " +
                        "SET head_citizen_id=?, address=?, owner_user_id=? " +
                        "WHERE household_id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (h.getHeadCitizenId() != null)
                ps.setInt(1, h.getHeadCitizenId());
            else
                ps.setNull(1, Types.INTEGER);

            ps.setString(2, h.getAddress());

            if (h.getOwnerUserId() != null)
                ps.setInt(3, h.getOwnerUserId());
            else
                ps.setNull(3, Types.INTEGER);

            ps.setInt(4, h.getHouseholdId());

            ps.executeUpdate();
        }
    }

    // Xoá hộ khẩu
    // Xoá hộ khẩu an toàn
    public static void deleteById(int householdId) throws SQLException {
        String sqlClearCitizens =
                "UPDATE citizens SET household_id = NULL WHERE household_id = ?";
        String sqlDeleteHousehold =
                "DELETE FROM households WHERE household_id = ?";

        try (Connection conn = Database.getConnection()) {

            // Bật transaction
            conn.setAutoCommit(false);

            // 1) Gỡ liên kết công dân
            try (PreparedStatement ps1 = conn.prepareStatement(sqlClearCitizens)) {
                ps1.setInt(1, householdId);
                ps1.executeUpdate();
            }

            // 2) Xóa household
            try (PreparedStatement ps2 = conn.prepareStatement(sqlDeleteHousehold)) {
                ps2.setInt(1, householdId);
                ps2.executeUpdate();
            }

            // Commit
            conn.commit();
        }
    }

    // Map ResultSet -> Household
    private static Household mapRow(ResultSet rs) throws SQLException {
        return new Household(
                rs.getInt("household_id"),
                (Integer) rs.getObject("head_citizen_id"),
                rs.getString("address"),
                (Integer) rs.getObject("owner_user_id")
        );
    }

}
