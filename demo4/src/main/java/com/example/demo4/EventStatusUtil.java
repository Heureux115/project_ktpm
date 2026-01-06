package com.example.demo4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventStatusUtil {

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm");

    public static void autoUpdatePastEvents() {
        LocalDate today = LocalDate.now();
        String nowTime = LocalTime.now().format(TIME_FMT); 

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE events " +
                             "SET status = N'ĐÃ DIỄN RA' " +
                             "WHERE status <> N'ĐÃ DIỄN RA' " +
                             "AND (date < ? OR (date = ? AND end_time < ?))"
             )) {

            String todayStr = today.toString(); 

            ps.setString(1, todayStr);
            ps.setString(2, todayStr);
            ps.setString(3, nowTime);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
