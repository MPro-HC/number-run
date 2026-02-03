package io.numberrun.Game.Title;

import java.awt.Color;
import java.util.List;

import io.numberrun.Component.Button;
import io.numberrun.Component.GradientRectangle;
import io.numberrun.Component.Image;
import io.numberrun.Component.Transform;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.SystemPriority;
import io.numberrun.System.World;

public class TitleSystem implements GameSystem {

    private final int windowWidth;
    private final int windowHeight;
    private final Color colorTop = new Color(1, 17, 58, 192); // #01113A 75%
    private final Color colorBottom = new Color(3, 47, 160, 26); // #032FA0 10%

    public TitleSystem(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    @Override
    public int getPriority() {
        return SystemPriority.HIGH.getPriority();
    }

    @Override
    public void update(World world, float deltaTime) {
        // SceneState を取得
        List<Entity> scenes = world.query(SceneState.class);
        if (scenes.isEmpty()) {
            return;
        }

        SceneState sceneState = scenes.get(0).getComponent(SceneState.class).get();

        // ゲームオーバー判定はプレイ中のみ行う
        if (sceneState.getCurrentScene() != SceneType.TITLE) {
            return;
        }

        // TitleOverlay がある？
        List<Entity> overlays = world.query(TitleOverlay.class);
        if (overlays.isEmpty()) {
            // なければスポーン
            spawnTitleOverlay(world);
        }
    }

    private void spawnTitleOverlay(World world) {
        // TitleOverlay コンポーネントを持つエンティティを生成
        world.spawn(
                new TitleOverlay(),
                new GradientRectangle(windowWidth, windowHeight, colorTop, colorBottom).withZOrder(100),
                new Transform()
        );
        world.spawn(
                new TitleOverlay(),
                new Image(
                        TitleSystem.class.getResource("/images/title_text.png"),
                        windowWidth,
                        windowHeight
                ).withZOrder(101),
                new Transform()
        );
        world.spawn(
                new TitleOverlay(),
                new Image(
                        TitleSystem.class.getResource("/images/start_button.png"),
                        windowWidth * 2 / 3,
                        windowHeight / 5
                ).withZOrder(101),
                new Transform(
                        // 中央、下10%
                        0,
                        windowHeight * 0.1f
                ),
                new Button() {
            @Override
            public void onClick(World world) {
                // タイトルからゲームプレイに移行する
                TitleExitSystem.exitTitleEnterGameplay(world);
            }
        }
        );
    }

}
