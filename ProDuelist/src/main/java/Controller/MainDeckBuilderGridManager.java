package Controller;

import Model.Card;
import Model.CardDAO;
import Model.CardImageView;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class MainDeckBuilderGridManager extends DeckSectionManager {
    private final GridPane mainDeckGird;
    private final int gridColumns;
    private final int gridMaxSize = 60;

    public MainDeckBuilderGridManager(GridPane mainDeckGird, int gridColumns) {
        this.mainDeckGird = mainDeckGird;
        this.gridColumns = gridColumns;
        setOnDragAndDrop(mainDeckGird);
    }

    @Override
    public void addCard(Node node) {
        if (node instanceof CardImageView cardImageView) {
            int cardId = cardImageView.getCard().getId();
            if (mainDeckGird.getChildren().size() < gridMaxSize) {
                int mainDeckSize = mainDeckGird.getChildren().size();
                int column = mainDeckSize % gridColumns;
                int row = mainDeckSize / gridColumns;
                mainDeckGird.add(node, column, row);
                DeckSectionManager.increaseCardCount(cardId);
                //System.out.println("Có " + getCardCount(cardImageView.getCard().getId()) + " lá bài tên " + cardImageView.getCard().getName());
            }
        }
    }

    @Override
    public void removeCard(Node node) {
        mainDeckGird.getChildren().remove(node);
        if (node instanceof CardImageView cardImageView) {
            DeckSectionManager.decreaseCardCount(cardImageView.getCard().getId());
            //System.out.println("Có " + getCardCount(cardImageView.getCard().getId()) + " lá bài tên " + cardImageView.getCard().getName());
        }
        rearrangDeck();
    }

    @Override
    public void loadDeckSection(List<String> cardIds) {
        mainDeckGird.getChildren().clear();
        for (String cardID : cardIds) {
            Card card = CardDAO.getCardById(cardID);
            if (card != null) {
                CardImageView cardImageView = new CardImageView(card);
                //setOnDragAndDrop(cardImageView);
                attachDragDoneHandler(cardImageView);
                addCard(cardImageView);
                cardImageView.setOnMouseEntered(_ -> showCardInfo(card));
            }
        }
    }

    @Override
    protected void rearrangDeck() {
        List<Node> nodes = new ArrayList<>(mainDeckGird.getChildren());
        mainDeckGird.getChildren().clear();
        for (int coorinate = 0; coorinate < nodes.size() && coorinate < gridMaxSize; coorinate++) {
            int column = coorinate % gridColumns;
            int row = coorinate / gridColumns;
            mainDeckGird.add(nodes.get(coorinate), column, row);
        }
    }
}
