import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public class Persistence {
    private final Engine engine;

    public Persistence(Engine engine) {
        this.engine = engine;
    }

    public void save() {
        var sb = new StringBuilder();

        try {
            var document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            var gameElement = document.createElement("game");
            document.appendChild(gameElement);
            // This is from previous implementation, save each pile in a new line
            var newLine = System.lineSeparator();
            for (var pile : engine.getMainPiles()) {
                sb.append(pile.toString()).append(newLine);
            }
            for (var pile : engine.finalPiles()) {
                sb.append(pile.toString()).append(newLine);
            }
            sb.append(engine.getDrawPile().toString()).append(newLine);
            sb.append(engine.getPile().toString()).append(newLine);
            String[] lines = sb.toString().split(newLine);

            for (var line : lines) {
                var pileElement = document.createElement("pile");
                String[] cardStrings = line.split("-");
                for (var card : cardStrings) {
                    String[] parts = card.split(" of ");
                    var cardE = document.createElement("card");
                    cardE.setAttribute("value", parts[0]);
                    cardE.setAttribute("suit", parts[1]);
                    cardE.setAttribute("isReversed", parts[2]);
                    pileElement.appendChild(cardE);
                }
                gameElement.appendChild(pileElement);
            }
            var transformer = TransformerFactory.newInstance().newTransformer();
            var domSource = new DOMSource(document);
            var streamResult = new StreamResult(new File("save.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(domSource, streamResult);
        } catch (Exception _) {
        }
    }

    public void load() {
        try {
            var nodeList = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse("save.xml")
                    .getDocumentElement()
                    .getChildNodes();

            int currentPileCount = 0;
            // Iterate through all piles
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE)
                    continue;
                var element = (Element) nodeList.item(i);
                if (element.getNodeName().contains("pile")) {
                    var cardList = element.getChildNodes();
                    var tempPile = Pile.NewNormalPile(100, 15);
                    // Iterate through all cards
                    for (int j = 0; j < cardList.getLength(); j++) {
                        if (cardList.item(j).getNodeType() != Node.ELEMENT_NODE)
                            continue;
                        var cardNode = (Element) cardList.item(j);
                        String suitName = cardNode.getAttribute("suit");
                        boolean isReversed = cardNode.getAttribute("isReversed").equals("true");
                        int value = Card.valueInt(cardNode.getAttribute("value"));
                        // Skip the base card
                        if (value == 100)
                            continue;
                        // Search for the card in all piles
                        Card card = null;
                        Pile foundPile = null;
                        for (Pile p : engine.allPiles()) {
                            if ((card = p.searchCard(value, suitName)) != null) {
                                foundPile = p;
                                break;
                            }
                        }
                        tempPile.addCard(card);
                        foundPile.removeCard(card);
                        // Face-up or face-down card
                        if (isReversed)
                            card.ocultar();
                        else
                            card.mostrar();
                    }
                    // Add the cards to the correct pile
                    if (currentPileCount < Engine.NUMBER_OF_GAME_PILES)
                        engine.getMainPiles().get(currentPileCount).merge(tempPile);
                    else if (currentPileCount < Engine.NUMBER_OF_GAME_PILES + 4) {
                        engine.finalPiles().get(currentPileCount - Engine.NUMBER_OF_GAME_PILES).merge(tempPile);
                        if (!tempPile.isEmpty()) {
                            // Set the pile filter for final piles
                            var card = tempPile.peekTopCard();
                            engine.finalPiles().get(currentPileCount - Engine.NUMBER_OF_GAME_PILES)
                                    .setSuitFilter(card.suit());
                        }
                    } else if (currentPileCount == Engine.NUMBER_OF_GAME_PILES + 4)
                        engine.getDrawPile().merge(tempPile);
                    else
                        engine.getPile().merge(tempPile);
                    currentPileCount++;
                }
            }
            // Draw and add the cards again so the offsets are re-calculated
            for (var pile : engine.allPiles()) {
                var cards = new ArrayList<Card>();
                while (!pile.isEmpty()) {
                    cards.add(pile.drawCard());
                }
                for (var card : cards) {
                    pile.addCard(card);
                }
            }
        } catch (Exception _) {
        }
    }
}
