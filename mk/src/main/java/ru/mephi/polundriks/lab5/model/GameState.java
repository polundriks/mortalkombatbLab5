package ru.mephi.polundriks.lab5.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameState {
    private Player player;
    private Enemy currentEnemy;
    private int level;
    private int score;
    private boolean isGameOver;
    @Setter
    @Getter
    private int defeatedEnemies;
    @Getter
    private int maxEnemies;

    public GameState() {
        player = new Player();
        level = 1;
        score = 0;
        isGameOver = false;
        defeatedEnemies = 0;
        maxEnemies = getMaxEnemiesForLevel(level);
        generateEnemy();
    }

    public void nextLevel() {
        level++;
        defeatedEnemies = 0;
        maxEnemies = getMaxEnemiesForLevel(level);
        generateEnemy();
    }

    private int getMaxEnemiesForLevel(int level) {
        return switch (level) {
            case 1 -> 2;
            case 2 -> 4;
            case 3 -> 7;
            case 4 -> 9;
            case 5 -> 12;
            default -> 15; // На последних уровнях количество противников не увеличивается
        };
    }

    public void generateEnemy() {
        EnemyCharacter[] enemyTypes = EnemyCharacter.values();
        EnemyCharacter type = enemyTypes[(int) (Math.random() * enemyTypes.length)];
        currentEnemy = new Enemy(type, level);
    }
}
