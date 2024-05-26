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

    public void startNewGame() {
        gameState = new GameState();
        playerTurn = true; // Игрок начинает первым
        playerStunned = false;
        enemyStunned = false;
        playerDefending = false;
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
                    gameState.setGameOver(true);
                }
                enemy.setDefending(false);
            } else {
                // Противник не защищается, игрок атакует
                enemy.setHealth(enemy.getHealth() - player.getAttackDamage());
                if (enemy.getHealth() <= 0) {
                    gameState.setScore(gameState.getScore() + 100); // За победу над противником начисляются очки
                    gameState.setDefeatedEnemies(gameState.getDefeatedEnemies() + 1);
                    dropItem();
                    if (gameState.getDefeatedEnemies() >= gameState.getMaxEnemies()) {
                        gameState.nextLevel();
                    } else {
                        gameState.generateEnemy();
                    }
                }
            }
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
                            gameState.setGameOver(true);
                        }
                    }
                    break;
                case DEFEND:
                    enemy.setDefending(true);
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
    }

    public void addRecord(String playerName, int score) {
        recordTable.addRecord(new Record(playerName, score));
        ioController.saveRecordTable(recordTable);
    }
}
