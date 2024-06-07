package model;

import controller.strategy.DefendAttackDefendStrategy;
import controller.strategy.EnemyBehaviorStrategy;
import controller.strategy.FourAttacksStrategy;
import controller.strategy.RandomAttackDefendStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
@Getter
@Setter
public class Enemy {

    private int health;
    private int attackDamage;
    private BufferedImage imageBuffer;
    private EnemyCharacter type;
    private EnemyBehaviorStrategy behaviorStrategy;
    private int behaviorStep;
    private boolean defending;
    private int weakenedTurns;
    private int maxHealth;

    public Enemy(EnemyCharacter type, int playerLevel) {
        String imagePath = ConfigManager.getStringProperty(type.name().toLowerCase(), "imagePath");
        try {
            imageBuffer = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.type = type;
        this.health = calculateHealth(type, playerLevel);
        this.attackDamage = calculateAttackDamage(type, playerLevel);
        determineBehavior(type);
        this.behaviorStep = 0;
        this.defending = false;
    }

    private int calculateHealth(EnemyCharacter type, int playerLevel) {
        maxHealth = ConfigManager.getIntProperty(type.name().toLowerCase(), "initialHealth");
        int levelUpHealthIncrement = ConfigManager.getIntProperty(type.name().toLowerCase(), "levelUpHealthIncrement");
        return maxHealth + (playerLevel * levelUpHealthIncrement);
    }

    private int calculateAttackDamage(EnemyCharacter type, int playerLevel) {
        int baseAttackDamage = ConfigManager.getIntProperty(type.name().toLowerCase(), "initialAttackDamage");
        int levelUpAttackDamageIncrement = ConfigManager.getIntProperty(type.name().toLowerCase(), "levelUpAttackDamageIncrement");
        return baseAttackDamage + (playerLevel * levelUpAttackDamageIncrement);
    }

    private void determineBehavior(EnemyCharacter type) {
        double random = Math.random();
        switch (type) {
            case BARAKA -> {
                // Танк
                if (random < 0.3) {
                    behaviorStrategy = new RandomAttackDefendStrategy();
                } else if (random < 0.9) {
                    behaviorStrategy = new DefendAttackDefendStrategy();
                } else {
                    behaviorStrategy = new FourAttacksStrategy();
                }
            }
            case SUB_ZERO -> // Маг
                behaviorStrategy = (random < 0.5) ? new RandomAttackDefendStrategy() : new FourAttacksStrategy();
            case LIU_KANG -> {
                // Боец
                if (random < 0.25) {
                    behaviorStrategy = new RandomAttackDefendStrategy();
                } else if (random < 0.35) {
                    behaviorStrategy = new DefendAttackDefendStrategy();
                } else {
                    behaviorStrategy = new FourAttacksStrategy();
                }
            }
            case SONYA_BLADE -> // Солдат
                behaviorStrategy = (random < 0.5) ? new RandomAttackDefendStrategy() : new DefendAttackDefendStrategy();
            case SHAO_KAHN -> // Босс
                behaviorStrategy = new FourAttacksStrategy();
        }
    }

    public Action getNextAction() {
        Action action = behaviorStrategy.getNextAction(behaviorStep);
        behaviorStep = (behaviorStep + 1) % 4;
        return action;
    }

    public void setHealth(int health) {
        if (health <= 0) {
            log.info("Victory of Player");
        }
        this.health = health;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Enemy.class.getSimpleName() + "[", "]")
                .add("attackDamage=" + attackDamage)
                .add("type=" + type)
                .add("maxHealth=" + maxHealth)
                .toString();
    }
}
