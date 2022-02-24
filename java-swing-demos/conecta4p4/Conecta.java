import javax.swing.*;

public class Conecta {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Conecta::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        var f = new JFrame("Conecta 4!");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Tablero());
        f.pack();
        f.setResizable(false);
        f.setVisible(true);
    }
}
