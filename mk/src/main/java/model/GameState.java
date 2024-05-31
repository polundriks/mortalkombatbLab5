package model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    @Setter
    @Getter
    private int totalLocations;
    @Setter
    @Getter
    private int currentLocation;

    public GameState() {
        player = new Player();
        level = 1;
        score = 0;
        isGameOver = false;
        defeatedEnemies = 0;
        maxEnemies = getMaxEnemiesForLevel(level);
        //generateEnemy();
    }

    public void nextLocation() {
        player.spawnInNewLocation();
        generateEnemy();
    }

    public void nextLevel() {
        level++;
        defeatedEnemies = 0;
        player.spawnInNewLocation();
        maxEnemies = getMaxEnemiesForLevel(level);
        log.info("Игрок перешел на уровень {} с {} врагами", level, maxEnemies);
        if (level == 3 || level == 5) {
            generateBoss();
        } else {
            generateEnemy();
        }
    }

    private int getMaxEnemiesForLevel(int level) {
        return switch (level) {
            case 1 -> 2;
            case 2 -> 4;
            case 3 -> 7;
            case 4 -> 9;
            case 5 -> 12;
            default -> 15; 
        };
    }

    public void generateEnemy() {
        log.info("in generateEnemy: defeatedEnemies = {}, maxEnemies = {}", defeatedEnemies, maxEnemies);
        if (defeatedEnemies == maxEnemies - 1) {
            generateBoss();
        } else {
            EnemyCharacter[] enemyTypes = EnemyCharacter.values();
            EnemyCharacter type = enemyTypes[(int) (Math.random() * (enemyTypes.length - 1))];
            currentEnemy = new Enemy(type, level);
            log.info("Сгенерирован противник {}", currentEnemy);
        }
    }

    private void generateBoss() {
        currentEnemy = new Enemy(EnemyCharacter.SHAO_KAHN, level);
        currentEnemy.setMaxHealth(player.getHealth() / 2);
        currentEnemy.setHealth(currentEnemy.getMaxHealth());
        log.info("Сгенерирован босс {}", currentEnemy);
    }
}
