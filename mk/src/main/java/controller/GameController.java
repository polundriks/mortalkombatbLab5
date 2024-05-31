package controller;

import model.RecordTable;
import model.ItemType;
import model.EnemyType;
import model.Player;
import model.Item;
import model.Action;
import model.Enemy;
import model.GameState;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.Record;

import java.util.Random;

/**
 * Класс GameController управляет основной логикой игры.
 * Он отвечает за управление состоянием игры, взаимодействие с игроком и противниками.
 */
@Slf4j
public class GameController {
    @Getter
    private GameState gameState;
    @Getter
    private IOController ioController;
    @Getter
    @Setter
    private RecordTable recordTable;
    private boolean playerTurn;
    private boolean playerStunned;
    private boolean enemyStunned;
    private boolean playerDefending;
    private Runnable levelUpChooseCallback;

    /**
     * Конструктор GameController инициализирует новую игру.
     */
    public GameController(Runnable levelUpChooseCallback) {
        gameState = new GameState();
        ioController = new IOController();
        playerTurn = true; // Игрок начинает первым
        playerStunned = false;
        enemyStunned = false;
        playerDefending = false;
        recordTable = new RecordTable();
        this.levelUpChooseCallback = levelUpChooseCallback;
    }

    /**
     * Метод startNewGame начинает новую игру с заданным количеством локаций.
     *
     * @param totalLocations общее количество локаций в игре
     */
    public void startNewGame(int totalLocations) {
        gameState = new GameState();
        gameState.setTotalLocations(totalLocations);
        gameState.setCurrentLocation(1);
        playerTurn = true; // Игрок начинает первым
        playerStunned = false;
        enemyStunned = false;
        playerDefending = false;
        gameState.generateEnemy();
    }

    /**
     * Метод moveToNextLocation перемещает игрока на следующую локацию.
     */
    public void moveToNextLocation() {
        if (gameState.getCurrentLocation() < gameState.getTotalLocations()) {
            gameState.setCurrentLocation(gameState.getCurrentLocation() + 1);
            gameState.nextLocation();
            log.info("Go to location {}", gameState.getCurrentLocation());
        } else {
            // Игра окончена, игрок прошел все локации
            gameState.setGameOver(true);
        }
    }

    /**
     * Метод playerAttack выполняет атаку игрока.
     */
    public void playerAttack() {
        log.info("The player attacks with force {}\n", gameState.getPlayer().getAttackDamage());
        if (playerTurn) {
            if (playerStunned) {
                skipTurn();
                log.info("Player is stunned and misses a turn");
                return;
            }
            Player player = gameState.getPlayer();
            Enemy enemy = gameState.getCurrentEnemy();
            if (enemy.isDefending()) {
                counterAttack(player, enemy);
            } else if (enemy.getNextAction() == Action.REGENERATE) {
                interruptRegeneration(player, enemy);
                log.info("The enemy tries to regenerate, the player interrupts the process");
            } else {
                attackEnemy(player, enemy);
                log.info("The enemy is attacked");
            }
            dropItem();
            playerTurn = false;
            enemyRespond();
        }
    }

    private void skipTurn() {
        playerStunned = false;
        playerTurn = false;
        enemyRespond();
    }

    private void counterAttack(Player player, Enemy enemy) {
        log.info("The enemy counterattacks with damage {}", enemy.getAttackDamage() / 2);
        player.setHealth(player.getHealth() - (enemy.getAttackDamage() / 2));
        if (player.getHealth() <= 0) {
            moveToNextLocation();
        }
        enemy.setDefending(false);
    }

    private void interruptRegeneration(Player player, Enemy enemy) {
        enemy.setHealth(enemy.getHealth() - (player.getAttackDamage() * 2));
        checkEnemyHealth(enemy);
    }

    private void attackEnemy(Player player, Enemy enemy) {
        int damage = player.getAttackDamage();
        if (enemy.getWeakenedTurns() > 0) {
            damage = (int) (damage * 1.25);
            enemy.setWeakenedTurns(enemy.getWeakenedTurns() - 1);
            enemy.setAttackDamage(enemy.getAttackDamage() / 2);
        }
        enemy.setHealth(enemy.getHealth() - damage);
        checkEnemyHealth(enemy);
    }

    private void checkEnemyHealth(Enemy enemy) {
        if (enemy.getHealth() <= 0) {
            gameState.setScore(gameState.getScore() + 100); 
            gameState.setDefeatedEnemies(gameState.getDefeatedEnemies() + 1);

            if (gameState.getDefeatedEnemies() >= gameState.getMaxEnemies()) {
                levelUpChooseCallback.run();
                gameState.nextLevel();
            } else {
                gameState.generateEnemy();
            }
        }
    }

