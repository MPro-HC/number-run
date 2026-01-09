package io.numberrun.Game;

import java.util.List;
import java.util.Optional;

import io.numberrun.Component.Transform;
import io.numberrun.Component.Velocity;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

/**
 * 速度に基づいてエンティティを移動するシステム
 */
public class MovementSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.LOW.getPriority(); // 他のシステムより後に実行
    }

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> entities = world.query(Transform.class, Velocity.class);

        for (Entity entity : entities) {
            Optional<Transform> transformOpt = entity.getComponent(Transform.class);
            Optional<Velocity> velocityOpt = entity.getComponent(Velocity.class);

            if (transformOpt.isPresent() && velocityOpt.isPresent()) {
                Transform transform = transformOpt.get();
                Velocity velocity = velocityOpt.get();

                transform.setX(transform.getX() + velocity.getVx() * deltaTime);
                transform.setY(transform.getY() + velocity.getVy() * deltaTime);
            }
        }
    }
}
