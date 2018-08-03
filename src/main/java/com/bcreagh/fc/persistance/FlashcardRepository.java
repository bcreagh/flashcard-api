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

}
