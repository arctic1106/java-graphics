
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * InputHandler - Mouse input for the OOP version.
 */
public class InputHandler implements MouseListener, MouseMotionListener {

    private final Game game;
    private int mouseX, mouseY;
    private boolean mouseDown = false;
    private int dragStartX, dragStartY;
    private boolean isDragging = false;

    public InputHandler(Game game) {
        this.game = game;
    }

    @Override public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseDown = true;
            dragStartX = mouseX;
            dragStartY = mouseY;
            isDragging = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!isDragging) {
                game.selectUnit(mouseX, mouseY);
            } else {
                game.boxSelect(dragStartX, dragStartY, mouseX, mouseY);
            }
            mouseDown = false;
            isDragging = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            game.issueCommand(mouseX, mouseY);
        }
    }

    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (mouseDown && Math.sqrt(Math.pow(mouseX - dragStartX, 2) + Math.pow(mouseY - dragStartY, 2)) > 5)
            isDragging = true;
    }

    @Override public void mouseMoved(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }

    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }
    public int getDragStartX() { return dragStartX; }
    public int getDragStartY() { return dragStartY; }
    public boolean isDragging() { return isDragging; }
}
