package com.bcreagh.fc.persistance;

import com.bcreagh.fc.domain.Category;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.*;

public class CategoryRepository {

    private final String dbUrl = "jdbc:mysql://localhost:3306/flashcards?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public Category getCategory(int id) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Category category = new Category();
        String username = getUsername();
        String password = getPassword();

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(dbUrl, username, password);

            stmt = conn.prepareStatement("select * from category where category_id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            rs.first();
            category.setId(rs.getInt("category_id"));
            category.setName(rs.getString("category_name"));
        } catch (SQLException e) {
            e.printStackTrace();
            category.setName(e.toString());

        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            category.setName(e.toString());

        } finally {
            if(rs != null) {
                rs.close();
            }
            if(stmt != null) {
                stmt.close();
            }

            if(conn != null) {
                conn.close();
            }
        }
        if(category.getId() > 0) {
            return category;
        } else {
            throw new IllegalArgumentException("The record could not be found: " + category.getName());
        }

    }

    private String getUsername() throws NamingException {
        String username = null;

        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:/comp/env");
        username = (String) envCtx.lookup("linkDbUser");

        return username;
    }

    private String getPassword() throws NamingException {
        String password = null;

        Context initialContext = new InitialContext();
        Context envContext = (Context) initialContext.lookup("java:/comp/env");
        password = (String) envContext.lookup("linkDbPassword");

        return password;
    }

}
