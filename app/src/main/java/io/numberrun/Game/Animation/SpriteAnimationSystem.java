package io.numberrun.Game.Animation;

import java.util.List;
import java.util.Optional;

import io.numberrun.Component.Sprite;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
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
        // SceneState を取得
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) {
            return;
        }
        // Game Over なら停止
        SceneType sceneType = scenes.get(0).getComponent(SceneState.class).map(SceneState::getCurrentScene).orElse(SceneType.GAMEPLAY);
        if (sceneType == SceneType.GAME_OVER) {
            return;
        }

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
