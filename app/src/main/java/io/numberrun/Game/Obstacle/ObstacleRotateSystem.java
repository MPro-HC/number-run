package io.numberrun.Game.Obstacle;

import java.util.List;

import io.numberrun.Component.Transform;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class ObstacleRotateSystem implements GameSystem {

    private static final float ROTATE_DEG_PER_SEC = 360f * 5; // 1秒で回転する角度

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

            // 子のホイールは逆回転させて、回転しないようにする
            for (Entity child : e.getChildren()) {
                Transform ct = child.getComponent(Transform.class).get();
                ct.setRotation(ct.getRotation() - ROTATE_DEG_PER_SEC * deltaTime);
            }
        }
    }
}
