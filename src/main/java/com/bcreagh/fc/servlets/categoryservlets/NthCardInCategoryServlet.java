package com.bcreagh.fc.servlets.categoryservlets;

import com.bcreagh.fc.domain.Flashcard;
import com.bcreagh.fc.persistance.CategoryRepository;
import com.bcreagh.fc.servlets.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/categories/cards")
public class NthCardInCategoryServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            int categoryId = getPositiveIntParameter(request, "categoryId");
            int cardPosition = getPositiveIntParameter(request, "cardPosition");
            CategoryRepository categoryRepository = new CategoryRepository();

            Flashcard flashcard = categoryRepository.getNthCardInCategory(categoryId, cardPosition);

            writeJsonResponse(response, flashcard);

        } catch (IllegalArgumentException e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
