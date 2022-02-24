package com.arcticsoft.ui.view;

import com.arcticsoft.domain.card.CardColor;

import javax.swing.*;
import java.util.ArrayList;

public class ColorPicker {
    private final ArrayList<String> colors;

    private ColorPicker() {
        colors = new ArrayList<>();
        for (var color : CardColor.values()) {
            colors.add(color.name());
        }
    }

    public static ColorPicker getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public CardColor show() {
        String pickedColor = (String) JOptionPane.showInputDialog(
                null,
                "Choose a color",
                "Wild Card",
                JOptionPane.PLAIN_MESSAGE,
                null,
                colors.toArray(),
                null);

        return CardColor.valueOf(pickedColor);
    }

    private static class SingletonHelper {
        private static final ColorPicker INSTANCE = new ColorPicker();
    }
}
