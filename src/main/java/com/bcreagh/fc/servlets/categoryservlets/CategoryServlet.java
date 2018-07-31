package com.bcreagh.fc.servlets.categoryservlets;

import com.bcreagh.fc.domain.Category;
import com.bcreagh.fc.persistance.CategoryRepository;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/category")
public class CategoryServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            int id;

            id = getPositiveIntParameter(request, "id");

            CategoryRepository categoryRepository = new CategoryRepository();
            Category category = categoryRepository.getCategory(id);
            Gson gson = new Gson();

            String json = gson.toJson(category);

            response.setHeader("content-type", "application/json");
            response.setHeader("Access-Control-Allow-Origin", "*");

            response.getWriter().write(json);


        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    // doPost

    // doPut

    // doDelete

    private int getPositiveIntParameter(HttpServletRequest request, String param) {
        int paramValue = getIntParameter(request, param);

        if(paramValue <= 0) {
            throw new IllegalArgumentException("The parameter was not positive: " + paramValue);
        }

        return paramValue;
    }

    private int getIntParameter(HttpServletRequest request, String param) {
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



}
