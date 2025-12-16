package com.example.demo4.dao;

import com.example.demo4.Database;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDao {

    protected static Connection getConn() throws SQLException {
        return Database.getConnection();
    }

    protected static void setIntOrNull(
            java.sql.PreparedStatement ps, int idx, Integer value)
            throws SQLException {
        if (value != null) ps.setInt(idx, value);
        else ps.setNull(idx, java.sql.Types.INTEGER);
    }
}
