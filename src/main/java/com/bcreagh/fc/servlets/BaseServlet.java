package com.bcreagh.fc.servlets;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class BaseServlet extends HttpServlet {

    protected int getPositiveIntParameter(HttpServletRequest request, String param) {
        int paramValue = getIntParameter(request, param);

        if(paramValue <= 0) {
            throw new IllegalArgumentException("The parameter was not positive: " + paramValue);
        }

        return paramValue;
    }

    protected int getIntParameter(HttpServletRequest request, String param) {
        int paramValue;

        String paramValueString = request.getParameter(param);

        if(paramValueString != null) {
            try {
                paramValue = Integer.parseInt(paramValueString);
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Could not parse the parameter", exception);
            }
        } else {
            throw new IllegalArgumentException("The request did not contain a parameter called: " + param);
        }

        return paramValue;
    }

    protected <T> T getObjectFromJsonRequest(HttpServletRequest request, Class<T> clazz) throws IOException {
        Gson gson = new Gson();
        String json = getStringFromRequestBody(request);

        T obj = gson.fromJson(json, clazz);
        return obj;
    }

    private String getStringFromRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        String result;

        while((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        result = stringBuilder.toString();
        return result;
    }

    protected <T> void writeJsonResponse(HttpServletResponse response, T data) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(data);

        response.setHeader("content-type", "application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.getWriter().write(json);
    }


}
