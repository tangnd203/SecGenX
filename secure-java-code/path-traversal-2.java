import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * MÃ FIX: Sử dụng Path Canonicalization và Path.startsWith() (CWE-22 được vá).
 * Snyk Code sẽ không báo cáo lỗi này.
 */
public class FixedPathTraversal {

    // Đường dẫn gốc AN TOÀN và đã được chuẩn hóa (Canonicalized)
    private static final String BASE_PATH_STRING = "/home/appuser/uploads/profiles/";

    // Mã fix: Sử dụng canonical path và kiểm tra đường dẫn an toàn
    public String loadProfilePictureSafe(String fileName) throws IOException {
        
        // 1. Chuẩn hóa đường dẫn gốc (Root Path)
        Path basePath = Paths.get(BASE_PATH_STRING).toAbsolutePath().normalize();
        
        // 2. Tạo đường dẫn file DỰ ĐỊNH và chuẩn hóa nó
        // Sử dụng .resolve() để kết hợp đường dẫn an toàn hơn so với nối chuỗi
        Path resolvedPath = basePath.resolve(fileName).toAbsolutePath().normalize();

        // 3. KIỂM TRA LỖ HỔNG (Mã fix): 
        // Đảm bảo rằng đường dẫn resolvedPath vẫn bắt đầu bằng basePath.
        if (!resolvedPath.startsWith(basePath)) {
            // Nếu không, có nghĩa là kẻ tấn công đã dùng ../ để thoát ra
            return "ERROR: Path traversal detected and blocked! (File: " + resolvedPath + ")";
        }
        
        // Nếu kiểm tra thành công, đường dẫn là an toàn
        // (Trong môi trường thực, bạn sẽ mở file tại resolvedPath)
        return "Accessing SAFE file: " + resolvedPath;
    }

    // Phương thức main() để chạy thử nghiệm mã fix
    public static void main(String[] args) {
        FixedPathTraversal app = new FixedPathTraversal();
        
        // Thử nghiệm tấn công Path Traversal
        String maliciousFile = "../../../../etc/passwd"; 

        System.out.println("--- MÃ FIX: Sử dụng Path Canonicalization ---");
        System.out.println("INPUT (Malicious): " + maliciousFile);
        
        try {
            String safeAccess = app.loadProfilePictureSafe(maliciousFile);
            System.out.println("KẾT QUẢ SAU KHI FIX:");
            System.out.println(safeAccess);
            // Kết quả: Path traversal detected and blocked!
        } catch (IOException e) {
            System.err.println("An error occurred during path resolution: " + e.getMessage());
        }
    }
}