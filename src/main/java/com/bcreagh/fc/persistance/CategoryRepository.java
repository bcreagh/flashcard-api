package com.bcreagh.fc.persistance;

import com.bcreagh.fc.domain.Category;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private final String dbUrl = "jdbc:mysql://localhost:3306/flashcards?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String username;
    private final String password;
    private ContextUtility contextUtility = ContextUtility.getInstance();

    public CategoryRepository() throws ClassNotFoundException, NamingException {
        username = contextUtility.getUsername();
        password = contextUtility.getPassword();
        Class.forName("com.mysql.jdbc.Driver"); //this is done to register the jdbc driver with the driver manager
    }

    public Category getCategory(int id) throws SQLException {
        Category category = null;

        try (
                Connection conn = DriverManager.getConnection(dbUrl, username, password);
                PreparedStatement stmt = conn.prepareStatement("select * from category where category_id = ?")
        ) {

            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                rs.first();
                category = getCategoryFromResultSet(rs);
            }
        }

        if(category.getId() > 0) {
            return category;
        } else {
            throw new IllegalArgumentException("The record could not be found");
        }
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();

        try (
                Connection conn = DriverManager.getConnection(dbUrl, username, password);
                PreparedStatement stmt = conn.prepareStatement("select * from category");
                ResultSet rs = stmt.executeQuery()
        ) {
                while (rs.next()){
                    categories.add(getCategoryFromResultSet(rs));
                }
        }

        return categories;
    }

    private Category getCategoryFromResultSet(ResultSet resultSet) throws SQLException{
        Category category = new Category();
        category.setId(resultSet.getInt("category_id"));
        category.setName(resultSet.getString("category_name"));
        return category;
    }

}
