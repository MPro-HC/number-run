package io.numberrun;

import java.awt.Color;
import java.awt.Font;

import io.numberrun.Component.Rectangle;
import io.numberrun.Component.Text;
import io.numberrun.Component.Timer;
import io.numberrun.Component.Transform;
import io.numberrun.Core.GameEngine;
import io.numberrun.Game.GlobalCursor.GlobalCursorSystem;
import io.numberrun.Game.Lane.LaneMappingSystem;
import io.numberrun.Game.Lane.LaneMovementSystem;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneVelocity;
import io.numberrun.Game.Lane.LaneView;
import io.numberrun.Game.Player.PlayerMovementSystem;
import io.numberrun.Game.Player.PlayerView;
import io.numberrun.Game.Player.PlayerViewSyncSystem;
import io.numberrun.System.Entity;
import io.numberrun.System.World;

public class App {

    private static final int WINDOW_WIDTH = 720;
    private static final int WINDOW_HEIGHT = 960;

    public static void main(String[] args) {
        // ゲームエンジンの作成
        // ゲームエンジンが Swing の処理を隠蔽する
        GameEngine engine = new GameEngine("Number Run", WINDOW_WIDTH, WINDOW_HEIGHT);
        engine.setBackgroundColor(new Color(255, 255, 255));

        // World が全てのエンティティやロジックを管理
        World world = engine.getWorld();

        {
            // プレイヤーの表示
            world.spawn(
                    new Transform(),
                    new LaneTransform(
                            0.0f, // X 座標 （中央)
                            0.4f, // Y 座標 (下側)
                            false
                    ).setMovementLimit(-0.45f, 0.45f, -0.5f, 0.5f), // 左右移動の範囲を少し制限
                    new LaneVelocity(),
                    new PlayerView()
            );
        }

        {
            // 道路の表示
            world.spawn(
                    new Transform(0, 0),
                    new LaneView(WINDOW_WIDTH, WINDOW_HEIGHT)
            );
        }

        {
            // 正方形の表示
            world.spawn(
                    new Transform(),
                    new Rectangle(100, 100, Color.RED),
                    new LaneTransform(0.25f, 0.25f), // レーン上の座標
                    new Timer(5_000, Timer.TimerMode.Loop)
            );

            // 正方形の表示(上側なので小さくなる)
            world.spawn(
                    new Transform(),
                    new Rectangle(100, 100, Color.ORANGE),
                    new LaneTransform(0.25f, -0.45f) // レーン上の座標
            );
        }

        {
            // 壁の表示
            Entity blueWall = world.spawn(
                    new Transform(),
                    new Rectangle(WINDOW_WIDTH / 2, 200, new Color(0, 144, 255, 50)),
                    new LaneTransform(-0.25f, -0.25f)
            );
            blueWall.addChildren(
                    world.spawn(
                            new Transform(),
                            new Text("x5", new Color(0x0588F0), new Font("SansSerif", Font.BOLD, 48))
                    )
            );

            // 壁の表示
            Entity redWall = world.spawn(
                    new Transform(),
                    // #E5484D 
                    new Rectangle(WINDOW_WIDTH / 2, 200, new Color(229, 72, 77, 50)),
                    new LaneTransform(0.25f, -0.25f)
            );
            redWall.addChildren(
                    world.spawn(
                            new Transform(),
                            new Text("-2", new Color(0xCE2C31), new Font("SansSerif", Font.BOLD, 48))
                    )
            );
        }

        // システムの追加
        // ゲームロジックはシステムとして扱う (これが Controller)
        world.addSystems(
                new GlobalCursorSystem(), // グローバルなマウス位置を取得するシステムを追加
                new LaneMovementSystem(),
                new LaneMappingSystem(), // レーン上の座標と画面上の座標を変換するシステム
                new PlayerViewSyncSystem(),
                new PlayerMovementSystem() // プレイヤー操作 (キーが入力された時に速度を適用する)
        );

        // ゲーム開始
        engine.start();
    }
}