    /**
     * Метод playerDefend выполняет защиту игрока.
     */
    public void playerDefend() {
        log.info("Player defends");
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
            if (gameState.getCurrentEnemy().getNextAction() == Action.REGENERATE) {
                // Если противник пытается регенерировать и игрок защищается, босс восстанавливает 50% от полученного на текущий момент урона
                Enemy enemy = gameState.getCurrentEnemy();
                enemy.setHealth((int) (enemy.getHealth() + (enemy.getMaxHealth() - enemy.getHealth()) * 0.5));
            }
            playerTurn = false;
            enemyRespond();
        }
    }

    /**
     * Метод playerSkip пропускает ход игрока.
     */
    public void playerSkip() {
        log.info("The player misses a turn");
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

    /**
     * Метод increasePlayerDamage увеличивает урон игрока.
     */
    public void increasePlayerDamage() {
        gameState.getPlayer().increasePlayerDamage();
        log.info("The player chose to increase attack: {} when moving to a level: {}",
            gameState.getPlayer().getAttackDamage(), gameState.getPlayer().getLevel());
    }

    /**
     * Метод increasePlayerHealth увеличивает здоровье игрока.
     */
    public void increasePlayerHealth() {
        gameState.getPlayer().increasePlayerMaxHealth();
        log.info("The player chose to increase health: {} when moving to a level: {}",
            gameState.getPlayer().getMaxHealth(), gameState.getPlayer().getLevel());
    }
    
    public void playerWeaken() {
        if (playerTurn) {
            log.info("The player is trying to weaken the enemy");
            if (playerStunned) {
                // Игрок оглушен, пропускает ход
                playerStunned = false;
                playerTurn = false;
                enemyRespond();
                log.info("The player was unable to weaken the enemy because he was stunned");
                return;
            }

            Player player = gameState.getPlayer();
            Enemy enemy = gameState.getCurrentEnemy();
            if (enemy.isDefending()) {
                // Если противник защищается, есть 75% шанс нанести ему дебаф
                if (new Random().nextInt(100) < 75) {
                    enemy.setWeakenedTurns(player.getLevel());
                    //enemy.setAttackDamage(enemy.getAttackDamage() / 2);
                    //player.setAttackDamage((int) (player.getAttackDamage() * 1.25));
                    log.info("The enemy is weakened by 50%, the player is strengthened by 25%");
                }
            } else {
                // Если противник атакует, ослабление срывается, а игрок получает дополнительный урон
                player.setHealth((int) (player.getHealth() - enemy.getAttackDamage() * 1.15));
                log.info("The player received additional damage {}", enemy.getAttackDamage() * 1.15);
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
                log.info("Enemy's move");
                enemyTurn();
            } else {
                enemyStunned = false;
                playerTurn = true;
                log.info("The enemy is stunned and misses a turn");
            }
        }
    }

    private void enemyTurn() {
        if (!playerTurn) {
            Player player = gameState.getPlayer();
            Enemy enemy = gameState.getCurrentEnemy();
            Action enemyAction = enemy.getNextAction();

            log.info("The enemy has chosen an action: {}\n", enemyAction);

            switch (enemyAction) {
                case ATTACK:
                    if (playerDefending) {
                        // Игрок защищается, атака противника не происходит
                        playerDefending = false;
                        log.info("The player defends, the enemy misses a move");
                    } else {
                        // Игрок не защищается, противник атакует
                        player.setHealth(player.getHealth() - enemy.getAttackDamage());
                        log.info("Player is attacked");
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
                            log.info("Player is stunned");
                        }
                    }
                    break;
                case WEAKEN:
                
                    if (enemy.getType().getType() != EnemyType.WIZARD) {
                        log.info("The enemy cannot weaken the player (Action not available for {})\n",
                            enemy.getType().getType().getStr());
                        break;
                    }
                    if (playerDefending) {
                        // Если игрок защищается, ослабление срывается, а ослабитель получает дополнительный урон
                        enemy.setHealth((int) (enemy.getHealth() - player.getAttackDamage() * 1.15));
                        log.info("The enemy received additional damage for trying to weaken the player");
                        if (enemy.getHealth() <= 0) {
                            gameState.setScore(gameState.getScore() + 100); 
                            gameState.setDefeatedEnemies(gameState.getDefeatedEnemies() + 1);

                            if (gameState.getDefeatedEnemies() >= gameState.getMaxEnemies()) {
                                levelUpChooseCallback.run();
                                gameState.nextLevel();
                            } else {
                                gameState.generateEnemy();
                            }
                        }
                    } else {
                        // Если игрок атакует, есть 75% шанс нанести ему дебаф
                        if (new Random().nextInt(100) < 75) {
                            player.setWeakenedTurns(1);
                            //player.setAttackDamage(player.getAttackDamage() / 2);
                            //enemy.setAttackDamage((int) (enemy.getAttackDamage() * 1.25));
                            log.info("The enemy has weakened the player");
                        }
                    }
                    break;
                case REGENERATE:
                    // Босс пытается регенерировать здоровье
                    if (playerDefending) {
                        // Если игрок защищается, босс восстанавливает 50% от полученного на текущий момент урона
                        enemy.setHealth((int) (enemy.getHealth() + (enemy.getMaxHealth() - enemy.getHealth()) * 0.5));
                        log.info("The boss regenerates health by 50% of the damage currently received");
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
            log.info("The player receives an item: {}", item.getType().getName());
        }
    }

    /**
     * Метод useItem использует выбранный предмет.
     *
     * @param item предмет, который нужно использовать
     */
    public void useItem(Item item) {
        Player player = gameState.getPlayer();
        switch (item.getType()) {
            case SMALL_HEALTH_POTION, LARGE_HEALTH_POTION:
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
        log.info("The player used the item '{}'", item);
    }

    /**
     * Метод addRecord добавляет новую запись в таблицу рекордов.
     *
     * @param playerName имя игрока
     * @param score      количество очков
     */
    public void addRecord(String playerName, int score) {
        recordTable.addRecord(new Record(playerName, score));
        ioController.saveRecordTable(recordTable);
    }
}
