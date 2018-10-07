package com.bcreagh.fc.servlets.cardservlets;

import com.bcreagh.fc.domain.DTO.StringResult;
import com.bcreagh.fc.domain.Flashcard;
import com.bcreagh.fc.persistance.FlashcardRepository;
import com.bcreagh.fc.servlets.BaseServlet;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cards")
public class CardServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = getPositiveIntParameter(request, "id");
            FlashcardRepository flashcardRepository = new FlashcardRepository();

            Flashcard flashcard = flashcardRepository.getFlashcardById(id);

            writeJsonResponse(response, flashcard);
            
        } catch (IllegalArgumentException e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            int categoryId = getPositiveIntParameter(request, "categoryId");
            FlashcardRepository flashcardRepository = new FlashcardRepository();

            Flashcard flashcard = getObjectFromJsonRequest(request, Flashcard.class);

            flashcardRepository.createFlashcard(flashcard, categoryId);

            writeJsonResponse(response, new StringResult("Ok"));

        } catch (IllegalArgumentException e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.err.println(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
