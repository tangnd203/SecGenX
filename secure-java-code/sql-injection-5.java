import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MÃ FIX: Sử dụng PreparedStatement (CWE-89 được vá).
 * Snyk Code sẽ không báo cáo lỗi này.
 */
public class FixedSQLInjection {

    // Hàm an toàn tạo truy vấn (cần có đối tượng Connection thực tế)
    public String createLoginQuerySafe(Connection connection, String username, String password) throws SQLException {
        // MÃ FIX: Sử dụng dấu ? (placeholder)
        String query = "SELECT userId FROM user_data WHERE user_name = ? AND password = ?";
        
        // Trong môi trường thực, bạn sẽ chạy đoạn code sau:
        /*
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username); // Driver tự động escape chuỗi '
            preparedStatement.setString(2, password);
            
            // ResultSet resultSet = preparedStatement.executeQuery();
            // ...
        }
        */
        
        return "SAFE QUERY: Using PreparedStatement with placeholders: " + query;
    }
    
    // Phương thức main() để chạy thử nghiệm mã fix
    public static void main(String[] args) {
        FixedSQLInjection app = new FixedSQLInjection();

        // Thử nghiệm tấn công với mã fix (Giả định Connection null)
        String maliciousUser = "admin' OR '1'='1"; 
        String maliciousPass = "anything"; 
        
        System.out.println("--- MÃ FIX: Sử dụng PreparedStatement ---");
        System.out.println("INPUT (Username): " + maliciousUser);
        System.out.println("INPUT (Password): " + maliciousPass);
        
        try {
             // Kết quả trả về cho mục đích mô phỏng
            String safeQuery = app.createLoginQuerySafe(null, maliciousUser, maliciousPass); 
            System.out.println("QUERY ĐƯỢC TẠO RA (Safe):");
            System.out.println(safeQuery + "\n");
        } catch (SQLException e) {
            // Xử lý ngoại lệ
        }
        
        // Khi chạy thực tế, input ' OR '1'='1 sẽ được truyền vào DB dưới dạng một giá trị chuỗi đơn lẻ, 
        // không thể làm thay đổi cấu trúc lệnh SQL, từ đó lỗ hổng đã được vá.
    }
}