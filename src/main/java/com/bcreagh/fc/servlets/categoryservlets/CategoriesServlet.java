package com.bcreagh.fc.servlets.categoryservlets;

import com.bcreagh.fc.domain.Category;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@WebServlet("/categories")
public class CategoriesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        Category category = new Category();
        category.setId(1);
        category.setName("None Selected");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Java");

        Category category3 = new Category();
        category3.setId(3);
        category3.setName("C");

        Category category4 = new Category();
        category4.setId(4);
        category4.setName("C#");

        List categories = new ArrayList<>();
        categories.add(category);
        categories.add(category2);
        categories.add(category3);
        categories.add(category4);


        Gson gson = new Gson();
        String json = gson.toJson(categories);

        response.setHeader("content-type", "application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.getWriter().write(json);
    }
}
