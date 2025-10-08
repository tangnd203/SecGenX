String sql = \"SELECT * FROM users WHERE user = '\" + username + \"' AND pass = '\" + password + \"'\";
Statement statement = connection.createStatement();
ResultSet rs = statement.executeQuery(sql);