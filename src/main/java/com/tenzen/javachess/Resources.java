package com.tenzen.javachess;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Resources {
    private static Image blackBishopImage;
    private static Image blackKingImage;
    private static Image blackKnightImage;
    private static Image blackPawnImage;
    private static Image blackQueenImage;
    private static Image blackRookImage;

    private static Image whiteBishopImage;
    private static Image whiteKingImage;
    private static Image whiteKnightImage;
    private static Image whitePawnImage;
    private static Image whiteQueenImage;
    private static Image whiteRookImage;

    private static Image checkmateImage;

    public static void init() throws FileNotFoundException {
        blackBishopImage = new Image(new FileInputStream("res/pieces/black-bishop.png"));
        blackKingImage = new Image(new FileInputStream("res/pieces/black-king.png"));
        blackKnightImage = new Image(new FileInputStream("res/pieces/black-knight.png"));
        blackPawnImage = new Image(new FileInputStream("res/pieces/black-pawn.png"));
        blackQueenImage = new Image(new FileInputStream("res/pieces/black-queen.png"));
        blackRookImage = new Image(new FileInputStream("res/pieces/black-rook.png"));

        whiteBishopImage = new Image(new FileInputStream("res/pieces/white-bishop.png"));
        whiteKingImage = new Image(new FileInputStream("res/pieces/white-king.png"));
        whiteKnightImage = new Image(new FileInputStream("res/pieces/white-knight.png"));
        whitePawnImage = new Image(new FileInputStream("res/pieces/white-pawn.png"));
        whiteQueenImage = new Image(new FileInputStream("res/pieces/white-queen.png"));
        whiteRookImage = new Image(new FileInputStream("res/pieces/white-rook.png"));

        checkmateImage = new Image(new FileInputStream("res/checkmate.jpg"));
    }

    public static Image getCheckmateImage() {
        return checkmateImage;
    }

    public static Image getImage(Piece.Kind kind, Piece.Color color) {
        return switch (color) {
            case BLACK -> switch (kind) {
                case BISHOP -> blackBishopImage;
                case KING -> blackKingImage;
                case KNIGHT -> blackKnightImage;
                case PAWN -> blackPawnImage;
                case QUEEN -> blackQueenImage;
                case ROOK -> blackRookImage;
            };
            case WHITE -> switch (kind) {
                case BISHOP -> whiteBishopImage;
                case KING -> whiteKingImage;
                case KNIGHT -> whiteKnightImage;
                case PAWN -> whitePawnImage;
                case QUEEN -> whiteQueenImage;
                case ROOK -> whiteRookImage;
            };
        };
    }
}
