package com.arcticsoft.ui.common;

import com.arcticsoft.domain.card.AbstractCard;
import com.arcticsoft.domain.card.CardColor;
import com.arcticsoft.domain.card.NumberCard;

import java.awt.*;

public class StyleUtil {
    public static final Color redColor = new Color(192, 80, 77);
    public static final Color blueColor = new Color(31, 73, 125);
    public static final Color greenColor = new Color(0, 153, 0);
    public static final Color yellowColor = new Color(255, 204, 0);
    public static final Color blackColor = new Color(0, 0, 0);
    public static final String DEFAULT_FONT = "Helvetica";
    private static final Character REVERSE_CHAR = (char) 8634;
    private static final Character SKIP_CHAR = (char) Integer.parseInt("2718", 16);

    private StyleUtil() {
    }

    public static Color convertCardColor(CardColor color) {
        if (color == null) {
            return blackColor;
        }
        return switch (color) {
            case RED -> redColor;
            case GREEN -> greenColor;
            case BLUE -> blueColor;
            case YELLOW -> yellowColor;
        };
    }

    public static String getValueToDisplay(AbstractCard card) {
        return switch (card.getType()) {
            case NUMBER -> Integer.toString(((NumberCard) card).getValue());
            case SKIP -> SKIP_CHAR.toString();
            case REVERSE -> REVERSE_CHAR.toString();
            case DRAW_TWO -> "2+";
            case WILD_COLOR -> "W";
            case WILD_DRAW_FOUR -> "4+";
        };
    }
}
