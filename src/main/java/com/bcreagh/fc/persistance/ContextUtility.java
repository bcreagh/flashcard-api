package com.bcreagh.fc.persistance;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextUtility {

    // DISCLAIMER: I would usually be very hesitant to create utility classes like this one, with all static methods
    // They tend to lead to tightly coupled, difficult to test code
    // However, this project is for revision purposes only and I didn't feel the need to set up dependency injection and extensive testing

    private static final ContextUtility instance = new ContextUtility();

    private final String username;
    private final String dbUrl;
    private final String password;

    public static ContextUtility getInstance() {
        return instance;
    }

    private ContextUtility() {
        if(instance != null) {
            throw new UnsupportedOperationException("ContextUtility has already been instantiated.");
        }

        try {

        username = getStringResource("linkDbUser");
        password = getStringResource("linkDbPassword");
        dbUrl = "jdbc:mysql://localhost:3306/flashcards?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        } catch (NamingException e) {
            throw new RuntimeException("The database credentials could not be retrieved.", e);
        }
    }

    private static String getStringResource(String resourceName) throws NamingException {
        String result = null;

        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:/comp/env");
        result = (String) envCtx.lookup(resourceName);

        return result;
    }


    public String getUsername() {
        return username;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getPassword() {
        return password;
    }
}
