import java.sql.*;

public class SQLInjectionVulnerable {
    public static void main(String[] args) {
        String userInput = ""admin'; DROP TABLE users; --""; // User input with malicious SQL injection payload

        try {
            Connection connection = DriverManager.getConnection(""jdbc:mysql://localhost:3306/mydatabase"", ""username"", ""password"");
            Statement statement = connection.createStatement();

            // Vulnerable code - concatenating user input directly into the SQL query
            String query = ""SELECT * FROM users WHERE username='"" + userInput + ""'"";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println(""Username: "" + resultSet.getString(""username""));
                System.out.println(""Password: "" + resultSet.getString(""password""));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}