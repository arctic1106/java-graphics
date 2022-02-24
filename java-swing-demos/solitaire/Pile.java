import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pile extends JLayeredPane {
    private Pile parent;
    private final PileType type;
    private final List<Card> cards = new ArrayList<>();
    private final CardHolder BASE = new CardHolder();
    private final int width;
    private int offset;
    private Suit suitFilter;

    public static Pile NewDrawPile() {
        return new Pile(120, 0, PileType.Draw);
    }

    public static Pile NewGetPile() {
        return new Pile(180, 0, PileType.Get);
    }

    public static Pile NewFinalPile() {
        return new Pile(100, 0, PileType.Final);
    }

    public static Pile NewNormalPile(int width, int offset) {
        return new Pile(width, offset, PileType.Normal);
    }

    public boolean isComplete() {
        return cards.size() == 13;
    }

    public void showTopCard() {
        if (!this.vacia())
            getTopCard().mostrar();

    }

    private Pile(int width, int offset, PileType pileType) {
        this.width = width;
        this.offset = offset;
        add(BASE, 1, 0);
        type = Objects.requireNonNull(pileType);
    }

    public void clear() {
        cards.clear();
        offset = 0;
        removeAll();
        add(BASE, 1, 0);
    }

    public void setSuitFilter(Suit suitFilter) {
        this.suitFilter = suitFilter;
    }

    public PileType type() {
        return type;
    }

    public void parentMerge() {
        parent.merge(this);
    }

    private Card getTopCard() {
        return cards.getLast();
    }

    public void addCard(Card c) {
        c.setLocation(0, offset * cards.size());
        c.setContainingPile(this);
        cards.add(c);
        this.add(c, 1, 0);
        updateSize();
    }

    public void removeCard(Card c) {
        cards.remove(c);
        this.remove(c);
        updateSize();
    }

    public Card peekTopCard() {
        return cards.getLast();
    }

    public Card drawCard() {
        Card c = cards.getFirst();
        removeCard(c);
        return c;
    }

    public void updateSize() {
        int height = BASE.getSize().height;
        if (!cards.isEmpty())
            height += offset * (cards.size() - 1);
        this.setPreferredSize(new Dimension(width, height));
        this.setSize(width, height);
    }

    public Pile split(Card first) {
        var pile = NewNormalPile(100, 15);
        for (var i = 0; i < cards.size(); ++i) {
            if (cards.get(i) == first) {
                while (i < cards.size()) {
                    var card = cards.get(i);
                    card.setContainingPile(pile);
                    pile.addCard(card);
                    removeCard(card);
                }
            }
        }
        pile.parent = this;
        return pile;
    }

    public void merge(Pile p) {
        for (var card : p.cards) {
            card.setContainingPile(this);
            addCard(card);
        }
        updateSize();
    }

    public Card searchCard(int value, String suitName) {
        for (var card : cards) {
            if (card.value() == value && card.suit().name().equals(suitName))
                return card;
        }
        return null;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    private int tamanyo() {
        return cards.size();
    }

    private boolean vacia() {
        return cards.isEmpty();
    }

    public boolean acceptsPile(Pile pile) {
        if (this == pile)
            return false;

        var newCard = pile.cards.getFirst();
        switch (type()) {
            case Normal -> {
                if (this.vacia())
                    return newCard.value() == 14;
                var topCard = getTopCard();
                if (topCard.mostrada()
                        && (topCard.color() != newCard.color()
                                && (topCard.value() == newCard.value() + 1
                                        || topCard.value() == 12 && newCard.value() == 10)))
                    return true;
            }
            case Final -> {
                // Merge with a single card
                if (pile.tamanyo() > 1)
                    return false;
                // Start with an ace
                if (this.vacia() && newCard.value() == 1) {
                    suitFilter = newCard.suit();
                    return true;
                }
                // Has to be the same color
                if (suitFilter != newCard.suit())
                    return false;
                // Consecutive values, ascending
                var topCard = getTopCard();
                if (topCard.value() == newCard.value() - 1 || topCard.value() == 10 && newCard.value() == 12)
                    return true;
            }
            default -> {
            }
        }
        return false;
    }

    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(BASE.saveAsString()).append("-");
        for (var card : cards) {
            result.append(card.saveAsString()).append("-");
        }
        return result.toString();
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    @Override
    public int getBaseline(int width, int height) {
        return 0;
    }
}
