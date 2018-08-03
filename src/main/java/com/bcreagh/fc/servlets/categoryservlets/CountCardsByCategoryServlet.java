package com.bcreagh.fc.servlets.categoryservlets;

import com.bcreagh.fc.domain.DTO.IntegerResult;
import com.bcreagh.fc.persistance.CategoryRepository;
import com.bcreagh.fc.servlets.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/category/count")
public class CountCardsByCategoryServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = getPositiveIntParameter(request, "id");

            CategoryRepository categoryRepository = new CategoryRepository();
            int count = categoryRepository.countNumberOfCardsInACategory(id);

            IntegerResult result = new IntegerResult(count);

            writeJsonResponse(response, result);

        } catch (IllegalArgumentException e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
