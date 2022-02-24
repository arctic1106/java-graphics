package com.arcticsoft;

import com.arcticsoft.application.GameAppService;
import com.arcticsoft.ui.AppFrame;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.swing.*;

@QuarkusMain
public class UnoApp {

    public static void main(String... args) {
        Quarkus.run(UnoApp.App.class, args);
    }

    public static class App implements QuarkusApplication {

        @Override
        public int run(String... args) {
            var appService = new GameAppService();
            SwingUtilities.invokeLater(() -> {
                new AppFrame(appService);
            });
            Quarkus.waitForExit();
            return 0;
        }
    }
}
