package com.ef.util;

import com.ef.domain.BlockedIP;
import com.ef.domain.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gardiary on 03/04/18.
 */
public class DBUtil {
    private static Connection connection = null;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/parser";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);

            if (connection != null) {
                System.out.println("DB connection established.");
            } else {
                System.out.println("Failed to make DB connection!");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("DB Connection Failed!");
            e.printStackTrace();
        }
    }

    public static void closeDBConnection() {
        try {
            if (connection != null) {
                connection.close();

                System.out.println("DB connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int insert(BlockedIP blockedIP) {

        int result = 0;
        String sql = "INSERT INTO blocked_ip (created_date, ip, count, start_date, end_date, threshold, reason) "
                + "VALUES (now(), ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, blockedIP.getIp());
            preparedStatement.setInt(2, blockedIP.getCount());
            preparedStatement.setTimestamp(3, new java.sql.Timestamp(blockedIP.getStartDate().getTime()));
            preparedStatement.setTimestamp(4, new java.sql.Timestamp(blockedIP.getEndDate().getTime()));
            preparedStatement.setInt(5, blockedIP.getThreshold());
            preparedStatement.setString(6, blockedIP.getReason());

            // execute insert SQL stetement
            result = preparedStatement.executeUpdate();

        } catch (Exception e) {

            System.out.println(e.getMessage());

        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    public static int insert(Log log) {

        int result = 0;
        String sql = "INSERT INTO log (date_time, ip, request, status, user_agent, job_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setTimestamp(1, new java.sql.Timestamp(log.getDateTime().getTime()));
            preparedStatement.setString(2, log.getIp());
            preparedStatement.setString(3, log.getRequest());
            preparedStatement.setInt(4, log.getStatus());
            preparedStatement.setString(5, log.getUserAgent());
            preparedStatement.setString(6, log.getJobId());

            // execute insert SQL stetement
            result = preparedStatement.executeUpdate();

        } catch (Exception e) {

            System.out.println(e.getMessage());

        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    public static void insertBatch(List<Log> logs) {

        String sql = "INSERT INTO log (date_time, ip, request, status, user_agent, job_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);

            int i = 0;
            for (Log log : logs) {
                preparedStatement.setTimestamp(1, new java.sql.Timestamp(log.getDateTime().getTime()));
                preparedStatement.setString(2, log.getIp());
                preparedStatement.setString(3, log.getRequest());
                preparedStatement.setInt(4, log.getStatus());
                preparedStatement.setString(5, log.getUserAgent());
                preparedStatement.setString(6, log.getJobId());

                preparedStatement.addBatch();
                i++;

                if (i % 1000 == 0 || i >= logs.size()) {
                    preparedStatement.executeBatch(); // Execute every 1000 items.
                }
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    public static List<Log> searchLogs(String jobId, Date startDate, Date endDate) {

        String sql = "SELECT id, date_time, ip FROM log WHERE job_id = '" + jobId + "' AND (date_time BETWEEN '"
                + DateUtil.format3(startDate) + "' AND '" + DateUtil.format3(endDate) + "')";

        System.out.println("Search SQL : " + sql);

        List<Log> logs = new ArrayList<Log>();

        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            // iterate through the java resultset
            while (rs.next()) {
                Integer id = rs.getInt("id");
                Date dateTime = rs.getDate("date_time");
                String ip = rs.getString("ip");

                Log log = new Log();
                log.setId(id);
                log.setDateTime(dateTime);
                log.setIp(ip);

                logs.add(log);
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;

    }

    public static List<Log> searchLogs(String jobId, String ip) {

        String sql = "SELECT id, date_time, ip FROM log WHERE job_id = '" + jobId + "' AND ip = '" + ip + "'";

        System.out.println("Search SQL : " + sql);

        List<Log> logs = new ArrayList<Log>();

        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            // iterate through the java resultset
            while (rs.next()) {
                Log log = new Log();
                log.setId(rs.getInt("id"));
                log.setDateTime(rs.getDate("date_time"));
                log.setIp(rs.getString("ip"));

                logs.add(log);
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;

    }

    public static List<Log> searchLogs(String ip) {

        String sql = "SELECT id, date_time, ip FROM log WHERE ip = '" + ip + "'";

        System.out.println("Search SQL : " + sql);

        List<Log> logs = new ArrayList<Log>();

        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            // iterate through the java resultset
            while (rs.next()) {
                Log log = new Log();
                log.setId(rs.getInt("id"));
                log.setDateTime(rs.getDate("date_time"));
                log.setIp(rs.getString("ip"));

                logs.add(log);
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;

    }

}
