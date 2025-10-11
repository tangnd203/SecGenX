package vulnerable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

// Lớp 3: Tương tác với CSDL, nơi chứa lỗ hổng
public class UserRepository {
    public Optional<User> queryUser(String username) {
        // ❌ LỖ HỔNG: Nối chuỗi trực tiếp vào câu lệnh SQL
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        System.out.println("[Repository] Đang thực thi câu lệnh SQL không an toàn: " + sql);

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}