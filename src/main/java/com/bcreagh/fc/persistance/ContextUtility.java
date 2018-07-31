package com.bcreagh.fc.persistance;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextUtility {

    // DISCLAIMER: I would usually be very hesitant to create utility classes like this one, with all static methods
    // They tend to lead to tightly coupled, difficult to test code
    // However, this project is for revision purposes only and I didn't feel the need to set up dependency injection and extensive testing

    public static String getStringResource(String resourceName) throws NamingException {
        String result = null;

        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:/comp/env");
        result = (String) envCtx.lookup(resourceName);

        return result;
    }

}
