package model;

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
    private EnemyBehavior behavior;
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
        this.behavior = determineBehavior(type);
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

    private EnemyBehavior determineBehavior(EnemyCharacter type) {
        double random = Math.random();
        switch (type) {
            case BARAKA: // Танк
                if (random < 0.3) return EnemyBehavior.TYPE_1;
                if (random < 0.9) return EnemyBehavior.TYPE_2;
                return EnemyBehavior.TYPE_3;
            case SUB_ZERO: // Маг
                return random < 0.5 ? EnemyBehavior.TYPE_1 : EnemyBehavior.TYPE_3;
            case LIU_KANG: // Боец
                if (random < 0.25) return EnemyBehavior.TYPE_1;
                if (random < 0.35) return EnemyBehavior.TYPE_2;
                return EnemyBehavior.TYPE_3;
            case SONYA_BLADE: // Солдат
                return random < 0.5 ? EnemyBehavior.TYPE_1 : EnemyBehavior.TYPE_2;
            case SHAO_KAHN: // Босс
                return EnemyBehavior.TYPE_3;
            default:
                return EnemyBehavior.TYPE_1;
        }
    }

    public Action getNextAction() {
        switch (behavior) {
            case TYPE_1:
                if (behaviorStep < 2) {
                    behaviorStep++;
                    return Math.random() < 0.5 ? Action.ATTACK : Action.DEFEND;
                } else {
                    behaviorStep = 0;
                    return Action.DEFEND;
                }
            case TYPE_2:
                if (behaviorStep == 0 || behaviorStep == 2) {
                    behaviorStep++;
                    return Action.DEFEND;
                } else {
                    behaviorStep++;
                    return Action.ATTACK;
                }
            case TYPE_3:
                behaviorStep = (behaviorStep + 1) % 4;
                return Action.ATTACK;
            default:
                return Action.ATTACK;
        }
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
