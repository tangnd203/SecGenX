public class SecureSample {
    public static void main(String[] args) {
        // MÃ FIX: Đọc thông tin nhạy cảm từ biến môi trường
        String password = System.getenv("APP_DATABASE_PASSWORD");
        
        if (password == null) {
            // Nên dừng ứng dụng nếu thông tin quan trọng bị thiếu
            System.err.println("ERROR: APP_DATABASE_PASSWORD environment variable not set.");
            return;
        }

        // Không nên in mật khẩu ra console trong ứng dụng thực tế,
        // nhưng đây là cách xử lý dữ liệu đã được lấy an toàn.
        System.out.println("Password successfully loaded (Length: " + password.length() + ")");
    }
}