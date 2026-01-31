package io.numberrun.Game.Animation;

import java.util.List;
import java.util.Optional;

import io.numberrun.Component.Sprite;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

/**
 * SpriteとTimerを持つエンティティのフレームアニメーションを更新するシステム Timerが1周するたびにSpriteの次のフレームに進む
 */
public class SpriteAnimationSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> entities = world.query(Sprite.class);

        for (Entity entity : entities) {
            Optional<Sprite> spriteOpt = entity.getComponent(Sprite.class);

            if (spriteOpt.isPresent()) {
                Sprite sprite = spriteOpt.get();
                sprite.tick(deltaTime * 1000); // sec -> ms
            }
        }
    }
}
