// Tên file: SQLInjectionVulnerable.java
public class SQLInjectionVulnerable {
    
    // Hàm giả lập kết nối và truy vấn DB
    public static void checkUser(String username) {
        // user input: admin' OR '1'='1 
        // Mục đích: Bỏ qua kiểm tra mật khẩu
        
        // Dòng NGUY HIỂM: Nối chuỗi trực tiếp
        String query = "SELECT * FROM users WHERE username = '" + username + "' AND active = 1";
        
        System.out.println("--- MÃ LỖI: Statement ---");
        System.out.println("SQL được thực thi: " + query);
        
        // Giả lập thực thi
        if (query.contains("OR 1=1")) {
            System.err.println("!!! PHÁT HIỆN SQLi: Điều kiện luôn đúng (OR 1=1) đã được chèn. !!!");
            System.out.println("Kẻ tấn công đăng nhập thành công.");
        } else {
             System.out.println("Truy vấn hợp lệ.");
        }
    }

    public static void main(String[] args) {
        String userInputMalicious = "admin' OR '1'='1";
        checkUser(userInputMalicious);
    }
}