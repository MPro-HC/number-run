package io.numberrun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import io.numberrun.Component.Image;
import io.numberrun.Component.Transform;
import io.numberrun.Core.GameEngine;
import io.numberrun.Game.GlobalCursor.GlobalCursorSystem;
import io.numberrun.Game.Grid.GridLineSpawnSystem;
import io.numberrun.Game.Lane.LaneMappingSystem;
import io.numberrun.Game.Lane.LaneMovementSystem;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Lane.LaneView;
import io.numberrun.Game.Level.LevelSystem;
import io.numberrun.Game.Player.PlayerMovementSystem;
import io.numberrun.Game.Player.PlayerPassWallSystem;
import io.numberrun.Game.Player.PlayerState;
import io.numberrun.Game.Player.PlayerView;
import io.numberrun.Game.Player.PlayerViewSyncSystem; // 自動でサイズ変更したかったので追加
import io.numberrun.System.World;   // 自動でサイズ変更したかったので追加

public class App {

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 画面の高さの 80% をウィンドウの高さにする (大きすぎず小さすぎず)
        int WINDOW_HEIGHT = (int) (screenSize.height * 0.9);
        // アスペクト比 3:4 (720:960) を維持して幅を計算
        int WINDOW_WIDTH = (int) (WINDOW_HEIGHT * 0.75);
        // ゲームエンジンの作成
        // ゲームエンジンが Swing の処理を隠蔽する
        GameEngine engine = new GameEngine("Number Run", WINDOW_WIDTH, WINDOW_HEIGHT);
        engine.setBackgroundColor(new Color(255, 255, 255));

        // World が全てのエンティティやロジックを管理
        World world = engine.getWorld();

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
            world.spawn(
                    new PlayerState(),
                    new Transform(),
                    new LaneTransform(
                            0.0f, // X 座標 （中央)
                            0.475f, // Y 座標 (下側)
                            false
                    ).setMovementLimit(-0.45f, 0.45f, -0.5f, 0.5f), // 左右移動の範囲を少し制限
                    new LaneVelocity(),
                    new PlayerView()
            );
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
                new LaneMappingSystem(), // レーン上の座標と画面上の座標を変換するシステム
                new GridLineSpawnSystem(), // レーン上にグリッドを表示する
                new PlayerViewSyncSystem(),
                new PlayerMovementSystem(), // プレイヤー操作 (キーが入力された時に速度を適用する)
                new PlayerPassWallSystem() // プレイヤーが壁を通過したか判定するシステム
        );

        // ゲーム開始
        engine.start();
    }
}
