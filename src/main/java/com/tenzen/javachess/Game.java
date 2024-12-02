package com.tenzen.javachess;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Game extends Group {
    private Board board = new Board();
    private Button resetButton = new Button("Reset");
    private TextField fenField = new TextField(Board.STARTING_FEN);
    private ImageView checkmateImageView;
    private List<HalfTurn> halfTurnList = new ArrayList<>();
    private boolean whiteCanCastleLong = true;
    private boolean whiteCanCastleShort = true;
    private boolean blackCanCastleLong = true;
    private boolean blackCanCastleShort = true;
    private int whiteWins = 0;
    private int blackWins = 0;
    private Text winCountText = new Text("White wins: " + whiteWins + "\nBlack wins: " + blackWins);
    private Text turnText = new Text("White to move");
    private Text welcomeText = new Text("WELCOME TO KRAZYKHESS");

    public Game() {
        resetButton.setLayoutX(1000);
        resetButton.setOnMouseClicked(event -> { setup(); });

        fenField.setLayoutX(1000);
        fenField.setLayoutY(200);
        fenField.setOnAction(event -> {
            setup();
            board.loadFen(fenField.getText());
        });

        checkmateImageView = new ImageView(Resources.getCheckmateImage());
        checkmateImageView.setX(500);
        checkmateImageView.setY(120);
        checkmateImageView.setVisible(false);

        winCountText.setX(1000);
        winCountText.setY(400);

        turnText.setX(800);

        welcomeText.setX(100);
        welcomeText.setY(680);

        getChildren().addAll(board, resetButton, fenField, checkmateImageView, winCountText, turnText, welcomeText);
        board.game = this;
    }

    public void setup() {
        board.loadFen(Board.STARTING_FEN);
        halfTurnList.clear();
        checkmateImageView.setVisible(false);

        whiteCanCastleLong = true;
        whiteCanCastleShort = true;
        blackCanCastleLong = true;
        blackCanCastleShort = true;

        turnText.setText("White to move");
    }

    public void addHalfTurn(HalfTurn halfTurn) {
        halfTurnList.add(halfTurn);
    }

    public Piece.Color turnColor() {
        return halfTurnList.size() % 2 == 0 ? Piece.Color.WHITE : Piece.Color.BLACK;
    }

    public boolean requestPossibleMove(Position from, Position to) {
        if (board.getPiece(from).getColor() != turnColor()) {
            return false;
        }

        Piece captured = board.getPiece(to);
        if (captured != null) {
            if (captured.getKind() == Piece.Kind.KING) {
                System.out.println("Checkmate!");
                checkmateImageView.setVisible(true);
                if (captured.getColor() == Piece.Color.WHITE) {
                    ++blackWins;
                } else {
                    ++whiteWins;
                }
                winCountText.setText("White wins: " + whiteWins + "\nBlack wins: " + blackWins);
            }
        }

        HalfTurn halfTurn = board.movePiece(from, to);
        addHalfTurn(halfTurn);

        if (turnColor() == Piece.Color.WHITE) {
            turnText.setText("White to move");
        } else {
            turnText.setText("White to move");
        }

        return true;
    }
}
