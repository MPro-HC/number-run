package io.numberrun.UI;

import java.util.HashSet;
import java.util.Set;

/**
 * 現在の入力状態を管理するクラス どのキーが押されているか、マウスの位置などを追跡する
 */
public class InputState {

    private final Set<Integer> pressedKeys = new HashSet<>();
    private int mouseX;
    private int mouseY;
    private final boolean[] mouseButtons = new boolean[4]; // 0-3のボタン

    /**
     * キーが押されているか確認
     */
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    /**
     * マウスボタンが押されているか確認
     */
    public boolean isMouseButtonPressed(int button) {
        if (button >= 0 && button < mouseButtons.length) {
            return mouseButtons[button];
        }
        return false;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    // 内部用メソッド（パッケージプライベートからpublicに変更）
    public void keyPressed(int keyCode) {
        pressedKeys.add(keyCode);
    }

    public void keyReleased(int keyCode) {
        pressedKeys.remove(keyCode);
    }

    public void mousePressed(int button) {
        if (button >= 0 && button < mouseButtons.length) {
            mouseButtons[button] = true;
        }
    }

    public void mouseReleased(int button) {
        if (button >= 0 && button < mouseButtons.length) {
            mouseButtons[button] = false;
        }
    }

    public void setMousePosition(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }
}
