import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

// Đặt class User ở đây để file có thể tự chạy độc lập
class SafeUser { // Đổi tên để tránh trùng lặp nếu 2 file trong cùng package
    private int id;
    private String username;
    private String role;
    public SafeUser(int id, String username, String role) { this.id = id; this.username = username; this.role = role; }
    @Override public String toString() { return "User{id=" + id + ", username='" + username + "', role='" + role + "'}"; }
}

public class SafeOrderBy {

    private final JdbcTemplate jdbcTemplate;
    
    // Whitelist các cột được phép sắp xếp
    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of("id", "username", "role");

    public SafeOrderBy(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    /**
     * ✅ PHIÊN BẢN AN TOÀN ✅
     * Kiểm tra `columnName` dựa trên một danh sách các giá trị hợp lệ (whitelist)
     * trước khi xây dựng câu lệnh SQL.
     */
    public List<SafeUser> getUsersSortedSafe(String columnName) {
        // FIX: Kiểm tra đầu vào với danh sách trắng (whitelist).
        if (!ALLOWED_SORT_COLUMNS.contains(columnName.toLowerCase())) {
            // Nếu cột không hợp lệ, ném ra ngoại lệ để từ chối yêu cầu.
            throw new IllegalArgumentException("Invalid sort column specified: " + columnName);
        }

        // Chỉ sau khi xác thực, chúng ta mới xây dựng câu lệnh SQL.
        String sql = "SELECT * FROM users ORDER BY " + columnName;
        System.out.println("Executing safe query: " + sql);

        RowMapper<SafeUser> rowMapper = (rs, rowNum) -> new SafeUser(
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
        
        SafeOrderBy example = new SafeOrderBy(dataSource);
        
        // Cùng một payload độc hại
        String maliciousColumn = "id; SELECT 1/0";

        System.out.println("## 1. Thử nghiệm phiên bản an toàn với đầu vào hợp lệ ##");
        try {
            List<SafeUser> users = example.getUsersSortedSafe("username");
            System.out.println("Truy vấn thành công với cột hợp lệ. Kết quả: " + users.size() + " users.");
        } catch (Exception e) {
            System.err.println("Thất bại: " + e.getMessage());
        }

        System.out.println("\n-------------------------------------------------\n");

        System.out.println("## 2. Thử nghiệm phiên bản an toàn với payload độc hại ##");
        try {
            example.getUsersSortedSafe(maliciousColumn);
        } catch (IllegalArgumentException e) {
            System.out.println("PHÒNG THỦ THÀNH CÔNG: Phiên bản an toàn đã chặn payload độc hại với lỗi: '" + e.getMessage() + "'");
        }
    }
}