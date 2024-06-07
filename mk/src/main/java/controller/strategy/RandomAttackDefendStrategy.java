package controller.strategy;

import model.Action;

/**
 * Случайно 1 или 2 атаки – защита
 */
public class RandomAttackDefendStrategy implements EnemyBehaviorStrategy {

    @Override
    public Action getNextAction(int behaviorStep) {
        if (behaviorStep < 2) {
            if (Math.random() < 0.5) {
                return Action.ATTACK;
            } else {
                return Action.DEFEND;
            }
        } else {
            return Action.DEFEND;
        }
    }
}

