package io.numberrun;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import io.numberrun.Component.Rectangle;
import io.numberrun.Component.Text;
import io.numberrun.Component.Transform;
import io.numberrun.Component.Velocity;
import io.numberrun.Core.GameEngine;
import io.numberrun.Game.Cursor.CursorSystem;
import io.numberrun.Game.Cursor.CursorView;
import io.numberrun.Game.GlobalCursor.GlobalCursorModel;
import io.numberrun.Game.GlobalCursor.GlobalCursorSystem;
import io.numberrun.Game.MovementSystem;
import io.numberrun.System.Entity;
import io.numberrun.System.GameSystem;
import io.numberrun.System.World;
import io.numberrun.UI.InputEvent;
import io.numberrun.UI.InputState;

/**
 * プレイヤー操作システム WASDキーでプレイヤーを移動する
 */
class PlayerMovementSystem implements GameSystem {

    private static final float SPEED = 400f;

    @Override
    public void update(World world, float deltaTime) {
        // このシステムはonInputで処理するので、updateでは何もしない
    }

    // キー入力が入るとこれが呼ばれる
    @Override
    public void onInput(World world, InputEvent event, InputState inputState) {
        // 全ての Transform と Velocity を持つエンティティを取得する
        // 今回はプレイヤーの操作できる四角が該当する
        for (Entity entity : world.query(Transform.class, Velocity.class)) {
            // 四角が持っている Velocity を取得して更新する
            entity.getComponent(Velocity.class).ifPresent(velocity -> {
                float vx = 0;
                float vy = 0;

                if (inputState.isKeyPressed(KeyEvent.VK_W) || inputState.isKeyPressed(KeyEvent.VK_UP)) {
                    vy -= SPEED;
                }
                if (inputState.isKeyPressed(KeyEvent.VK_S) || inputState.isKeyPressed(KeyEvent.VK_DOWN)) {
                    vy += SPEED;
                }
                if (inputState.isKeyPressed(KeyEvent.VK_A) || inputState.isKeyPressed(KeyEvent.VK_LEFT)) {
                    vx -= SPEED;
                }
                if (inputState.isKeyPressed(KeyEvent.VK_D) || inputState.isKeyPressed(KeyEvent.VK_RIGHT)) {
                    vx += SPEED;
                }

                velocity.setVelocity(vx, vy);
            });

        }
    }
}

public class App {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    public static void main(String[] args) {
        // ゲームエンジンの作成
        // ゲームエンジンが Swing の処理を隠蔽する
        GameEngine engine = new GameEngine("Number Run", WINDOW_WIDTH, WINDOW_HEIGHT);
        engine.setBackgroundColor(new Color(40, 40, 40));

        // World が全てのエンティティやロジックを管理
        World world = engine.getWorld();

        // プレイヤーエンティティ（青い四角、WASDで移動）
        {
            Rectangle rect = new Rectangle(50, 50, Color.CYAN);
            rect.setZOrder(0); // 描画の重なり順 (一番下)
            world.spawn(
                    // これらのモデルを持つエンティティをスポーンする
                    new Transform(400, 300), // 表示座標モデル
                    new Velocity(0, 0), // 移動速度モデル
                    rect
            );
        }

        // 静的な障害物エンティティ（赤い四角）
        {
            Rectangle rect = new Rectangle(60, 60, Color.RED);
            rect.setZOrder(10); // 描画の重なり順 (一番上)
            world.spawn(
                    // 動かさない長方形
                    new Transform(200, 200),
                    rect
            );
        }

        // テキストエンティティ
        world.spawn(
                new Transform(400, 50),
                new Text("Demo - Use WASD or Arrow Keys to move", Color.WHITE, new Font("SansSerif", Font.BOLD, 16))
        );

        {
            // カーソルを追跡するエンティティ
            // カーソルの位置に正方形を表示する
            CursorView tracker = new CursorView(50, 50, Color.ORANGE);
            tracker.setZOrder(5); // 描画の重なり順 (水色よりも上だが赤よりも下)
            world.spawn(
                    new Transform(),
                    new GlobalCursorModel(),
                    tracker
            );
        }

        // システムの追加
        // ゲームロジックはシステムとして扱う (これが Controller)
        world.addSystems(
                new PlayerMovementSystem(), // プレイヤー操作 (キーが入力された時に速度を適用する)
                new MovementSystem(), // 移動（Velocity を Transform に反映する）
                new GlobalCursorSystem(), // グローバルなマウス位置を取得するシステムを追加
                new CursorSystem() // 長方形がカーソルをトラッキングするように
        );

        // ゲーム開始
        engine.start();
    }
}
