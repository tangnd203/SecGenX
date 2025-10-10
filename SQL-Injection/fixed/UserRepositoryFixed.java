package fixed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

// Lớp 3: Nơi lỗ hổng được vá bằng PreparedStatement
public class UserRepositoryFixed {
    public Optional<User> queryUser(String username) {
        // ✅ ĐÃ SỬA LỖI: Sử dụng PreparedStatement với tham số hóa (?)
        String sql = "SELECT * FROM users WHERE username = ?";
        System.out.println("[Repository] Đang chuẩn bị câu lệnh SQL an toàn: " + sql);

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gán giá trị vào tham số một cách an toàn
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("role")
                    );
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}