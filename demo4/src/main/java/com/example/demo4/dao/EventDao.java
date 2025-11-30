package com.example.demo4.dao;

import com.example.demo4.Database;
import com.example.demo4.models.Event;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDao {

    // Lấy các sự kiện từ ngày minDate trở đi (sắp xếp theo ngày + giờ bắt đầu)
    public static List<Event> findUpcomingFrom(LocalDate minDate) throws SQLException {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT id, title, date, start_time, end_time, location, description, status " +
                "FROM events WHERE date >= ? ORDER BY date, start_time";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, minDate.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // Lấy các sự kiện trước ngày limitDate (quá khứ)
    public static List<Event> findPastBefore(LocalDate limitDate) throws SQLException {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT id, title, date, start_time, end_time, location, description, status " +
                "FROM events WHERE date < ? ORDER BY date DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, limitDate.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // Lấy tất cả sự kiện
    public static List<Event> findAll() throws SQLException {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT id, title, date, start_time, end_time, location, description, status FROM events";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // Kiểm tra xem trong ngày đó đã có sự kiện trùng khoảng thời gian chưa
    public static boolean hasTimeConflict(String date, String start, String end) throws SQLException {
        String sql =
                "SELECT COUNT(*) FROM events " +
                        "WHERE date = ? " +
                        "AND ((start_time <= ? AND end_time > ?) " +
                        "   OR (start_time < ? AND end_time >= ?))";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, date);
            ps.setString(2, start);
            ps.setString(3, start);
            ps.setString(4, end);
            ps.setString(5, end);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }


    // Thêm sự kiện mới
    public static void insert(Event e) throws SQLException {
        String sql = "INSERT INTO events(title, date, start_time, end_time, location, description, status) " +
                "VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getTitle());
            ps.setString(2, e.getDate());
            ps.setString(3, e.getStartTime());
            ps.setString(4, e.getEndTime());
            ps.setString(5, e.getLocation());
            ps.setString(6, e.getDescription());
            ps.setString(7, e.getStatus());
            ps.executeUpdate();
        }
    }

    // Cập nhật toàn bộ thông tin sự kiện
    public static void update(Event e) throws SQLException {
        String sql = "UPDATE events SET title=?, date=?, start_time=?, end_time=?, " +
                "location=?, description=?, status=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getTitle());
            ps.setString(2, e.getDate());
            ps.setString(3, e.getStartTime());
            ps.setString(4, e.getEndTime());
            ps.setString(5, e.getLocation());
            ps.setString(6, e.getDescription());
            ps.setString(7, e.getStatus());
            ps.setInt(8, e.getId());
            ps.executeUpdate();
        }
    }

    // Chỉ cập nhật trạng thái sự kiện
    public static void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE events SET status=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // Map 1 dòng ResultSet -> 1 đối tượng Event
    private static Event mapRow(ResultSet rs) throws SQLException {
        return new Event(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("date"),
                rs.getString("start_time"),
                rs.getString("end_time"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getString("status")
        );
    }
}
