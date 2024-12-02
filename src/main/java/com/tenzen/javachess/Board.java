package com.tenzen.javachess;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Board extends Group {
    private final static Paint LIGHT_COLOR = Color.BISQUE;
    private final static Paint DARK_COLOR = Color.BURLYWOOD;

    public Board() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                int x = (i * 8 + j) + (i % 2 == 0 ? 0 : 1);
                Square square = new Square(new Position(i, j), x % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                square.setOnMouseClicked(event -> {
                    handleClick(square.position);
                });
                getChildren().add(square);
                Logger.trace(square.toString());
            }
        }
    }

    private void handleClick(Position position) {
        Logger.debug("handleClick: " + position.toString());
    }

    private static class Square extends Rectangle {
        public final Position position;

        public Square(Position position, Paint paint) {
            super (position.getFile() * 64, position.getRank() * 64, 64, 64);
            setFill(paint);
            this.position = position;
        }
    }
}
