package com.example.demo4.dao;

import com.example.demo4.Database;
import com.example.demo4.models.Citizen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitizenDao {

    // ⭐ Tìm công dân theo user_id
    public static Citizen findByUserId(int userId) throws Exception {
        String sql = "SELECT * FROM citizens WHERE user_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public static int countByHousehold(int householdId) throws Exception {
        String sql = "SELECT COUNT(*) FROM citizens WHERE household_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, householdId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public static List<Citizen> searchByIdPrefix(String keyword) throws Exception {
        String sql = """
        SELECT * FROM citizens
        WHERE CAST(id AS VARCHAR) LIKE ?
    """;

        List<Citizen> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public static List<Citizen> searchByCccdPrefix(String keyword) throws Exception {
        String sql = """
        SELECT * FROM citizens
        WHERE cccd LIKE ?
    """;

        List<Citizen> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ⭐ Tìm công dân theo CCCD
    public static Citizen findByCccd(String cccd) throws Exception {

        String sql = "SELECT * FROM citizens WHERE cccd = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cccd);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    // INSERT citizen mới
    public static int insert(Citizen citizen) throws Exception {
        String sql = """
            INSERT INTO citizens(full_name, relation, dob, cccd, job, household_id, user_id)
            VALUES (?,?,?,?,?,?,?)
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, citizen.getFullName());
            ps.setString(2, citizen.getRelation());
            ps.setString(3, citizen.getDob());
            ps.setString(4, citizen.getCccd());
            ps.setString(5, citizen.getJob());

            if (citizen.getHouseholdId() != null)
                ps.setInt(6, citizen.getHouseholdId());
            else
                ps.setNull(6, Types.INTEGER);

            if (citizen.getUserId() != null)
                ps.setInt(7, citizen.getUserId());
            else
                ps.setNull(7, Types.INTEGER);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    citizen.setId(id);
                    return id;
                }
            }
        }

        throw new Exception("Không lấy được ID công dân mới!");
    }

    // UPDATE citizen
    public static void update(Citizen c) throws Exception {
        String sql = """
            UPDATE citizens
            SET full_name=?, relation=?, dob=?, cccd=?, job=?, household_id=?, user_id=?
            WHERE id=?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getFullName());
            ps.setString(2, c.getRelation());
            ps.setString(3, c.getDob());
            ps.setString(4, c.getCccd());
            ps.setString(5, c.getJob());

            if (c.getHouseholdId() != null)
                ps.setInt(6, c.getHouseholdId());
            else
                ps.setNull(6, Types.INTEGER);

            if (c.getUserId() != null)
                ps.setInt(7, c.getUserId());
            else
                ps.setNull(7, Types.INTEGER);

            ps.setInt(8, c.getId());

            if (ps.executeUpdate() == 0)
                throw new Exception("Không có bản ghi nào được cập nhật!");
        }
    }

    // Tìm citizen theo ID
    public static Citizen findById(int id) throws Exception {
        String sql = "SELECT * FROM citizens WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    // Lấy tất cả citizen
    public static List<Citizen> findAll() throws Exception {
        String sql = "SELECT * FROM citizens";
        List<Citizen> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        }

        return list;
    }


    // Delete
    public static void deleteById(int id) throws Exception {
        String sql = "DELETE FROM citizens WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void moveCitizenToHousehold(int citizenId, int newHouseholdId)
            throws SQLException {

        String sql = "UPDATE citizens SET household_id = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newHouseholdId);
            ps.setInt(2, citizenId);
            ps.executeUpdate();
        }
    }

    public static void removeCitizenFromHousehold(int citizenId)
            throws SQLException {

        String sql = "UPDATE citizens SET household_id = NULL WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, citizenId);
            ps.executeUpdate();
        }
    }

    public static List<Citizen> findWithoutHousehold() throws SQLException {
        String sql = "SELECT * FROM citizens WHERE household_id IS NULL";
        List<Citizen> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }


    // ⭐ LẤY CÔNG DÂN CHƯA CÓ USER
    public static List<Citizen> findAvailableForAssign() throws Exception {
        String sql = "SELECT * FROM citizens WHERE user_id IS NULL";
        List<Citizen> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));
        }

        return list;
    }

    // ⭐ GÁN USER CHO CITIZEN
    public static void assignUser(int citizenId, int userId) throws Exception {

        // 1️⃣ Kiểm tra user đã được gán chưa
        String checkSql = "SELECT COUNT(*) FROM citizens WHERE user_id = ?";
        String updateSql = "UPDATE citizens SET user_id=? WHERE id=?";

        try (Connection conn = Database.getConnection()) {

            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setInt(1, userId);
                ResultSet rs = check.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new Exception("Tài khoản này đã được gán cho công dân khác!");
                }
            }

            // 2️⃣ Gán nếu hợp lệ
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, citizenId);
                ps.executeUpdate();
            }
        }
    }


    // Helper
    private static Citizen mapRow(ResultSet rs) throws SQLException {
        return new Citizen(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("relation"),
                rs.getString("dob"),
                rs.getString("cccd"),
                rs.getString("job"),
                (Integer) rs.getObject("household_id"),
                (Integer) rs.getObject("user_id")
        );
    }

    // Lấy công dân theo hộ khẩu
    public static List<Citizen> findByHousehold(int householdId) throws Exception {
        String sql = """
        SELECT id, full_name, relation, dob, cccd, job, household_id, user_id
        FROM citizens
        WHERE household_id = ?
    """;

        List<Citizen> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, householdId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Citizen(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("relation"),
                        rs.getString("dob"),
                        rs.getString("cccd"),
                        rs.getString("job"),
                        (Integer) rs.getObject("household_id"),
                        (Integer) rs.getObject("user_id")
                ));
            }
        }

        return list;
    }
}
