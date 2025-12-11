package group;
import java.awt.event.*;
import javax.swing.Timer;

// ゲーム全体の状態（Model側で定義しておく想定）
enum GameState {
    TITLE,      // タイトル画面
    PLAYING,    // プレイ中
    RESULT      // 結果画面
}

/**
 * Controllerクラス
 * - キーボード入力を受け取ってModelに伝える
 * - Timerで一定間隔ごとにModel.update()を呼ぶ
 * - 状態に応じて画面遷移（TITLE→PLAYING→RESULT→…）を行う
 */
public class GameController implements KeyListener, ActionListener {

    private final GameModel model;   // ゲームの状態と計算
    private final GameView view;     // 画面描画
    private final Timer timer;       // ゲームループ用タイマー

    // コンストラクタでModelとViewを受け取る
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        // 30ミリ秒ごとに actionPerformed() が呼ばれる（約33fps）
        this.timer = new Timer(30, this);
    }

    /** ゲーム開始時に呼んでもらう */
    public void start() {
        timer.start();
    }

    /** Timerから呼ばれる：1フレーム分の更新＋再描画 */
    @Override
    public void actionPerformed(ActionEvent e) {
        // プレイ中だけ進めたいなら、状態を見て条件分岐
        if (model.getState() == GameState.PLAYING) {
            model.update();  // ゲームロジック更新
        }
        view.repaint();      // 画面を描き直す
    }

    /** キーが押されたときに呼ばれる */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_LEFT:
                // ←キーでプレイヤーを左のレーンへ
                if (model.getState() == GameState.PLAYING) {
                    model.movePlayerLeft();
                }
                break;

            case KeyEvent.VK_RIGHT:
                // →キーでプレイヤーを右のレーンへ
                if (model.getState() == GameState.PLAYING) {
                    model.movePlayerRight();
                }
                break;

            case KeyEvent.VK_SPACE:
                // SPACEキーでゲーム開始（タイトル → プレイ中）
                if (model.getState() == GameState.TITLE) {
                    model.startGame();
                }
                // 結果画面からもう一度SPACEでリトライ開始
                else if (model.getState() == GameState.RESULT) {
                    model.resetGame();
                    model.startGame();
                }
                break;

            case KeyEvent.VK_R:
                // Rキーでいつでもリセット（タイトルに戻す想定）
                model.resetGame();
                break;

            default:
                // 他のキーは今は何もしない
                break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
