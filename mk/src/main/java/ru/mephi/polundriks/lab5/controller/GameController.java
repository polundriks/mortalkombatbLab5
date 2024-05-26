package ru.mephi.polundriks.lab5.controller;

import lombok.Getter;
import ru.mephi.polundriks.lab5.model.Record;
import ru.mephi.polundriks.lab5.model.*;

import java.util.Random;

public class GameController {
    @Getter
    private GameState gameState;
    private IOController ioController;
    @Getter
    private RecordTable recordTable;
    private boolean playerTurn;
    private boolean playerStunned;
    private boolean enemyStunned;
    private boolean playerDefending;

    public GameController() {
        gameState = new GameState();
        ioController = new IOController();
        playerTurn = true; // Игрок начинает первым
        playerStunned = false;
        enemyStunned = false;
        playerDefending = false;
        recordTable = new RecordTable(); // todo ioController.loadRecordTable();
    }

    public void startNewGame(int totalLocations) {
        gameState = new GameState();
        gameState.setTotalLocations(totalLocations);
        gameState.setCurrentLocation(1);
        playerTurn = true; // Игрок начинает первым
        playerStunned = false;
        enemyStunned = false;
        playerDefending = false;
    }

    public void moveToNextLocation() {
        if (gameState.getCurrentLocation() < gameState.getTotalLocations()) {
            gameState.setCurrentLocation(gameState.getCurrentLocation() + 1);
        } else {
            // Игра окончена, игрок прошел все локации
            gameState.setGameOver(true);
        }
    }

    public void playerAttack() {
        if (playerTurn) {
            if (playerStunned) {
                // Игрок оглушен, пропускает ход
                playerStunned = false;
                playerTurn = false;
                enemyRespond();
                return;
            }

            Player player = gameState.getPlayer();
            Enemy enemy = gameState.getCurrentEnemy();
            if (enemy.isDefending()) {
                // Противник защищается, контрудар на 50% урона
                player.setHealth(player.getHealth() - (enemy.getAttackDamage() / 2));
                if (player.getHealth() <= 0) {
                    moveToNextLocation();
                }
                enemy.setDefending(false);
            } else {
                // Противник не защищается, игрок атакует
                int damage = player.getAttackDamage();
                if (enemy.getWeakenedTurns() > 0) {
                    damage = (int) (damage * 1.25);
                    enemy.setWeakenedTurns(enemy.getWeakenedTurns() - 1);
                    if (enemy.getWeakenedTurns() == 0) {
                        enemy.setAttackDamage(enemy.getAttackDamage() * 2);
                    }
                }
                enemy.setHealth(enemy.getHealth() - damage);
                if (enemy.getHealth() <= 0) {
                    gameState.setScore(gameState.getScore() + 100); // За победу над противником начисляются очки
                    gameState.setDefeatedEnemies(gameState.getDefeatedEnemies() + 1);

                    if (gameState.getDefeatedEnemies() >= gameState.getMaxEnemies()) {
                        gameState.nextLevel();
                    } else {
                        gameState.generateEnemy();
                    }
                }
            }
            dropItem();
            playerTurn = false;
            enemyRespond();
        }
    }

    public void playerDefend() {
        if (playerTurn) {
            if (playerStunned) {
                // Игрок оглушен, пропускает ход
                playerStunned = false;
                playerTurn = false;
                enemyRespond();
                return;
            }

            playerDefending = true;
            if (gameState.getCurrentEnemy().isDefending()) {
                // Если противник также защищается, есть 50% шанс оглушить его
                if (new Random().nextBoolean()) {
                    enemyStunned = true;
                }
            }
            playerTurn = false;
            enemyRespond();
        }
    }

    public void playerSkip() {
        if (playerTurn) {
            if (playerStunned) {
                // Игрок оглушен, пропускает ход
                playerStunned = false;
                playerTurn = false;
                enemyRespond();
                return;
            }

            playerTurn = false;
            enemyRespond();
        }
    }

    public void increasePlayerDamage() {
        gameState.getPlayer().increasePlayerDamage();
    }

    public void increasePlayerHealth() {
        gameState.getPlayer().increasePlayerMaxHealth();
    }

