package com.bcreagh.fc.servlets.categoryservlets;

import com.bcreagh.fc.domain.Flashcard;
import com.bcreagh.fc.persistance.CategoryRepository;
import com.bcreagh.fc.persistance.FlashcardRepository;
import com.bcreagh.fc.servlets.BaseServlet;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@WebServlet("/category/cards/random")
public class RandomCardByCategoryServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {

            CategoryRepository categoryRepository = new CategoryRepository();
            int id = getPositiveIntParameter(request, "id");

            Flashcard flashcard = categoryRepository.getRandomCardByCategoryId(id);

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
