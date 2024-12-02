package com.tenzen.javachess;

import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

public class Game extends Group {
    public Board board = new Board();
    private List<HalfTurn> halfTurnList = new ArrayList<>();

    public Game() {
        getChildren().add(board);
        board.game = this;
    }

    public void setup() {
        board.loadFen(Board.STARTING_FEN);
        halfTurnList.clear();
    }

    public void addHalfTurn(HalfTurn halfTurn) {
        halfTurnList.add(halfTurn);
    }

    public Piece.Color turnColor() {
        return halfTurnList.size() % 2 == 0 ? Piece.Color.WHITE : Piece.Color.BLACK;
    }
}
