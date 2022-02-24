import java.awt.*;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class Engine {
    public static final int NUMBER_OF_GAME_PILES = 7;

    private final Deck deck;
    private final Pile drawPile = Pile.NewDrawPile();
    private final Pile getPile = Pile.NewGetPile();
    private final ArrayList<Pile> mainPiles = new ArrayList<>();
    private final ArrayList<Pile> suitStacks = new ArrayList<>();
    private final ArrayList<Pile> allPiles = new ArrayList<>();

    public Engine(Deck deck) {
        this.deck = deck;
        drawPile.updateSize();
        getPile.updateSize();
    }

    public void setupGame(EventListener listenersImpl) {
        addCardListeners(listenersImpl);
        createMainPiles();
        createSuitStacks();
        while (deck.size() > 0) {
            Card card = deck.popCard();
            card.ocultar();
            drawPile.addCard(card);
        }
    }

    private void createSuitStacks() {
        for (var i = 0; i < Suit.values().length; i++) {
            var pile = Pile.NewFinalPile();
            pile.updateSize();
            suitStacks.add(pile);
            allPiles.add(pile);
        }
    }

    private void createMainPiles() {
        for (var i = 0; i < NUMBER_OF_GAME_PILES; i++) {
            var pile = createPileOfNCards(i + 1);
            mainPiles.add(pile);
            allPiles.add(pile);
        }
    }

    private void addCardListeners(EventListener listenersImpl) {
        deck().addCardListeners(listenersImpl);
    }

    private Pile createPileOfNCards(int n) {
        var pile = Pile.NewNormalPile(120, 15);
        for (int i = 1; i <= n; ++i) {
            var card = deck.popCard();
            pile.addCard(card);
            if (i != n)
                card.ocultar();
            else
                card.mostrar();
        }
        return pile;
    }

    public void drawCard() {
        if (!drawPile.isEmpty()) {
            var card = drawPile.drawCard();
            card.mostrar();
            getPile.addCard(card);
        }
    }

    public void clickPile(Pile pile) {
        pile.showTopCard();
    }

    public void turnGetPile() {
        if (!drawPile.isEmpty())
            return;
        while (!getPile.isEmpty()) {
            var card = getPile.drawCard();
            card.ocultar();
            drawPile.addCard(card);
        }
    }

    public boolean checkWin() {
        for (var suitPile : suitStacks) {
            if (!suitPile.isComplete())
                return false;
        }
        return true;
    }

    public Deck deck() {
        return deck;
    }

    public List<Pile> getMainPiles() {
        return mainPiles;
    }

    public Pile getDrawPile() {
        return drawPile;
    }

    public Pile getPile() {
        return getPile;
    }

    public List<Pile> finalPiles() {
        return suitStacks;
    }

    public List<Pile> allPiles() {
        return allPiles;
    }

    public boolean mergeIfPossible(Pile tempPile, Point mousePosition) {
        var match = false;
        var droppable = new ArrayList<>(getMainPiles());
        droppable.addAll(finalPiles());
        for (var pile : droppable) {
            var pilePosition = pile.getLocationOnScreen();
            var rectangle = pile.getBounds();
            rectangle.x = pilePosition.x;
            rectangle.y = pilePosition.y;
            if (rectangle.contains(mousePosition) && pile.acceptsPile(tempPile)) {
                pile.merge(tempPile);
                match = true;
                break;
            }
        }
        return match;
    }
}
