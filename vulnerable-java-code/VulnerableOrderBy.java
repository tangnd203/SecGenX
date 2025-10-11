import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.List;

// Giả định User là một POJO đơn giản
class User {
    private int id;
    private String username;
    private String role;
    public User(int id, String username, String role) { this.id = id; this.username = username; this.role = role; }
    @Override public String toString() { return "User{id=" + id + ", username='" + username + "', role='" + role + "'}"; }
}

public class VulnerableOrderBy {

    private final JdbcTemplate jdbcTemplate;

    public VulnerableOrderBy(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * ❌ PHIÊN BẢN LỖ HỔNG ❌
     * Sắp xếp người dùng dựa trên tên cột được cung cấp.
     * Dữ liệu `columnName` được nối trực tiếp vào câu lệnh SQL, tạo ra lỗ hổng.
     */
    public List<User> getUsersSortedVulnerable(String columnName) {
        // LỖ HỔNG: columnName được nối chuỗi trực tiếp mà không qua kiểm tra.
        String sql = "SELECT * FROM users ORDER BY " + columnName;

        System.out.println("Executing vulnerable query: " + sql);

        RowMapper<User> rowMapper = (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("role")
        );
        return jdbcTemplate.query(sql, rowMapper);
    }

    public static void main(String[] args) {
        // --- Setup ---
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .build();
        
        VulnerableOrderBy example = new VulnerableOrderBy(dataSource);

        // --- Kịch bản tấn công ---
        // Payload này có thể gây ra lỗi cú pháp hoặc thực thi một hàm của CSDL.
        // Ví dụ: `id; SELECT 1/0` sẽ gây ra lỗi chia cho 0 trên H2.
        String maliciousColumn = "id; SELECT 1/0";

        System.out.println("## Thử nghiệm phiên bản chứa lỗ hổng với payload độc hại ##");
        try {
            System.out.println("Bắt đầu truy vấn...");
            List<User> users = example.getUsersSortedVulnerable(maliciousColumn);
            System.out.println("Kết quả: " + users);
        } catch (Exception e) {
            System.err.println("TẤN CÔNG THÀNH CÔNG: Lệnh SQL độc hại đã được thực thi và gây ra lỗi: " + e.getMessage());
        }
    }
}