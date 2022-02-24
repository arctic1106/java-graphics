import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseListenerImpl {

    public void mouseDragged(MouseEvent e, GUI gui) {
        if (gui.tempPile() != null) {
            Point pos = gui.getLocationOnScreen();
            pos.x = e.getLocationOnScreen().x - pos.x - gui.mouseOffset().x;
            pos.y = e.getLocationOnScreen().y - pos.y - gui.mouseOffset().y;
            gui.tempPile().setLocation(pos);
        }
        gui.repaint();
    }

    public void mouseClicked(MouseEvent e, Engine engine) {
        if (e.getComponent() instanceof Card card) {
            var pile = card.containingPile();
            switch (pile.type()) {
                case Draw -> engine.drawCard();
                case Normal -> engine.clickPile(pile);
                case Get -> engine.turnGetPile();
                default -> {
                }
            }
        }
    }

    public void mousePressed(MouseEvent e, GUI gui) {
        if (e.getComponent() instanceof Card card) {
            if (card.mostrada()) {
                var pile = (Pile) card.getParent();
                if (pile.isEmpty() || pile.type().equals(PileType.Final))
                    return;
                gui.setTempPile(pile.split(card));
                gui.getLayeredPane().add(gui.tempPile(), JLayeredPane.DRAG_LAYER);
                var position = gui.getLocationOnScreen();
                gui.setMouseOffset(e.getPoint());
                position.x = e.getLocationOnScreen().x - position.x - gui.mouseOffset().x;
                position.y = e.getLocationOnScreen().y - position.y - gui.mouseOffset().y;
                gui.tempPile().setLocation(position);
                gui.repaint();
            }
        }
    }

    public void mouseReleased(MouseEvent e, GUI gui, Engine engine) {
        if (gui.tempPile() != null) {
            var mousePosition = e.getLocationOnScreen();
            var match = engine.mergeIfPossible(gui.tempPile(), mousePosition);
            if (!match)
                gui.tempPile().parentMerge();
            gui.getLayeredPane().remove(gui.tempPile());
            gui.setTempPile(null);
            gui.repaint();
            if (engine.checkWin()) {
                JOptionPane.showMessageDialog(gui, "You won! Congrats!");
                gui.reset();
            }
        }
    }
}
