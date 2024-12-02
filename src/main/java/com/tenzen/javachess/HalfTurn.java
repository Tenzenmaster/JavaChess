package com.tenzen.javachess;

public class HalfTurn {
    public Piece.Kind kind;
    public Piece.Color color;
    public Position from;
    public Position to;
    public boolean capture;
    public boolean check;
    public boolean checkmate;

    @Override
    public String toString() {
        return "HalfTurn[\n"
                + "    kind: " + kind + ",\n"
                + "    color: " + color + ",\n"
                + "    from: " + from + ",\n"
                + "    to: " + to + ",\n"
                + "    capture: " + capture + ",\n"
                + "    check: " + check + ",\n"
                + "    checkmate: " + checkmate + ",\n"
                + "]";
    }
}
