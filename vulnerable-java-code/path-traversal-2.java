import java.io.File;
import java.io.IOException;

/**
 * MÃ LỖI: Path Traversal / File Inclusion (CWE-22). 
 * Snyk Code sẽ báo cáo lỗi nghiêm trọng vì biến 'fileName' 
 * được sử dụng trực tiếp để tạo đối tượng File.
 */
public class VulnerablePathTraversal {

    private static final String BASE_DIRECTORY = "/home/appuser/uploads/profiles/";

    // Mã lỗi: Phương thức chứa lỗ hổng Path Traversal
    public String loadProfilePicture(String fileName) {
        // Lỗ hổng: Nối chuỗi input trực tiếp
        String filePath = BASE_DIRECTORY + fileName;
        
        File file = new File(filePath);
        
        // Trong môi trường thực, code sẽ đọc nội dung của 'file'
        // Ở đây, ta chỉ trả về đường dẫn để mô phỏng.
        return "Attempting to access file: " + file.getAbsolutePath();
    }

    // Phương thức main() để chạy thử nghiệm và chứng minh lỗ hổng
    public static void main(String[] args) {
        VulnerablePathTraversal app = new VulnerablePathTraversal();

        // 1. Thử nghiệm bình thường
        String normalFile = "my_avatar.png";
        System.out.println("--- 1. Input Bình Thường ---");
        System.out.println(app.loadProfilePicture(normalFile) + "\n");
        // Kết quả mong đợi: /home/appuser/uploads/profiles/my_avatar.png

        // 2. Thử nghiệm tấn công Path Traversal
        // Kẻ tấn công sử dụng: ../../../etc/passwd (hoặc ../../../Windows/win.ini trên Windows)
        // Mục tiêu: Thoát khỏi BASE_DIRECTORY để truy cập tệp nhạy cảm của hệ thống
        String maliciousFile = "../../../../etc/passwd"; 
        String maliciousAccess = app.loadProfilePicture(maliciousFile);

        System.out.println("--- 2. Query Tấn Công (CWE-22) ---");
        System.out.println("INPUT (Malicious): " + maliciousFile);
        System.out.println("ATTEMPTED PATH: " + maliciousAccess);
        // Kết quả bị tấn công: /home/appuser/uploads/profiles/../../../../etc/passwd
        // Khi hệ thống giải quyết đường dẫn, nó sẽ truy cập /etc/passwd
    }
}