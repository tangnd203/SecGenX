import java.sql.*;

public class SQLInjectionSecure {
    public static void main(String[] args) {
        String userInput = ""admin'; DROP TABLE users; --""; // User input with malicious SQL injection payload

        try {
            Connection connection = DriverManager.getConnection(""jdbc:mysql://localhost:3306/mydatabase"", ""username"", ""password"");

            // Use prepared statement to safely handle user input
            String query = ""SELECT * FROM users WHERE username=?"";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userInput);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println(""Username: "" + resultSet.getString(""username""));
                System.out.println(""Password: "" + resultSet.getString(""password""));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}