package com.example.demo4.dao;

import com.example.demo4.Database;
import com.example.demo4.models.assets;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssetDao {

    public static List<assets> findAll() throws SQLException {
        List<assets> list = new ArrayList<>();
        String sql = "SELECT id, name, type, quantity, status FROM assets";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public static void insert(assets a) throws SQLException {
        String sql = "INSERT INTO assets(name, type, quantity, status) VALUES(?,?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getType());
            ps.setInt(3, a.getQuantity());
            ps.setString(4, a.getStatus());
            ps.executeUpdate();
        }
    }

    public static void update(assets a) throws SQLException {
        String sql = "UPDATE assets SET name=?, type=?, quantity=?, status=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getType());
            ps.setInt(3, a.getQuantity());
            ps.setString(4, a.getStatus());
            ps.setInt(5, a.getId());
            ps.executeUpdate();
        }
    }

    public static void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM assets WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private static assets mapRow(ResultSet rs) throws SQLException {
        return new assets(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("type"),
                rs.getInt("quantity"),
                rs.getString("status")
        );
    }
}
