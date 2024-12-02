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
        Resources.init();

        Game game = new Game();
        game.setup();

        //dbg
        game.board.setPiece(new Position(5, 6), new Piece(Piece.Kind.KNIGHT, Piece.Color.BLACK));
        for (Position pos : game.board.getPieceMoves(new Position(6, 5))) {
            Logger.debug("Available move: " + pos.toString());
        }

        Scene scene = new Scene(game);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.show();
    }

    public static void main(String[] args) {
        Logger.logLevel = Logger.LogLevel.DEBUG;
        launch();
    }
}