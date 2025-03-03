package org.example.produelist;

import Model.Card;
import Model.CardAPI;
import Model.CardDAO;
import Model.DatabaseManager;

import java.util.List;

public class CreateSQlLiteDatabase {
    public static void main(String[] args) {
        DatabaseManager.createTable();

        List<Card> cards = CardAPI.fetchCards();
        CardDAO.saveCards(cards);

        List<Card> savedCards = CardDAO.getAllCards();
        for (Card card : savedCards) {
            System.out.println(card.getName() + " - " + card.getType());
        }
    }
}
