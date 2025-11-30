package com.example.demo4.dao;

import com.example.demo4.Database;
import com.example.demo4.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Tìm user theo username + password (login)
    public static User findByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, role, fullname, email " +
                "FROM users WHERE username=? AND password=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    // Tìm user theo username (dùng để check trùng khi register)
    public static User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password, role, fullname, email " +
                "FROM users WHERE username=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    // Lấy tất cả user (cho admin nếu sau này cần)
    public static List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, password, role, fullname, email FROM users";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // Thêm user (dùng trong Register)
    public static void insert(User u) throws SQLException {
        String sql = "INSERT INTO users(username, password, role, fullname, email) " +
                "VALUES(?,?,?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.setString(4, u.getFullname());
            ps.setString(5, u.getEmail());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                u.setId(keys.getInt(1));
            }
        }
    }

    // Cập nhật user (nếu muốn sửa thông tin sau này)
    public static void update(User u) throws SQLException {
        String sql = "UPDATE users SET username=?, password=?, role=?, fullname=?, email=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.setString(4, u.getFullname());
            ps.setString(5, u.getEmail());
            ps.setInt(6, u.getId());
            ps.executeUpdate();
        }
    }

    // Xoá user
    public static void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Map 1 dòng ResultSet -> User
    private static User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("fullname"),
                rs.getString("email")
        );
    }
}
