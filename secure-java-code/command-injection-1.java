// Tên file: CommandInjectionFixed.java
import java.io.IOException;
import java.util.Arrays;

public class CommandInjectionFixed {
    
    public static void executeCleanupCommandSafe(String filename) {
        
        System.out.println("--- MÃ FIX: Sử dụng ProcessBuilder an toàn ---");
        
        // DÒNG AN TOÀN: Truyền lệnh và tham số dưới dạng MẢNG
        // Mỗi phần tử trong mảng là một tham số riêng biệt.
        ProcessBuilder pb = new ProcessBuilder("find", "/tmp/logs", "-name", filename, "-delete");
        
        System.out.println("Tham số được gửi: " + Arrays.toString(pb.command().toArray()));
        
        try {
            Process process = pb.start();
            
            if (filename.contains(";")) {
                System.out.println("Dữ liệu độc hại được coi là tên file LITERAL.");
                System.out.println("-> Lệnh độc hại KHÔNG được thực thi.");
            } else {
                 System.out.println("Lệnh hợp lệ được thực thi.");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String maliciousInput = "log.txt; whoami";
        executeCleanupCommandSafe(maliciousInput);
    }
}