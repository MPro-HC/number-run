package io.numberrun.Game.Result;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import io.numberrun.Component.Button;
import io.numberrun.Component.GradientRectangle;
import io.numberrun.Component.Image;
import io.numberrun.Component.Text;
import io.numberrun.Component.Transform;
import io.numberrun.Game.Goal.FinalScore;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class ResultSystem implements GameSystem {

    private final int windowWidth;
    private final int windowHeight;

    // GameOver/Titleと雰囲気を合わせる（必要なら調整）
    private final Color colorTop = new Color(1, 17, 58, 192);     // #01113A 75%
    private final Color colorBottom = new Color(3, 47, 160, 26);  // #032FA0 10%

    public ResultSystem(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    @Override
    public int getPriority() {
        return SystemPriority.HIGH.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) return;

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();
        if (sceneState.getCurrentScene() != SceneType.RESULT) return;

        // 既にオーバーレイがあるなら二重生成しない
        if (!world.query(ResultOverlay.class).isEmpty()) return;

        spawnResultOverlay(world);
    }

    private void spawnResultOverlay(World world) {
        int score = 0;
        int number = 0;

        List<Entity> scores = world.query(FinalScore.class);
        if (!scores.isEmpty()) {
            FinalScore fs = scores.get(0).getComponent(FinalScore.class).get();
            score = fs.getScore();
            number = fs.getNumberAtGoal();
        }

        // 背景
        world.spawn(
                new ResultOverlay(),
                new GradientRectangle(windowWidth, windowHeight, colorTop, colorBottom).withZOrder(200),
                new Transform(0, 0)
        );

        // タイトル
        world.spawn(
                new ResultOverlay(),
                new Text("RESULT", Color.WHITE, new Font("SansSerif", Font.BOLD, 72), 210),
                new Transform(0, -windowHeight * 0.20f)
        );

        // スコア表示
        world.spawn(
                new ResultOverlay(),
                new Text("SCORE: " + score, Color.WHITE, new Font("SansSerif", Font.BOLD, 48), 210),
                new Transform(0, -windowHeight * 0.02f)
        );

        // 数字表示（任意）
        world.spawn(
                new ResultOverlay(),
                new Text("NUMBER: " + number, Color.WHITE, new Font("SansSerif", Font.PLAIN, 32), 210),
                new Transform(0, windowHeight * 0.08f)
        );

        // リトライボタン（GameOverと同じ画像を流用）
        world.spawn(
                new ResultOverlay(),
                new Image(
                        ResultSystem.class.getResource("/images/restart_button.png"),
                        windowWidth * 2 / 3,
                        windowHeight / 5
                ).withZOrder(201),
                new Transform(0, windowHeight * 0.25f),
                new Button() {
                    @Override
                    public void onClick(World w) {
                        ResultExitSystem.exitResultEnterGamePlay(w);
                    }
                }
        );

        // タイトルへボタン
        world.spawn(
                new ResultOverlay(),
                new Image(
                        ResultSystem.class.getResource("/images/title_button.png"),
                        windowWidth * 2 / 3,
                        windowHeight / 5
                ).withZOrder(201),
                new Transform(0, windowHeight * 0.42f),
                new Button() {
                    @Override
                    public void onClick(World w) {
                        ResultExitSystem.exitResultEnterTitle(w);
                    }
                }
        );
    }
}
