package controller.strategy;

import model.Action;

/**
 * Защита – атака – защита
 */
public class DefendAttackDefendStrategy implements EnemyBehaviorStrategy {

    @Override
    public Action getNextAction(int behaviorStep) {
        if (behaviorStep == 0 || behaviorStep == 2) {
            return Action.DEFEND;
        } else {
            return Action.ATTACK;
        }
    }
}
