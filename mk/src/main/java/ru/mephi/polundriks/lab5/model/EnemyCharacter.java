package ru.mephi.polundriks.lab5.model;

import lombok.Getter;

import static ru.mephi.polundriks.lab5.model.EnemyType.*;

@Getter
public enum EnemyCharacter {
    BARAKA(TANK),
    LIU_KANG(WARRIOR),
    SONYA_BLADE(SOLDIER),
    SUB_ZERO(WIZARD),
    SHAO_KAHN(BOSS);

    private final EnemyType type;

    EnemyCharacter(EnemyType type) {
        this.type = type;
    }
}
