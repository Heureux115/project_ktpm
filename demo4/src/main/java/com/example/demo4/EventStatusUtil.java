package com.example.demo4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventStatusUtil {

    // end_time của cậu đang lưu dạng "HH:mm" → format giống vậy
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Tự động set status = 'ĐÃ DIỄN RA'
     * cho các sự kiện đã kết thúc (date < hôm nay
     * hoặc date = hôm nay & end_time < giờ hiện tại)
     */
    public static void autoUpdatePastEvents() {
        LocalDate today = LocalDate.now();
        String nowTime = LocalTime.now().format(TIME_FMT); // "HH:mm"

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE events " +
                             "SET status = N'ĐÃ DIỄN RA' " +
                             "WHERE status <> N'ĐÃ DIỄN RA' " +
                             "AND (date < ? OR (date = ? AND end_time < ?))"
             )) {

            String todayStr = today.toString(); // yyyy-MM-dd

            ps.setString(1, todayStr);
            ps.setString(2, todayStr);
            ps.setString(3, nowTime);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

