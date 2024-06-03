package controller.strategy;

import model.Action;

public interface EnemyBehaviorStrategy {

    Action getNextAction(int behaviorStep);
}
