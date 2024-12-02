package com.tenzen.javachess;

public class Position {
    private int rank;
    private int file;

    public Position(int rank, int file) {
        setFile(file);
        setRank(rank);
    }

    public int getRank() {
        return rank;
    }

    public void setFile(int file) {
        verifyRange(file);
        this.file = file;
    }

    public int getFile() {
        return file;
    }

    public void setRank(int rank) {
        verifyRange(rank);
        this.rank = rank;
    }

    private void verifyRange(int x) {
        if (rank < 0 || rank > 7) {
            throw new OutOfBoundsException();
        }
    }

    @Override
    public String toString() {
        return String.format("Position[rank=%d, file=%d]", rank, file);
    }

    public static class OutOfBoundsException extends RuntimeException {}
}
