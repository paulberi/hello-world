package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root"; //TODO skriv ditt eget lösenord här
    private static final String URL = "jdbc:mysql://localhost:3306/spotify_2?serverTimezone=UTC";

    private ConnectionFactory() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error " + e);
        }
    }

    public static Connection getConnection(String url, String username, String password) {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error " + e);
        }
    }

}
