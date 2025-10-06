// Tên file: SQLInjectionFixed.java
public class SQLInjectionFixed {
    
    // Hàm giả lập kết nối và truy vấn DB an toàn
    public static void checkUserSafe(String username) {
        
        // DÒNG AN TOÀN: Sử dụng placeholder (?)
        String query = "SELECT * FROM users WHERE username = ? AND active = 1";
        
        // Trong môi trường thực, tham số sẽ được gán bằng PreparedStatement.setString(1, username);
        // Biến username sẽ được mã hóa và tìm kiếm là một chuỗi LITERAL.
        
        System.out.println("--- MÃ FIX: PreparedStatement ---");
        System.out.println("SQL gốc (với placeholder): " + query);
        
        if (username.contains("' OR '1'='1")) {
             System.out.println("Đầu vào: " + username);
             System.out.println("JDBC sẽ tìm kiếm một TÊN NGƯỜI DÙNG có đầy đủ chuỗi này.");
             System.out.println("-> Truy vấn KHÔNG thành công (Tên người dùng không tồn tại). SQLi đã bị ngăn chặn.");
        } else {
             System.out.println("Truy vấn an toàn.");
        }
    }

    public static void main(String[] args) {
        String userInputMalicious = "admin' OR '1'='1";
        checkUserSafe(userInputMalicious);
    }
}