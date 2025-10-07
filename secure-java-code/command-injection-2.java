import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SecurePath {
    private static final String BASE_DIR = "/var/data/uploads/";

    public void readFileSafely(String filename) {
        Path base = Paths.get(BASE_DIR).toAbsolutePath().normalize();
        Path requestedPath = Paths.get(BASE_DIR, filename).toAbsolutePath().normalize();
        
        // **Kiểm tra (Sanitization):** Đảm bảo đường dẫn được yêu cầu là con của thư mục gốc.
        if (!requestedPath.startsWith(base)) {
            System.err.println("ERROR: Attempted path traversal detected and blocked.");
            return;
        }

        try {
            // Dòng này Snyk sẽ không báo lỗi
            System.out.println("Reading file safely: " + requestedPath.toString());
            // Giả lập đọc file
            // File content = Files.readAllBytes(requestedPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Thao tác tấn công bị chặn
        new SecurePath().readFileSafely("../../../etc/passwd"); 
        // Thao tác hợp lệ
        new SecurePath().readFileSafely("profile.jpg");
    }
}