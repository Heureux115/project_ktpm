package com.example.demo4.dao;

import com.example.demo4.Database;
import com.example.demo4.models.Event;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDao {

    // Lấy các sự kiện từ ngày minDate trở đi
    public static List<Event> findUpcomingFrom(LocalDate minDate) throws SQLException {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT id, title, event_date, start_time, end_time, location, description, status " +
                "FROM events WHERE event_date >= ? ORDER BY event_date, start_time";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, minDate.toString()); // yyyy-MM-dd
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // Lấy sự kiện quá khứ
    public static List<Event> findPastBefore(LocalDate limitDate) throws SQLException {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT id, title, event_date, start_time, end_time, location, description, status " +
                "FROM events WHERE event_date < ? ORDER BY event_date DESC";

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
        String sql = "SELECT id, title, event_date, start_time, end_time, location, description, status FROM events";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // Kiểm tra trùng lịch sự kiện
    public static boolean hasTimeConflict(String date, String start, String end) throws SQLException {
        String sql =
                "SELECT COUNT(*) FROM events " +
                        "WHERE event_date = ? " +
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
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // Thêm mới sự kiện
    public static void insert(Event e) throws SQLException {
        String sql = "INSERT INTO events(title, event_date, start_time, end_time, location, description, status) " +
                "VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getTitle());
            ps.setString(2, e.getDate());       // yyyy-MM-dd
            ps.setString(3, e.getStartTime());
            ps.setString(4, e.getEndTime());
            ps.setString(5, e.getLocation());
            ps.setString(6, e.getDescription());
            ps.setString(7, e.getStatus());
            ps.executeUpdate();
        }
    }

    public static int insertWithCheck(Event e) throws SQLException {

        String checkSql = """
        SELECT COUNT(*) FROM events
        WHERE event_date = ?
          AND ((start_time <= ? AND end_time > ?)
           OR  (start_time < ? AND end_time >= ?))
    """;

        String insertSql = """
        INSERT INTO events(title,event_date,start_time,end_time,location,description,status)
        OUTPUT INSERTED.id
        VALUES (?,?,?,?,?,?,?)
    """;

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            // 1️⃣ Check trùng giờ
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setString(1, e.getDate());
                ps.setString(2, e.getStartTime());
                ps.setString(3, e.getStartTime());
                ps.setString(4, e.getEndTime());
                ps.setString(5, e.getEndTime());

                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Trùng giờ sự kiện");
                }
            }

            // 2️⃣ Insert + lấy ID
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, e.getTitle());
                ps.setString(2, e.getDate());
                ps.setString(3, e.getStartTime());
                ps.setString(4, e.getEndTime());
                ps.setString(5, e.getLocation());
                ps.setString(6, e.getDescription());
                ps.setString(7, e.getStatus());

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int eventId = rs.getInt(1);
                    conn.commit();
                    return eventId;
                }
            }

            conn.rollback();
            throw new SQLException("Không tạo được sự kiện");
        }
    }

    public static Event findById(int eventId) throws Exception {

        String sql = """
    SELECT id, title, event_date, start_time, end_time,
           location, description, status
    FROM events
    WHERE id = ?
""";


        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("event_date"), // 🔥 SỬA Ở ĐÂY
                            rs.getString("start_time"),
                            rs.getString("end_time"),
                            rs.getString("location"),
                            rs.getString("description"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    // Cập nhật sự kiện
    public static void update(Event e) throws SQLException {
        String sql = "UPDATE events SET title=?, event_date=?, start_time=?, end_time=?, " +
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

    // Cập nhật trạng thái sự kiện
    public static void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE events SET status=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // Map ResultSet -> Event
    private static Event mapRow(ResultSet rs) throws SQLException {
        return new Event(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("event_date"),
                rs.getString("start_time"),
                rs.getString("end_time"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getString("status")
        );
    }
}
