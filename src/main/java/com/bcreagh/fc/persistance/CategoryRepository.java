package com.bcreagh.fc.persistance;

import com.bcreagh.fc.domain.Category;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.*;

public class CategoryRepository {

    private final String dbUrl = "jdbc:mysql://localhost:3306/flashcards?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public Category getCategory(int id) throws SQLException, ClassNotFoundException, NamingException {
        Category category = new Category();
        String username = getUsername();
        String password = getPassword();
        Class.forName("com.mysql.jdbc.Driver");

        try (
                Connection conn = DriverManager.getConnection(dbUrl, username, password);
                PreparedStatement stmt = conn.prepareStatement("select * from category where category_id = ?")
        ) {

            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                rs.first();
                category.setId(rs.getInt("category_id"));
                category.setName(rs.getString("category_name"));
            }
        }

        if(category.getId() > 0) {
            return category;
        } else {
            throw new IllegalArgumentException("The record could not be found");
        }
    }

    private String getUsername() throws NamingException {
        return ContextUtility.getStringResource("linkDbUser");
    }

    private String getPassword() throws NamingException {
        return ContextUtility.getStringResource("linkDbPassword");
    }

}
