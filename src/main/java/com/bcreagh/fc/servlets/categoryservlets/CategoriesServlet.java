package com.bcreagh.fc.servlets.categoryservlets;

import com.bcreagh.fc.persistance.CategoryRepository;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//@WebServlet("/categories")
public class CategoriesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        try {
            CategoryRepository categoryRepository = new CategoryRepository();
            List categories = categoryRepository.getAllCategories();

            Gson gson = new Gson();
            String json = gson.toJson(categories);

            response.setHeader("content-type", "application/json");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.getWriter().write(json);

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
