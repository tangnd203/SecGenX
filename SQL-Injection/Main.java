import vulnerable.UserController;
import fixed.UserControllerFixed;

public class Main {
    public static void main(String[] args) {
        // 1. Khởi tạo CSDL và dữ liệu mẫu
        Database.initialize();

        // 2. Chuẩn bị payload tấn công SQL Injection
        // Payload này được thiết kế để bỏ qua điều kiện WHERE và trả về tất cả các bản ghi
        String maliciousInput = "' OR '1'='1";

        System.out.println("\n=======================================================");
        System.out.println(" KỊCH BẢN 1: CHẠY PHIÊN BẢN CHỨA LỖI (VULNERABLE)");
        System.out.println(" Payload: " + maliciousInput);
        System.out.println("=======================================================");
        UserController vulnerableController = new UserController();
        vulnerableController.findUser(maliciousInput);

        System.out.println("\n======================================================");
        System.out.println(" KỊCH BẢN 2: CHẠY PHIÊN BẢN ĐÃ SỬA LỖI (FIXED)");
        System.out.println(" Payload: " + maliciousInput);
        System.out.println("======================================================");
        UserControllerFixed fixedController = new UserControllerFixed();
        fixedController.findUser(maliciousInput);
    }
}