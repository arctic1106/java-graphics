import java.util.Collections;
import java.util.EventListener;
import java.util.Stack;

public class Deck {
    private final Stack<Card> cards = new Stack<>();

    public Deck() {
        for (var suit : Suit.values()) {
            for (var value = 1; value < 14; value++) {
                cards.push(new Card(suit, value));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int size() {
        return cards.size();
    }

    public Card popCard() {
        return cards.pop();
    }

    public Stack<Card> cards() {
        return cards;
    }

    public void addCardListeners(EventListener listenersImpl) {
        for (var c : cards()) {
            c.addMouseListeners(listenersImpl);
        }
    }
}
