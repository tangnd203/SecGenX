// Tên file: FixedSearchServlet.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement; // THAY ĐỔI QUAN TRỌNG
import java.sql.ResultSet;
// ... (các imports khác)

public class FixedSearchServlet extends HttpServlet {
    
    // ... (các hằng số DB_URL, USER, PASS)

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");

        if (username == null || username.isEmpty()) {
            response.getWriter().println("Vui lòng cung cấp tên người dùng.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
             
            // DÒNG AN TOÀN 1: Sử dụng placeholder (?) trong truy vấn
            String sql = "SELECT email FROM users WHERE username = ?";
            
            // DÒNG AN TOÀN 2: Sử dụng PreparedStatement
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                // --- BƯỚC GÁN THAM SỐ AN TOÀN ---
                // Dữ liệu người dùng được gán dưới dạng LITERAL (giá trị), không phải mã SQL.
                pstmt.setString(1, username);
                
                System.out.println("[FIXED] SQL gốc với placeholder: " + sql); 

                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    response.getWriter().println("Email tìm thấy: " + rs.getString("email"));
                } else {
                    response.getWriter().println("Không tìm thấy người dùng.");
                }
            }

        } catch (Exception e) {
            response.getWriter().println("Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }
}