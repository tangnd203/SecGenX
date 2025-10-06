// Tên file: VulnerableSearchServlet.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VulnerableSearchServlet extends HttpServlet {
    
    // Giả lập thông tin kết nối
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "dbuser";
    private static final String PASS = "dbpass";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- BƯỚC 1: Lấy dữ liệu không an toàn (Source) ---
        String username = request.getParameter("username");

        if (username == null || username.isEmpty()) {
            response.getWriter().println("Vui lòng cung cấp tên người dùng.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             // DÒNG NGUY HIỂM: Khai báo Statement
             Statement stmt = conn.createStatement()) {

            // --- BƯỚC 2: Nối chuỗi trực tiếp vào truy vấn (Sink) ---
            // Snyk Code sẽ phát hiện luồng dữ liệu từ request.getParameter() 
            // đến hàm executeQuery() qua nối chuỗi.
            String sql = "SELECT email FROM users WHERE username = '" + username + "'";
            
            System.out.println("[VULNERABLE] SQL thực thi: " + sql); 

            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                response.getWriter().println("Email tìm thấy: " + rs.getString("email"));
            } else {
                response.getWriter().println("Không tìm thấy người dùng.");
            }

        } catch (Exception e) {
            response.getWriter().println("Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }
}