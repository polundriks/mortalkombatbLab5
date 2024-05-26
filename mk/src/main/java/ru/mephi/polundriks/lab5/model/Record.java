package ru.mephi.polundriks.lab5.model;

public class Record {
    private String playerName;
    private int score;

    public Record(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}
