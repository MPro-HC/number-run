package io.numberrun;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import io.numberrun.Component.Rectangle;
import io.numberrun.Component.Text;
import io.numberrun.Component.Timer;
import io.numberrun.Component.Transform;
import io.numberrun.Core.GameEngine;
import io.numberrun.Game.GlobalCursor.GlobalCursorSystem;
import io.numberrun.Game.Lane.LaneMappingSystem;
import io.numberrun.Game.Lane.LaneTransform;
import io.numberrun.Game.Lane.LaneView;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.World;

// /**
//  * プレイヤー操作システム WASDキーでプレイヤーを移動する
//  */
// class PlayerMovementSystem implements GameSystem {
//     private static final float SPEED = 400f;
//     @Override
//     public void update(World world, float deltaTime) {
//         // このシステムはonInputで処理するので、updateでは何もしない
//     }
//     // キー入力が入るとこれが呼ばれる
//     @Override
//     public void onInput(World world, InputEvent event, InputState inputState) {
//         // 全ての Transform と Velocity を持つエンティティを取得する
//         // 今回はプレイヤーの操作できる四角が該当する
//         for (Entity entity : world.query(Transform.class, Velocity.class)) {
//             // 四角が持っている Velocity を取得して更新する
//             entity.getComponent(Velocity.class).ifPresent(velocity -> {
//                 float vx = 0;
//                 float vy = 0;
//                 if (inputState.isKeyPressed(KeyEvent.VK_W) || inputState.isKeyPressed(KeyEvent.VK_UP)) {
//                     vy -= SPEED;
//                 }
//                 if (inputState.isKeyPressed(KeyEvent.VK_S) || inputState.isKeyPressed(KeyEvent.VK_DOWN)) {
//                     vy += SPEED;
//                 }
//                 if (inputState.isKeyPressed(KeyEvent.VK_A) || inputState.isKeyPressed(KeyEvent.VK_LEFT)) {
//                     vx -= SPEED;
//                 }
//                 if (inputState.isKeyPressed(KeyEvent.VK_D) || inputState.isKeyPressed(KeyEvent.VK_RIGHT)) {
//                     vx += SPEED;
//                 }
//                 velocity.setVelocity(vx, vy);
//             });
//         }
//     }
// }
// /**
//  * タイマーを使用した一定時間ごとに処理する例
//  */
// class PulseCircleSystem implements GameSystem {
//     @Override
//     public void update(World world, float deltaTime) {
//         List<Entity> circles = world.query(Circle.class, Timer.class);
//         for (Entity entity : circles) {
//             entity.getComponent(Timer.class).ifPresent(timer -> {
//                 timer.tick(deltaTime * 1000); // deltaTime is in seconds, convert to milliseconds
//                 if (timer.justCompleted()) {
//                     // タイマーが終了したら円の色を変えるなどの処理を行う
//                     entity.getComponent(Circle.class).ifPresent(circle -> {
//                         circle.setColor(new Color(
//                                 (int) (Math.random() * 256),
//                                 (int) (Math.random() * 256),
//                                 (int) (Math.random() * 256)
//                         ));
//                     });
//                 }
//             });
//         }
//     }
// }
class RedRectMovementSystem implements GameSystem {

    @Override
    public void update(World world, float deltaTime) {
        List<Entity> rects = world.query(Rectangle.class, LaneTransform.class, Timer.class);

        for (Entity entity : rects) {

            entity.getComponent(Timer.class).ifPresent(timer -> {
                timer.tick(deltaTime * 1000); // deltaTime is in seconds, convert to milliseconds
                float progress = timer.getProgress();

                entity.getComponent(LaneTransform.class).ifPresent(laneTransform -> {
                    // タイマーが終了したら四角の位置を更新する
                    float startY = -0.6f;
                    float endY = 0.6f;
                    float newY = startY + (endY - startY) * progress;

                    laneTransform.setLaneY(newY);
                });

            });
        }

    }
}

public class Sample {

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
                new LaneMappingSystem(), // レーン上の座標と画面上の座標を変換するシステム
                // new PlayerMovementSystem(), // プレイヤー操作 (キーが入力された時に速度を適用する)
                // new MovementSystem(), // 移動（Velocity を Transform に反映する）
                // new CursorSystem(), // 長方形がカーソルをトラッキングするように
                // new PulseCircleSystem() // 円の色を変えるシステム
                new RedRectMovementSystem()
        );

        // ゲーム開始
        engine.start();
    }
}
