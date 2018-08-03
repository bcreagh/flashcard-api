package com.bcreagh.fc.servlets.cardservlets;

import com.bcreagh.fc.domain.Flashcard;
import com.bcreagh.fc.persistance.FlashcardRepository;
import com.bcreagh.fc.servlets.BaseServlet;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cards")
public class CardByIdServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = getPositiveIntParameter(request, "id");
            FlashcardRepository flashcardRepository = new FlashcardRepository();

            Flashcard flashcard = flashcardRepository.getFlashcardById(id);

            Gson gson = new Gson();
            String json = gson.toJson(flashcard);

            response.setHeader("content-type", "application/json");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.getWriter().write(json);


        } catch (IllegalArgumentException e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
