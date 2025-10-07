// Tên file: CommandInjectionVulnerable.java
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
// Giả định đây là một method trong Controller nhận đầu vào từ request
public class CommandInjectionVulnerable {
    
    public static void executeCleanupCommand(String filename) {
        // user input: file.log; rm -rf /etc
        
        // --- BƯỚC 1: Source (Đầu vào không an toàn) là filename ---
        
        // DÒNG NGUY HIỂM: Nối chuỗi trực tiếp để tạo lệnh
        String command = "find /tmp/logs -name " + filename + " -delete";
        
        System.out.println("--- MÃ LỖI: Command Injection ---");
        System.out.println("Lệnh được thực thi: " + command);
        
        try {
            // --- BƯỚC 2: Sink (Thực thi lệnh nguy hiểm) ---
            Process process = Runtime.getRuntime().exec(command);
            
            // Nếu input chứa ';', lệnh độc hại sẽ được thực thi trên hệ điều hành.
            System.err.println("!!! LỖI: Lệnh độc hại có thể đã được thực thi trên hệ thống.");
            
            // Đọc output để minh họa
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Giả lập output của lệnh
                // System.out.println(line); 
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // user input độc hại
        String maliciousInput = "log.txt; whoami";
        executeCleanupCommand(maliciousInput);
    }
}