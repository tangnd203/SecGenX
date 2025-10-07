import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MÃ FIX: Sử dụng ProcessBuilder và Whitelisting để ngăn chặn Command Injection.
 * Snyk Code sẽ không báo cáo lỗi này.
 */
@RestController
public class FixedCommandInjection {
    
    // Whitelisting: Chỉ cho phép một tập hợp lệnh an toàn
    private static final List<String> SAFE_COMMANDS = Arrays.asList("ping", "echo", "cat_log");

    // Mã fix: Sử dụng ProcessBuilder và kiểm tra lệnh có trong danh sách an toàn
    @GetMapping("/api/run-command-safe")
    public String executeCommandSafe(@RequestParam("cmd") String fullCommand) {
        
        // 1. Phân tích lệnh (chỉ lấy lệnh chính, không lấy tham số)
        String[] parts = fullCommand.split("\\s+", 2);
        String baseCommand = parts[0];
        
        // 2. KIỂM TRA LỖ HỔNG (Mã fix): Chỉ cho phép các lệnh trong danh sách trắng
        if (!SAFE_COMMANDS.contains(baseCommand)) {
            return "ERROR: Command '" + baseCommand + "' is not on the whitelist and has been blocked.";
        }
        
        // 3. Sử dụng ProcessBuilder và phân tách lệnh/tham số
        try {
            // Tách lệnh và tham số thành danh sách riêng biệt
            List<String> commandList = Arrays.asList(parts);
            
            // ProcessBuilder an toàn hơn vì nó không gọi shell (sh/cmd) để phân tích cú pháp chuỗi
            ProcessBuilder pb = new ProcessBuilder(commandList); 
            
            Process process = pb.start();
            
            return "Safe command execution initiated for: " + baseCommand;
        } catch (IOException e) {
            return "Error executing command: " + e.getMessage();
        }
    }
    
    // *** Thử nghiệm an toàn ***
    // Input độc hại: ?cmd=ping%20127.0.0.1%20%26%20ls%20/
    // Mã fix sẽ chỉ cho phép 'ping' chạy (hoặc chặn hoàn toàn nếu nó phân tích không đúng), 
    // nhưng quan trọng là nó không cho phép ký tự '&' chạy lệnh thứ hai.
}