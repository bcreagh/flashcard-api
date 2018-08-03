package com.bcreagh.fc.servlets.categoryservlets;

import com.bcreagh.fc.persistance.CategoryRepository;
import com.bcreagh.fc.servlets.BaseServlet;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//@WebServlet("/categories")
public class CategoriesServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){

        try {
            CategoryRepository categoryRepository = new CategoryRepository();
            List categories = categoryRepository.getAllCategories();

            writeJsonResponse(response, categories);

        } catch (IllegalArgumentException e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
