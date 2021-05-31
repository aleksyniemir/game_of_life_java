package com.eee;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainView mainView = new MainView();
        Scene sceneOfGame = new Scene(mainView, 69.5 * mainView.tableWidth, 48 * mainView.tableHeight);

        stage.setScene(sceneOfGame);
        mainView.draw(mainView.tableWidth, mainView.tableHeight);
        stage.setTitle("GameOfLife");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}