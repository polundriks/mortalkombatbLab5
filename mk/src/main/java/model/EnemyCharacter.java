package model;

import lombok.Getter;

import static model.EnemyType.*;

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
