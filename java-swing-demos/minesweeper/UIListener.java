import java.awt.Color;

import javax.swing.Icon;

interface UIListener {
    void updateCellContent(int x, int y, String content);

    void updateCellIcon(int x, int y, Icon icon);

    void updateCellBackground(int x, int y, Color color);

    void updateCellText(int x, int y, String text);

    void updateCellTextColor(int x, int y, Color color);
}
