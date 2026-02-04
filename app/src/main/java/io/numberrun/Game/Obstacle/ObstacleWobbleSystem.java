package io.numberrun.Game.Obstacle;

import java.util.List;

import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class ObstacleWobbleSystem implements GameSystem {

    @Override
    public int getPriority() {
        // LaneMovementSystem は LOW なので、DEFAULT なら十分先に動く
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> entities = world.query(ObstacleWobble.class, LaneTransform.class);

        for (Entity e : entities) {
            ObstacleWobble wobble = e.getComponent(ObstacleWobble.class).get();
            LaneTransform lt = e.getComponent(LaneTransform.class).get();

            wobble.addTime(deltaTime);

            float x = wobble.getBaseX()
                    + wobble.getAmplitude() * (float)Math.sin(wobble.getOmega() * wobble.getT() + wobble.getPhase());

            lt.setLaneX(x); // movementLimit が効く
        }
    }
}