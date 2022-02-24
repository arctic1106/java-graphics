import java.io.IOException;

public class Solitario {

    public static void main(String[] args) throws IOException {
        new GUI(new Engine(new Deck()));
    }
}
