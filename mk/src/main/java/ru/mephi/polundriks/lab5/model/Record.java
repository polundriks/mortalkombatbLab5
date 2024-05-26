package ru.mephi.polundriks.lab5.model;

import lombok.Getter;

@Getter
public class Record {
    private String playerName;
    private int score;

    public Record(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }
}
