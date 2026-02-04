package io.numberrun.Game.Obstacle;

import java.util.List;

import io.numberrun.Component.Transform;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class ObstacleRotateSystem implements GameSystem {

    private static final float ROTATE_DEG_PER_SEC = 360f; // 1秒で1回転（好み）

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> obstacles = world.query(Obstacle.class, Transform.class);

        for (Entity e : obstacles) {
            Transform t = e.getComponent(Transform.class).get();
            t.setRotation(t.getRotation() + ROTATE_DEG_PER_SEC * deltaTime);
        }
    }
}
