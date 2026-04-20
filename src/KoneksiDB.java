import java.sql.*;

public class KoneksiDB {
    private static Connection connection;
    
    private static final String URL = "jdbc:mysql://localhost:3306/db_siakad";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Koneksi database berhasil!");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver JDBC tidak ditemukan!");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Koneksi database gagal!");
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Koneksi database ditutup.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}