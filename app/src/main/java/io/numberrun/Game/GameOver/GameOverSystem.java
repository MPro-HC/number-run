package io.numberrun.Game.GameOver;

import java.awt.Color;
import java.util.List;

import io.numberrun.Component.GradientRectangle;
import io.numberrun.Component.Image;
import io.numberrun.Component.NamedValue;
import io.numberrun.Component.Timer;
import io.numberrun.Component.Transform;
import io.numberrun.Game.Easing.Easing;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Player.PlayerState;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

// シーンステートがゲームオーバーになったら、何かしらのゲームオーバーを表示する
public class GameOverSystem implements GameSystem {

    private final int windowWidth;
    private final int windowHeight;
    private final Color colorTop = new Color(58, 1, 2, 192); // #3A0102 75%
    private final Color colorBottom = new Color(58, 1, 2, 128); // #3A0102 50%

    public GameOverSystem(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    @Override
    public int getPriority() {
        return SystemPriority.HIGH.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // プレイヤーの状態を確認して、ゲームオーバーにする処理を実装する
        // 1. world から PlayerState を持つエンティティを取得
        List<Entity> players = world.query(PlayerState.class);
        if (players.isEmpty()) {
            return;
        }

        PlayerState playerState = players.get(0).getComponent(PlayerState.class).get();

        // SceneState を取得
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) {
            return;
        }

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();

        // ゲームオーバー判定はプレイ中のみ行う
        if (sceneState.getCurrentScene() != SceneType.GAMEPLAY) {
            return;
        }

        // 2. PlayerState の状態を確認して、ゲームオーバー条件を満たしていたら
        // 2.1 ゲームオーバー処理を実行する 
        // SceneState を持つエンティティを取得し、
        // SceneState に GameOver をセットする
        if (playerState.getNumber() <= 0) {
            enterGameOverScene(world);

        }
    }

    private void enterGameOverScene(World world) {
        // 1. Scene をセット
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) {
            return;
        }
        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();
        sceneState.setCurrentScene(SceneType.GAME_OVER);

        // 2. LaneVelocity を持つものを全てゼロに
        List<Entity> laneEntities = world.query(LaneVelocity.class);
        for (Entity laneEntity : laneEntities) {
            LaneVelocity laneVelocity = laneEntity.getComponent(LaneVelocity.class).get();
            laneVelocity.setVy(0);
        }

        // 3. ゲームオーバーのオーバーレイをスポーンする
        spawnGameOverOverlay(world);

        // 広告を表示する
        spawnGameOverAd(world);
    }

    private void spawnGameOverOverlay(World world) {
        world.spawn(
                new GameOverOverlay(),
                // #3A0102 75% -> #3A0102 -> 50%
                new GradientRectangle(windowWidth, windowHeight, colorTop, colorBottom).withZOrder(200),
                new Transform()
        );
        world.spawn(
                new GameOverOverlay(),
                new Image(
                        GameOverSystem.class.getResource("/images/game_over_text.png"),
                        windowWidth, windowHeight
                ).withZOrder(201),
                new Transform()
        );
    }

    private void spawnGameOverAd(World world) {
        world.spawn(
                new GameOverAd(
                        (int) Math.round(windowWidth * 0.9),
                        (int) Math.round(windowHeight * 0.9),
                        new Easing(
                                new Timer(
                                        1000, // ms
                                        Timer.TimerMode.Once
                                )
                        ), // 入退場アニメーション
                        new Easing(
                                new Timer(
                                        1000, // ms
                                        Timer.TimerMode.PingPong
                                )
                        ) // 大きくなったり小さくなったりアニメーション
                ).withZOrder(205),
                new Transform(
                        0,
                        windowHeight // 画面の下側に登場させる
                ),
                new NamedValue<>("initialY", (float) windowHeight)
        ).addChild(
                world.spawn(
                        new Image(
                                GameOverSystem.class.getResource("/images/close_button.png"),
                                50, 50 // w h
                        ).withZOrder(210),
                        new Transform(
                                // 右上の方
                                (int) Math.round(windowWidth * 0.775f / 2),
                                -(int) Math.round(windowHeight * 0.9f / 2)
                        )
                )
        );
    }
}
