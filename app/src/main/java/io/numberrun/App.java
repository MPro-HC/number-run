package io.numberrun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import io.numberrun.Component.Image;
import io.numberrun.Component.Transform;
import io.numberrun.Core.GameEngine;
import io.numberrun.Core.SoundManager;
import io.numberrun.Game.Animation.SpriteAnimationSystem;
import io.numberrun.Game.Effect.DamageEffectSystem;
import io.numberrun.Game.Effect.PowerUpEffectSystem;
import io.numberrun.Game.GameOver.GameOverAdSystem;
import io.numberrun.Game.GameOver.GameOverExitSystem;
import io.numberrun.Game.GameOver.GameOverSystem;
import io.numberrun.Game.GlobalCursor.GlobalCursorSystem;
import io.numberrun.Game.Grid.GridLineSpawnSystem;
import io.numberrun.Game.Lane.LaneMappingSystem;
import io.numberrun.Game.Lane.LaneMovementSystem;
import io.numberrun.Game.Lane.LaneView;
import io.numberrun.Game.Level.Level;
import io.numberrun.Game.Level.LevelSystem;
import io.numberrun.Game.Obstacle.ObstacleRotateSystem;
import io.numberrun.Game.Obstacle.ObstacleWobbleSystem;
import io.numberrun.Game.Obstacle.PlayerHitObstacleSystem;
import io.numberrun.Game.Player.PlayerMovementSystem;
import io.numberrun.Game.Player.PlayerPassWallSystem;
import io.numberrun.Game.Player.PlayerView;
import io.numberrun.Game.Player.PlayerViewSyncSystem;
import io.numberrun.Game.Scene.Scene;
import io.numberrun.Game.Scene.SceneState;
import io.numberrun.Game.Scene.SceneType;
import io.numberrun.Game.Title.TitleExitSystem;
import io.numberrun.Game.Title.TitleSystem;
import io.numberrun.System.World;
import io.numberrun.UI.ButtonClickSystem;

public class App {

    public static void main(String[] args) {
        // オーディオシステムを事前に初期化
        SoundManager.warmup();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 画面の高さの 90% をウィンドウの高さにする
        int WINDOW_HEIGHT = (int) (screenSize.height * 0.9);
        // アスペクト比 3:4を維持して幅を計算
        int WINDOW_WIDTH = (int) (WINDOW_HEIGHT * 0.75);
        // ゲームエンジンの作成
        // ゲームエンジンが Swing の処理を隠蔽する
        GameEngine engine = new GameEngine("Number Run", WINDOW_WIDTH, WINDOW_HEIGHT);
        engine.setBackgroundColor(new Color(255, 255, 255));

        // World が全てのエンティティやロジックを管理
        World world = engine.getWorld();

        {
            // シーンに関する情報をもつエンティティを置いておく
            // 本来なら Resource とかで管理すべきだけど、まあ
            world.spawn(
                    new Scene(),
                    new SceneState(SceneType.TITLE),
                    new Level() // ステージ管理
            );
        }

        {
            // 背景画像
            world.spawn(
                    new Transform(),
                    new Image(
                            App.class.getResource("/images/background.jpg"),
                            WINDOW_WIDTH, WINDOW_HEIGHT
                    ).withZOrder(-200)
            );
        }

        {
            // 奥行き感を出すためのグラデーション
            world.spawn(
                    new Transform(),
                    new Image(
                            App.class.getResource("/images/overlay_gradient.png"),
                            WINDOW_WIDTH, WINDOW_HEIGHT
                    ).withZOrder(50) // 上の方に置いとく
            );
        }
        {
            // プレイヤーの表示
            PlayerView.setupInitialPlayer(world);
        }

        {
            // レーンの表示
            LaneView laneView = new LaneView(WINDOW_WIDTH, WINDOW_HEIGHT).withZOrder(-100);
            world.spawn(
                    new Transform(0, 0),
                    laneView
            );
            // グリッドを引いておく
            GridLineSpawnSystem.setupInitialLines(world, laneView);
        }

        // システムの追加
        // ゲームロジックはシステムとして扱う (これが Controller)
        world.addSystems(
                new GlobalCursorSystem(), // グローバルなマウス位置を取得するシステムを追加
                new LevelSystem(), // レベル進行・障害物生成システム
                new LaneMovementSystem(),
                new ObstacleWobbleSystem(),
                new ObstacleRotateSystem(), // 障害物の回転、あってもなくてもいい
                new PlayerHitObstacleSystem(
                        WINDOW_WIDTH,
                        WINDOW_HEIGHT
                ),
                new LaneMappingSystem(), // レーン上の座標と画面上の座標を変換するシステム
                new GridLineSpawnSystem(), // レーン上にグリッドを表示する
                new PlayerViewSyncSystem(),
                new PlayerMovementSystem(), // プレイヤー操作 (キーが入力された時に速度を適用する)
                new PlayerPassWallSystem(
                        WINDOW_WIDTH,
                        WINDOW_HEIGHT
                ), // プレイヤーが壁を通過したか判定するシステム
                new GameOverSystem(
                        WINDOW_WIDTH,
                        WINDOW_HEIGHT
                ), // ゲームオーバー判定と処理
                new GameOverExitSystem(),
                new DamageEffectSystem(),
                new PowerUpEffectSystem(),
                new GameOverAdSystem(),
                new SpriteAnimationSystem(),
                new TitleExitSystem(),
                new TitleSystem(WINDOW_WIDTH, WINDOW_HEIGHT),
                new ButtonClickSystem()
        );

        // ゲーム開始
        engine.start();
    }
}
