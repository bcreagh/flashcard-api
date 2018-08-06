package com.bcreagh.fc.persistance;

import com.bcreagh.fc.domain.Category;
import com.bcreagh.fc.domain.Flashcard;
import com.bcreagh.fc.persistance.utilities.ContextUtility;
import com.bcreagh.fc.persistance.utilities.ResultSetConverter;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public boolean categoryExists(int id) throws SQLException {
        Category category = null;

        try (
                Connection conn = DriverManager.getConnection(dbUrl, username, password);
                PreparedStatement stmt = conn.prepareStatement("select * from category where category_id = ?")
        ) {

            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                return rs.first();
            }
        }
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
                category = ResultSetConverter.getCategoryFromResultSet(rs);
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
                    categories.add(ResultSetConverter.getCategoryFromResultSet(rs));
                }
        }

        return categories;
    }

    public int countNumberOfCardsInACategory(int id) throws SQLException {

        int count;
        String sql = "select count(*) from flashcard, category, card_category " +
                "where flashcard.card_id = card_category.card_id " +
                "and card_category.category_id = category.category_id " +
                "and category.category_id = ?";

        if(id <= 0) {
            throw new IllegalArgumentException("The id of the category must be greater than 0");
        }

        try (
                Connection conn = DriverManager.getConnection(dbUrl, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                rs.first();
                count = rs.getInt(1);


            }
        }
        return count;
    }

    public Flashcard getRandomCardByCategoryId(int categoryId) throws SQLException {
        //returns a random card from the category specified by the categoryId

        int numberOfCardsInCategory = countNumberOfCardsInACategory(categoryId);
        int randomlyGeneratedPosition = getRandomInt(numberOfCardsInCategory);

        Flashcard flashcard = getNthCardInCategory(categoryId, randomlyGeneratedPosition);
        return flashcard;
    }

    public Flashcard getNthCardInCategory(int categoryId, int n) throws SQLException {
        List<Flashcard> flashcards = getCardsByCategory(categoryId, n - 1, 1);
        if(flashcards.size() == 0) {
            throw new IllegalArgumentException("No flashcard exists at position " + n);
        }
        return flashcards.get(0);
    }



    public List<Flashcard> getCardsByCategory(int categoryId, int skip, int take) throws SQLException {
        String sql = "select flashcard.* from flashcard, category, card_category " +
                "where flashcard.card_id = card_category.card_id " +
                "and card_category.category_id = category.category_id " +
                "and category.category_id = ? " +
                "limit ?, ?";
        List<Flashcard> flashcards = new ArrayList<>();

        if(categoryId <= 0) {
            throw new IllegalArgumentException("The id of the category must be greater than 0");
        }
        if(skip < 0) {
            throw new IllegalArgumentException("The skip parameter must be greater than 0");
        }
        if(take < 0) {
            throw new IllegalArgumentException("The take parameter must be greater than 0");
        }

        try (
                Connection conn = DriverManager.getConnection(dbUrl, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, categoryId);
            stmt.setInt(2, skip);
            stmt.setInt(3, take);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    flashcards.add(ResultSetConverter.getFlashcardFromResultSet(rs));
                }
            }
        }
        return flashcards;
    }

    private int getRandomInt(int max) {
        Random random = new Random();
        int result = random.nextInt(max) + 1;
        return result;
    }

}
