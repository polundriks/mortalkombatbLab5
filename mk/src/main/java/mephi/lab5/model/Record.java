package mephi.lab5.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Record {
    private String playerName;
    private int score;

    public Record(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }
}