    public void playerWeaken() {
        if (playerTurn) {
            if (playerStunned) {
                // Игрок оглушен, пропускает ход
                playerStunned = false;
                playerTurn = false;
                enemyRespond();
                return;
            }

            Player player = gameState.getPlayer();
            Enemy enemy = gameState.getCurrentEnemy();
            if (enemy.isDefending()) {
                // Если противник защищается, есть 75% шанс нанести ему дебаф
                if (new Random().nextInt(100) < 75) {
                    enemy.setWeakenedTurns(player.getLevel());
                    enemy.setAttackDamage(enemy.getAttackDamage() / 2);
                    player.setAttackDamage((int) (player.getAttackDamage() * 1.25));
                }
            } else {
                // Если противник атакует, ослабление срывается, а ослабитель получает дополнительный урон
                player.setHealth((int) (player.getHealth() - enemy.getAttackDamage() * 1.15));
                if (player.getHealth() <= 0) {
                    moveToNextLocation();
                }
            }
            playerTurn = false;
            enemyRespond();
        }
    }

    private void enemyRespond() {
        if (!playerTurn) {
            if (!enemyStunned) {
                enemyTurn();
            } else {
                enemyStunned = false;
                playerTurn = true;
            }
        }
    }

    private void enemyTurn() {
        if (!playerTurn) {
            if (enemyStunned) {
                // Противник оглушен, пропускает ход
                enemyStunned = false;
                playerTurn = true;
                return;
            }

            Player player = gameState.getPlayer();
            Enemy enemy = gameState.getCurrentEnemy();
            Action enemyAction = enemy.getNextAction();

            switch (enemyAction) {
                case ATTACK:
                    if (playerDefending) {
                        // Игрок защищается, атака противника не происходит
                        playerDefending = false;
                    } else {
                        // Игрок не защищается, противник атакует
                        player.setHealth(player.getHealth() - enemy.getAttackDamage());
                        if (player.getHealth() <= 0) {
                            moveToNextLocation();
                        }
                    }
                    break;
                case DEFEND:
                    enemy.setDefending(true);
                    if (playerDefending) {
                        // Если игрок также защищается, есть 50% шанс оглушить его
                        if (new Random().nextBoolean()) {
                            playerStunned = true;
                        }
                    }
                    break;

                case WEAKEN:
                    // свойство ослабления только у мага и игрока
                    if (enemy.getType().getType() != EnemyType.WIZARD) {
                        break;
                    }
                    if (playerDefending) {
                        // Если игрок защищается, ослабление срывается, а ослабитель получает дополнительный урон
                        enemy.setHealth((int) (enemy.getHealth() - player.getAttackDamage() * 1.15));
                        if (enemy.getHealth() <= 0) {
                            gameState.setScore(gameState.getScore() + 100); // За победу над противником начисляются очки
                            gameState.setDefeatedEnemies(gameState.getDefeatedEnemies() + 1);

                            if (gameState.getDefeatedEnemies() >= gameState.getMaxEnemies()) {
                                gameState.nextLevel();
                            } else {
                                gameState.generateEnemy();
                            }
                        }
                    } else {
                        // Если игрок атакует, есть 75% шанс нанести ему дебаф
                        if (new Random().nextInt(100) < 75) {
                            player.setWeakenedTurns(1);
                            player.setAttackDamage(player.getAttackDamage() / 2);
                            enemy.setAttackDamage((int) (enemy.getAttackDamage() * 1.25));
                        }
                    }
                    break;
            }

            playerTurn = true;
        }
    }

    private void dropItem() {
        Random random = new Random();
        int chance = random.nextInt(100);
        Item item = null;
        if (chance < 25) {
            item = new Item(ItemType.SMALL_HEALTH_POTION);
        } else if (chance < 40) {
            item = new Item(ItemType.LARGE_HEALTH_POTION);
        } else if (chance < 45) {
            item = new Item(ItemType.RESURRECTION_CROSS);
        }
        if (item != null) {
            gameState.getPlayer().addItem(item);
        }
    }

    public void useItem(Item item) {
        Player player = gameState.getPlayer();
        switch (item.getType()) {
            case SMALL_HEALTH_POTION:
                player.setHealth(player.getHealth() + item.getEffect());
                break;
            case LARGE_HEALTH_POTION:
                player.setHealth(player.getHealth() + item.getEffect());
                break;
            case RESURRECTION_CROSS:
                if (player.getHealth() <= 0) {
                    player.setHealth(item.getEffect());
                    gameState.setGameOver(false);
                }
                break;
        }
        player.removeItem(item);
    }

    public void addRecord(String playerName, int score) {
        recordTable.addRecord(new Record(playerName, score));
        ioController.saveRecordTable(recordTable);
    }
}
