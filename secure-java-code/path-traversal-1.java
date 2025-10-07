// Tên file: PathTraversalFixed.java
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class PathTraversalFixed {
    
    private static final String FILE_STORAGE = "/app/data/user_uploads/"; 

    public static void downloadFileSafe(String requestedFilename) {
        
        try {
            Path baseDirPath = Paths.get(FILE_STORAGE).toAbsolutePath().normalize();
            
            // Bước 1: Tạo đường dẫn file dựa trên đầu vào người dùng
            Path userPath = Paths.get(FILE_STORAGE, requestedFilename);
            
            // Bước 2: Chuẩn hóa đường dẫn để xử lý ../ và .
            Path normalizedUserPath = userPath.toAbsolutePath().normalize();

            // Bước 3: KIỂM TRA QUAN TRỌNG: Đảm bảo đường dẫn vẫn nằm trong thư mục gốc.
            if (!normalizedUserPath.startsWith(baseDirPath)) {
                System.err.println("!!! BẢO MẬT: Phát hiện truy cập ngoài thư mục gốc! Hành động bị từ chối.");
                return;
            }

            // DÒNG AN TOÀN: Chỉ đọc file sau khi xác minh.
            System.out.println("--- MÃ FIX: Chuẩn hóa và Kiểm tra Thư mục Gốc ---");
            System.out.println("Đường dẫn an toàn được truy cập: " + normalizedUserPath);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String maliciousInput = "../../../etc/passwd";
        downloadFileSafe(maliciousInput);
    }
}