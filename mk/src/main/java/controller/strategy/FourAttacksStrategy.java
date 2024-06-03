package controller.strategy;

import model.Action;

/**
 * 4 атаки
 */
public class FourAttacksStrategy implements EnemyBehaviorStrategy {

    @Override
    public Action getNextAction(int behaviorStep) {
        return Action.ATTACK;
    }
}
