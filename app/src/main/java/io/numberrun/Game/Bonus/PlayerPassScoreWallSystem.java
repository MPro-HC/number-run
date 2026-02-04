package io.numberrun.Game.Bonus;

import java.util.List;

import io.numberrun.Game.Goal.FinalScore;
import io.numberrun.Game.Lane.LaneSize;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Player.PlayerState;
import io.numberrun.Game.Scene.Scene;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class PlayerPassScoreWallSystem implements GameSystem {

    private static final int[] VALUES = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90};

    @Override
    public int getPriority() {
        return SystemPriority.DEFAULT.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> scenes = world.query(Scene.class, SceneState.class);
        if (scenes.isEmpty()) return;

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();
        if (sceneState.getCurrentScene() != SceneType.BONUS) return;

        List<Entity> players = world.query(PlayerState.class, LaneTransform.class);
        if (players.isEmpty()) return;

        List<Entity> bsList = world.query(BonusState.class);
        if (bsList.isEmpty()) return;

        BonusState bs = bsList.get(0).getComponent(BonusState.class).get();

        Entity player = players.get(0);
        PlayerState ps = player.getComponent(PlayerState.class).get();
        LaneTransform pT = player.getComponent(LaneTransform.class).get();

        float px = pT.getLaneX();
        float py = pT.getLaneY();

        // スコア壁（Wall + ScoreWall）を探す
        List<Entity> scoreWalls = world.query(ScoreWall.class, LaneTransform.class, LaneSize.class, LaneVelocity.class);
        for (Entity wEnt : scoreWalls) {
            ScoreWall sw = wEnt.getComponent(ScoreWall.class).get();
            LaneTransform wT = wEnt.getComponent(LaneTransform.class).get();
            LaneSize wS = wEnt.getComponent(LaneSize.class).get();
            LaneVelocity wV = wEnt.getComponent(LaneVelocity.class).get();

            float wx = wT.getLaneX();
            float wy = wT.getLaneY();
            float halfW = wS.getWidth() / 2f;

            // X範囲
            boolean passX = (px >= wx - halfW) && (px <= wx + halfW);
            if (!passX) continue;

            // 壁と同じ「跨いだ」判定
            float prevWY = wy - wV.getVy() * deltaTime;
            boolean crossed = (prevWY < py) && (wy >= py);
            if (!crossed) continue;

            int value = sw.getValue();

            // まず減らす（0になったかを先に判定）
            int before = bs.currentNumber;
            int after = Math.max(0, before - value);
            bs.currentNumber = after;

            // 演出：プレイヤー表示も更新
            ps.setNumber(bs.currentNumber);

            // 0にならなかったときだけスコア加算（＝0になった瞬間の壁は得点なし）
            if (after > 0) {
                bs.totalScore += value;
            }

            // 次へ
            bs.clearedCount++;

            // 今の壁を消す
            wEnt.destroy();

            // 終了条件：最後まで or 0になったら RESULT
            boolean finishedByWalls = (bs.clearedCount >= VALUES.length);
            boolean finishedByZero  = (after <= 0);

            if (finishedByWalls || finishedByZero) {
                if (world.query(io.numberrun.Game.Goal.FinalScore.class).isEmpty()) {
                    world.spawn(new io.numberrun.Game.Goal.FinalScore(bs.totalScore, bs.currentNumber));
                }

                // HUDを出しているなら消す
                for (Entity e : world.query(io.numberrun.Game.Bonus.BonusHUD.class)) e.destroy();
                for (Entity e : world.query(io.numberrun.Game.Bonus.ScoreWall.class)) e.destroy();

                // BonusState を消して持ち越さない
                bsList.get(0).destroy();

                sceneState.setCurrentScene(io.numberrun.Game.Scene.SceneType.RESULT);
            }

            break;
        }
    }
}