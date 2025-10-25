String productId = request.getParameter("id");
String query = "SELECT * FROM products WHERE id = ?";

PreparedStatement st = connection.prepareStatement(query);
st.setString(1, productId);
ResultSet rs = st.executeQuery();

while (rs.next()) {
String name = rs.getString("name");
String description = rs.getString("description");
// display product details
}