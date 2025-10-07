// Tên file: XSSVulnerable.java
public class XSSVulnerable {
    
    // Giả lập một hàm Servlet in ra HTML
    public static String generateOutput(String userInput) {
        // userInput: <script>alert('XSS by XSSVulnerable')</script>
        
        // Dòng NGUY HIỂM: Nối chuỗi trực tiếp vào HTML
        String htmlOutput = "<html><body>";
        htmlOutput += "<h2>Kết quả tìm kiếm cho: " + userInput + "</h2>";
        htmlOutput += "</body></html>";
        
        return htmlOutput;
    }

    public static void main(String[] args) {
        String maliciousPayload = "<script>alert('XSS by XSSVulnerable')</script>";
        String result = generateOutput(maliciousPayload);
        
        System.out.println("--- MÃ LỖI: Thiếu Output Encoding ---");
        System.out.println("Output được tạo ra:");
        System.out.println(result);
        System.err.println("!!! PHÁT HIỆN XSS: Trình duyệt sẽ thực thi thẻ <script>. !!!");
    }
}