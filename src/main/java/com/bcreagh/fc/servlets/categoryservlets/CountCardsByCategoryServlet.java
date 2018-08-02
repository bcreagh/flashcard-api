package com.bcreagh.fc.servlets.categoryservlets;

import com.bcreagh.fc.domain.DTO.IntegerResult;
import com.bcreagh.fc.persistance.CategoryRepository;
import com.bcreagh.fc.servlets.BaseServlet;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet("/category/count")
public class CountCardsByCategoryServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = getPositiveIntParameter(request, "id");

            CategoryRepository categoryRepository = new CategoryRepository();
            int count = categoryRepository.countNumberOfCardsInACategory(id);

            IntegerResult result = new IntegerResult(count);

            Gson gson = new Gson();
            String json = gson.toJson(result);

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
