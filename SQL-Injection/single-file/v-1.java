String productId = request.getParameter("id");
String query = "SELECT * FROM products WHERE id = " + productId;

Statement st = connection.createStatement();
ResultSet rs = st.executeQuery(query);

while (rs.next()) {
String name = rs.getString("name");
String description = rs.getString("description");
// display product details
}