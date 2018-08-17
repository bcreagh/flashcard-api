package com.bcreagh.fc.persistance;


import com.bcreagh.fc.domain.Flashcard;
import com.bcreagh.fc.persistance.utilities.ContextUtility;
import com.bcreagh.fc.persistance.utilities.ResultSetConverter;

import javax.naming.NamingException;
import java.sql.*;

public class FlashcardRepository {

    private final String dbUrl = "jdbc:mysql://localhost:3306/flashcards?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String username;
    private final String password;
    private ContextUtility contextUtility = ContextUtility.getInstance();

    public FlashcardRepository() throws ClassNotFoundException, NamingException {
        username = contextUtility.getUsername();
        password = contextUtility.getPassword();
        Class.forName("com.mysql.jdbc.Driver"); //this is done to register the jdbc driver with the driver manager
    }

    public Flashcard getFlashcardById(int id) throws SQLException {
        Flashcard flashcard = null;

        try (
                Connection conn = DriverManager.getConnection(dbUrl, username, password);
                PreparedStatement stmt = conn.prepareStatement("select * from flashcard where card_id = ?")
        ) {

            stmt.setInt(1, id);

            try(ResultSet resultSet = stmt.executeQuery()) {
                resultSet.first();
                flashcard = ResultSetConverter.getFlashcardFromResultSet(resultSet);
            }
        }

        if(flashcard != null && flashcard.getId() > 0) {
            return flashcard;
        } else {
            throw new IllegalArgumentException("The record could not be found");
        }
    }

    public void createFlashcard(Flashcard flashcard, int categoryId) throws SQLException, ClassNotFoundException, NamingException {
        CategoryRepository categoryRepository = new CategoryRepository();


        assert categoryId > 0 : "The categoryId must be greater than 0";
        assert flashcard != null : "The flashcard that was passed into this function was null!";
        assert categoryRepository.categoryExists(categoryId) : "The category of the flashcard you tried to create does not exist!";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, username, password);
            connection.setAutoCommit(false);

            int flashcardId = insertRowIntoFlashcardTable(connection, flashcard);

            insertRowIntoCardCategoryTable(connection, flashcardId, categoryId);

            connection.commit();
        } catch (SQLException e) {
            if(connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if(connection != null) {
                connection.close();
            }
        }

    }

    private int insertRowIntoFlashcardTable(Connection connection, Flashcard flashcard) throws SQLException {
        String createFlashcard = "insert into flashcard (question, answer) values (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(createFlashcard, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, flashcard.getQuestion());
            stmt.setString(2, flashcard.getAnswer());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return (int) generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }

    private void insertRowIntoCardCategoryTable(Connection connection, int flashcardId, int categoryId) throws SQLException {
        String createCardCategory = "insert into card_category (card_id, category_id) values (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(createCardCategory)) {

            stmt.setInt(1, flashcardId);
            stmt.setInt(2, categoryId);

            int rowsAffected = stmt.executeUpdate();

            if(rowsAffected <= 0) {
                throw new SQLException("The card_category was not created: " + rowsAffected + " rows were affected");
            }
        }
    }

}
