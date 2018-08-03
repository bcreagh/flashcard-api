package com.bcreagh.fc.servlets.categoryservlets;

import com.bcreagh.fc.domain.Category;
import com.bcreagh.fc.persistance.CategoryRepository;
import com.bcreagh.fc.servlets.BaseServlet;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/category")
public class CategoryServlet extends BaseServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            int id;

            id = getPositiveIntParameter(request, "id");

            CategoryRepository categoryRepository = new CategoryRepository();
            Category category = categoryRepository.getCategory(id);

            writeJsonResponse(response, category);

        } catch (IllegalArgumentException e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
