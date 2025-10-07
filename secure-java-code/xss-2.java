import org.apache.commons.text.StringEscapeUtils; 

// Tên file: XSSFixed.java (Yêu cầu thư viện Apache Commons Text)
public class XSSFixed {
    
    // Hàm giả lập Servlet an toàn
    public static String generateOutputSafe(String userInput) {
        
        // DÒNG AN TOÀN: Mã hóa đầu vào
        // Biến < thành &lt; và > thành &gt;
        String safeInput = StringEscapeUtils.escapeHtml4(userInput); 
        
        String htmlOutput = "<html><body>";
        htmlOutput += "<h2>Kết quả tìm kiếm cho: " + safeInput + "</h2>";
        htmlOutput += "</body></html>";
        
        return htmlOutput;
    }

    public static void main(String[] args) {
        String maliciousPayload = "<script>alert('XSS by XSSVulnerable')</script>";
        String result = generateOutputSafe(maliciousPayload);
        
        System.out.println("--- MÃ FIX: Sử dụng Output Encoding ---");
        System.out.println("Output an toàn được tạo ra:");
        System.out.println(result);
        System.out.println("-> Thẻ <script> đã được mã hóa thành văn bản và không được thực thi.");
    }
}