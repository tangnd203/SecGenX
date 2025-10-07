import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MÃ LỖI: SQL Injection (CWE-89). 
 * Snyk Code sẽ báo cáo lỗi nghiêm trọng vì biến 'username' và 'password' 
 * được nối trực tiếp vào chuỗi truy vấn SQL.
 */
public class VulnerableSQLInjection {

    // Giả định Connection đã được thiết lập. 
    // Trong môi trường thực, hàm này sẽ thực thi truy vấn.
    public String createLoginQuery(String username, String password) {
        // Lỗ hổng: Nối chuỗi input của người dùng trực tiếp vào SQL
        String query = "SELECT userId FROM user_data WHERE user_name = '" 
                     + username + "' AND password = '" + password + "'";
        
        return query; 
    }
    
    // Phương thức main() để chạy thử nghiệm (test) logic lỗ hổng
    public static void main(String[] args) {
        VulnerableSQLInjection app = new VulnerableSQLInjection();

        // --- 1. Query Tấn Công (Payload gây lỗi) ---
        // Kẻ tấn công nhập: ' OR '1'='1
        String maliciousUser = "admin' OR '1'='1"; 
        String maliciousPass = "anything"; 
        String maliciousQuery = app.createLoginQuery(maliciousUser, maliciousPass);

        System.out.println("--- MÃ LỖI: CWE-89 SQL Injection ---");
        System.out.println("INPUT (Username): " + maliciousUser);
        System.out.println("INPUT (Password): " + maliciousPass);
        System.out.println("QUERY BỊ TẠO RA (Vulnerable):");
        System.out.println(maliciousQuery + "\n");
        // Kết quả: "SELECT userId FROM user_data WHERE user_name = 'admin' OR '1'='1' AND password = 'anything'"
        // Lệnh ' OR '1'='1' khiến mệnh đề WHERE luôn đúng (1=1), bỏ qua việc kiểm tra mật khẩu.

        // --- 2. Query Bình Thường ---
        String normalUser = "guest";
        String normalPass = "guestpass";
        String normalQuery = app.createLoginQuery(normalUser, normalPass);
        System.out.println("QUERY BÌNH THƯỜNG:");
        System.out.println(normalQuery);
    }
}