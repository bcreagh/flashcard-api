package com.bcreagh.fc.persistance.utilities;

import com.bcreagh.fc.domain.Category;
import com.bcreagh.fc.domain.Flashcard;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetConverter {
    public static Flashcard getFlashcardFromResultSet(ResultSet resultSet) throws SQLException {
        Flashcard flashcard = new Flashcard();
        try {
            flashcard.setId(resultSet.getInt("card_id"));
            flashcard.setQuestion(resultSet.getString("question"));
            flashcard.setAnswer(resultSet.getString("answer"));
            return flashcard;
        } catch (SQLException e) {
            throw new SQLException("Could not create a Flashcard object from the ResultSet", e);
        }
    }

    public static Category getCategoryFromResultSet(ResultSet resultSet) throws SQLException{
        Category category = new Category();
        category.setId(resultSet.getInt("category_id"));
        category.setName(resultSet.getString("category_name"));
        return category;
    }
}
