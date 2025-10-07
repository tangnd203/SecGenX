import java.io.File;
import java.io.IOException;

public class InsecurePath {
    public void readFile(String filename) {
        // Tainted data: filename đến từ người dùng
        File file = new File("/var/data/uploads/" + filename);
        
        try {
            // Dòng này Snyk sẽ báo lỗi Path Traversal
            System.out.println("Attempting to read: " + file.getCanonicalPath());
            // Giả lập đọc file
            // File content = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Đây là nơi tấn công: dùng .. để đi lên thư mục cha
        new InsecurePath().readFile("../../../etc/passwd"); 
    }
}