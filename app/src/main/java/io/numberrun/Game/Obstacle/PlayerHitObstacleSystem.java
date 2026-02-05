package io.numberrun.Game.Obstacle;

import java.util.List;

import io.numberrun.Core.SoundManager;
import io.numberrun.Game.Effect.DamageEffectSystem;
import io.numberrun.Game.Lane.LaneSize;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Player.PlayerState;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class PlayerHitObstacleSystem implements GameSystem {

    private final int windowWidth;
    private final int windowHeight;

    public PlayerHitObstacleSystem(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> players = world.query(PlayerState.class, LaneTransform.class);
        if (players.isEmpty()) {
            return;
        }

        Entity player = players.get(0);
        PlayerState ps = player.getComponent(PlayerState.class).get();
        LaneTransform pT = player.getComponent(LaneTransform.class).get();
        float px = pT.getLaneX();
        float py = pT.getLaneY();

        List<Entity> obstacles = world.query(Obstacle.class, LaneTransform.class, LaneSize.class);

        for (Entity o : obstacles) {
            LaneTransform oT = o.getComponent(LaneTransform.class).get();
            LaneSize oS = o.getComponent(LaneSize.class).get();

            float ox = oT.getLaneX();
            float oy = oT.getLaneY();
            float w = oS.getWidth();

            // X範囲判定（壁と同じ考え方）
            boolean passX = (px + 0.01f >= ox - w / 2) && (px - 0.01f <= ox + w / 2);
            if (!passX) {
                continue;
            }

            // “通過”判定（壁と同じ）
            float vy = o.getComponent(LaneVelocity.class)
                    .map(LaneVelocity::getVy)
                    .orElse(0f);

            float prevOY = oy - vy * deltaTime;

            boolean crossed = (vy != 0f)
                    ? (prevOY < py) && (oy >= py)
                    : (oy >= py);

            if (!crossed) {
                continue;
            }

            // 効果：半減
            ps.setNumber(ps.getNumber() / 2);

            DamageEffectSystem.spawnDamageEffect(world, windowWidth, windowHeight);
            SoundManager.play("/sounds/crash.wav");

            o.destroy();
            break;
        }
    }
}
