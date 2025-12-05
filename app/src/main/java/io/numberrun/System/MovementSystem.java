package io.numberrun.System;

import io.numberrun.Component.Transform;
import io.numberrun.Component.Velocity;
import io.numberrun.Game.Entity;
import io.numberrun.Game.GameSystem;
import io.numberrun.Game.World;

import java.util.List;
import java.util.Optional;

/**
 * 速度に基づいてエンティティを移動するシステム
 */
public class MovementSystem implements GameSystem {

    @Override
    public int getPriority() {
        return 100; // 他のシステムより後に実行
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
