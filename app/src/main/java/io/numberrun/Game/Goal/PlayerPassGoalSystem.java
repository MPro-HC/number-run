package io.numberrun.Game.Goal;

import java.util.List;

import io.numberrun.Game.Bonus.BonusState;
import io.numberrun.Game.Scene.Scene;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.Game.Obstacle.Obstacle;
import io.numberrun.Game.Wall.Wall;
import io.numberrun.Game.Lane.LaneSize;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Player.PlayerState;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class PlayerPassGoalSystem implements GameSystem {

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> players = world.query(PlayerState.class, LaneTransform.class);
        if (players.isEmpty()) return;

        Entity player = players.get(0);
        PlayerState ps = player.getComponent(PlayerState.class).get();
        LaneTransform pT = player.getComponent(LaneTransform.class).get();

        float px = pT.getLaneX();
        float py = pT.getLaneY();

        List<Entity> goals = world.query(Goal.class, LaneTransform.class, LaneSize.class, LaneVelocity.class);
        for (Entity g : goals) {
            LaneTransform gT = g.getComponent(LaneTransform.class).get();
            LaneSize gS = g.getComponent(LaneSize.class).get();
            LaneVelocity gV = g.getComponent(LaneVelocity.class).get();

            float gx = gT.getLaneX();
            float gy = gT.getLaneY();
            float w = gS.getWidth();

            boolean passX = (px >= gx - w / 2f) && (px <= gx + w / 2f);
            if (!passX) continue;

            float prevGY = gy - gV.getVy() * deltaTime;
            boolean crossed = (prevGY < py) && (gy >= py);
            if (!crossed) continue;

            // ボーナス開始：プレイヤーを中央に強制移動
            player.getComponent(LaneTransform.class).ifPresent(t -> t.setLaneX(0f));
            
            // BONUS開始時点の数字
            int startNumber = ps.getNumber();

            // BonusState を作成（既にあれば作らない）
            if (world.query(BonusState.class).isEmpty()) {
                world.spawn(new BonusState(startNumber));
            }

            // 進行中のオブジェクトを消す（BONUS演出に入るので画面をきれいにする）
            for (Entity wallEntity : world.query(Wall.class)) wallEntity.destroy();
            for (Entity o : world.query(Obstacle.class)) o.destroy();
            g.destroy();

            player.getComponent(io.numberrun.Game.Lane.LaneTransform.class).ifPresent(t -> t.setLaneX(0f));

            // シーンをBONUSへ
            Entity sceneEntity = world.query(Scene.class, SceneState.class).get(0);
            sceneEntity.getComponent(SceneState.class).get().setCurrentScene(SceneType.BONUS);

            break;

        }
    }

}