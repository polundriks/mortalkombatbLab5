package mephi.lab5.model;

import lombok.Getter;

@Getter
public enum EnemyType {
    TANK("Танк"),
    WIZARD("Маг"),
    WARRIOR("Боец"),
    SOLDIER("Солдат"),
    BOSS("Босс");

    private final String str;

    EnemyType(String str) {
        this.str = str;
    }
}
