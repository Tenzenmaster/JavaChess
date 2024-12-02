package com.tenzen.javachess;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class JavaChess extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Board board = new Board();
        Image image = new Image(new FileInputStream("res/pieces/black-bishop.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(64);
        imageView.setFitWidth(64);
        board.getChildren().add(imageView);

        Scene scene = new Scene(board);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.show();
    }

    public static void main(String[] args) {
        Logger.logLevel = Logger.LogLevel.TRACE;
        launch();
    }
}