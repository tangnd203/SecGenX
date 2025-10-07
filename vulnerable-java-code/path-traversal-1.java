// Tên file: PathTraversalVulnerable.java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PathTraversalVulnerable {
    
    private static final String FILE_STORAGE = "/app/data/user_uploads/"; // Thư mục gốc

    public static void downloadFile(String requestedFilename) {
        // user input: ../../../etc/passwd 
        
        // --- BƯỚC 1: Source (Đầu vào không an toàn) là requestedFilename ---
        
        // DÒNG NGUY HIỂM: Nối chuỗi trực tiếp
        File file = new File(FILE_STORAGE + requestedFilename);
        
        System.out.println("--- MÃ LỖI: Path Traversal ---");
        System.out.println("Đường dẫn được tạo ra: " + file.getPath());
        
        // --- BƯỚC 2: Sink (Thao tác file nguy hiểm) ---
        try (FileInputStream fis = new FileInputStream(file)) {
            // Giả lập logic đọc file
            System.err.println("!!! LỖI: File nhạy cảm ngoài thư mục gốc có thể đã được truy cập.");
        } catch (FileNotFoundException e) {
            // Bỏ qua lỗi file không tồn tại
             System.out.println("Truy cập file an toàn hoặc file không tồn tại.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String maliciousInput = "../../../etc/passwd";
        downloadFile(maliciousInput);
    }
}