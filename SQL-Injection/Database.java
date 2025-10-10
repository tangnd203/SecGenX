import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Lớp tiện ích để thiết lập CSDL H2 in-memory
public class Database {
    private static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void initialize() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            System.out.println("Khởi tạo cơ sở dữ liệu...");
            stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, username VARCHAR(255), role VARCHAR(255))");
            stmt.execute("INSERT INTO users VALUES (1, 'admin', 'administrator')");
            stmt.execute("INSERT INTO users VALUES (2, 'alice', 'user')");
            System.out.println("Cơ sở dữ liệu đã sẵn sàng.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}