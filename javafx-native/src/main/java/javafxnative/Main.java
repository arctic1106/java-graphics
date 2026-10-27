package javafxnative;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private int clicks;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        var version = new Label("JavaFX " + System.getProperty("javafx.version") + " / Java " + System.getProperty("java.version"));
        var counter = new Label("Clicks: 0");
        var button = new Button("Click me");
        button.setOnAction(e -> counter.setText("Clicks: " + ++clicks));

        var root = new VBox(16, version, counter, button);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));

        stage.setTitle("JavaFX Native");
        stage.setScene(new Scene(root, 400, 700));
        stage.show();
    }
}
