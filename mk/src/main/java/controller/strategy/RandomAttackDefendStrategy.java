package controller.strategy;

import model.Action;

/**
 * Случайно 1 или 2 атаки – защита
 */
public class RandomAttackDefendStrategy implements EnemyBehaviorStrategy {

    @Override
    public Action getNextAction(int behaviorStep) {
        if (behaviorStep < 2) {
            return Math.random() < 0.5 ? Action.ATTACK : Action.DEFEND;
        } else {
            return Action.DEFEND;
        }
    }
}
