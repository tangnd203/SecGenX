import java.io.*; 
import javax.servlet.*; 
import javax.servlet.http.*; 

public class ClickjackingSecure extends HttpServlet { 
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 

// Secure code: The application sets the X-Frame-Options header to prevent the page from being embedded in a frame or iframe 
response.setHeader("X-Frame-Options", "DENY"); 

// Generate response PrintWriter out = response.getWriter(); 
out.println("<html><body>"); 
out.println("This page is secured against clickjacking attacks with the X-Frame-Options header."); 
out.println("</body></html>"); 
} 
}