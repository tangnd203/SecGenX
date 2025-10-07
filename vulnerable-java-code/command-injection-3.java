import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MÃ LỖI: Command Injection (CWE-77)
 * Snyk Code sẽ phát hiện: Dữ liệu từ @RequestParam (source) đi đến Runtime.exec() (sink).
 * Mức độ: High/Critical.
 */
@RestController // Annotation quan trọng để Snyk xác định đây là điểm truy cập web
public class VulnerableCommandInjection {

    // Lỗ hổng: Hàm nhận input và thực thi lệnh hệ thống
    @GetMapping("/api/run-command")
    public String executeCommandVulnerable(@RequestParam("cmd") String command) {
        
        // --- Vùng Snyk Code sẽ báo lỗi ---
        // Lỗ hổng: Input 'command' (từ web request, không đáng tin cậy) được truyền trực tiếp
        // vào Runtime.exec() mà không qua bất kỳ lớp bảo vệ nào.
        try {
            Process process = Runtime.getRuntime().exec(command);
            
            // Mã giả lập việc đọc output, chỉ để hoàn chỉnh logic
            // (Thường lệnh tấn công sẽ là: `& rm -rf /`)
            return "Command execution initiated for: " + command;
            
        } catch (IOException e) {
            return "Error executing command: " + e.getMessage();
        }
    }

    // *** Thử nghiệm tấn công ***
    // Nếu ứng dụng đang chạy, kẻ tấn công sẽ gửi yêu cầu:
    // GET /api/run-command?cmd=ping -c 1 127.0.0.1%20%26%20ls%20/
    // Hoặc trên Linux/macOS: ?cmd=id
}