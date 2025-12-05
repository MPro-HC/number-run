package io.numberrun.Game;

import io.numberrun.UI.InputEvent;
import io.numberrun.UI.InputState;

/**
 * ゲーム挙動は GameSystem として扱う 例: 毎フレーム何かする、何かがあったら反応する、など
 */
public interface GameSystem {

    /**
     * システムの優先度（小さいほど先に実行）
     */
    default int getPriority() {
        return 0;
    }

    /**
     * 毎フレーム呼ばれる更新処理
     *
     * @param world ゲームワールド
     * @param deltaTime 前フレームからの経過時間（秒）
     */
    default void update(World world, float deltaTime) {
    }

    /**
     * 入力イベントを処理
     *
     * @param world ゲームワールド
     * @param event 入力イベント
     * @param inputState 現在の入力状態
     */
    default void onInput(World world, InputEvent event, InputState inputState) {
    }

    /**
     * システム初期化時に呼ばれる
     *
     * @param world ゲームワールド
     */
    default void onStart(World world) {
    }

    /**
     * システム終了時に呼ばれる
     *
     * @param world ゲームワールド
     */
    default void onStop(World world) {
    }
}
