package io.numberrun.UI;

/**
 * 入力イベントを表すクラス キーボードとマウスの両方のイベントを統一的に扱う
 */
public class InputEvent {

    private final InputType type;
    private final int keyCode;
    private final char keyChar;
    private final int mouseX;
    private final int mouseY;
    private final int mouseButton;

    private InputEvent(InputType type, int keyCode, char keyChar, int mouseX, int mouseY, int mouseButton) {
        this.type = type;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mouseButton = mouseButton;
    }

    /**
     * キーボードイベントを作成
     */
    public static InputEvent keyEvent(InputType type, int keyCode, char keyChar) {
        return new InputEvent(type, keyCode, keyChar, 0, 0, 0);
    }

    /**
     * マウスイベントを作成
     */
    public static InputEvent mouseEvent(InputType type, int x, int y, int button) {
        return new InputEvent(type, 0, '\0', x, y, button);
    }

    public InputType getType() {
        return type;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public char getKeyChar() {
        return keyChar;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getMouseButton() {
        return mouseButton;
    }

    public boolean isKeyEvent() {
        return type == InputType.KEY_PRESSED || type == InputType.KEY_RELEASED || type == InputType.KEY_TYPED;
    }

    public boolean isMouseEvent() {
        return !isKeyEvent();
    }
}
