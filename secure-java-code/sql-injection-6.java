"String sql = \"SELECT * FROM users WHERE user = ? AND pass = ?\";
"PreparedStatement ps = connection.prepareStatement(sql);
"ps.setString(1, username);
"ps.setString(2, password);
"ResultSet rs = ps.executeQuery();