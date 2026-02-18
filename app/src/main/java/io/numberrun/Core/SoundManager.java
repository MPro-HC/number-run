package io.numberrun.Core;

// 必要なJava標準ライブラリのインポート
import java.net.URL; // リソースURLを扱うクラス
import java.util.ArrayList; // 可変長配列リスト
import java.util.Collections; // コレクション操作のためのユーティリティ
import java.util.List; // リストインターフェース

// Java Sound APIのインポート
import javax.sound.sampled.AudioInputStream; // 音声入力ストリーム
import javax.sound.sampled.AudioSystem; // 音声システムのファクトリクラス
import javax.sound.sampled.Clip; // 音声データのクリップ（メモリ上にロードして再生）
import javax.sound.sampled.FloatControl; // 音量などのコントロール
import javax.sound.sampled.LineEvent; // 音声ラインのイベント（再生停止など）

// サウンド再生を管理するクラス
public class SoundManager {

    // 再生中のClipを保持するリスト。複数のスレッドからアクセスされるため同期化リストにする
    private static final List<Clip> activeClips = Collections.synchronizedList(new ArrayList<>());
    // 初期化済みかどうかを判定するフラグ。スレッド間の可視性を保証するため volatile にする
    private static volatile boolean initialized = false;

    /**
     * オーディオシステムを事前に初期化（実際に音を無音で再生して完全に起動させる）
     */
    public static void warmup() {
        // すでに初期化済みなら何もしない
        if (initialized) {
            return;
        }

        // 同期ブロックで排他制御を行い、複数回初期化されるのを防ぐ
        synchronized (SoundManager.class) {
            // ダブルチェックロッキング：ロック取得後にもう一度確認
            if (initialized) {
                return;
            }

            // 別スレッドでウォームアップを実行（メインスレッドの描画をブロックしないため）
            Thread warmupThread = new Thread(() -> {
                try {
                    // クラスパスからサウンドファイルのリソースを取得する
                    URL url = SoundManager.class.getResource("/sounds/powerup.wav");
                    // リソースが見つからなかった場合のエラー処理
                    if (url == null) {
                        System.err.println("Warmup sound not found");
                        return;
                    }

                    // 音声入力ストリームを取得
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                    // 音声を再生するためのClipを取得
                    Clip clip = AudioSystem.getClip();
                    // ストリームを開いてデータを読み込む
                    clip.open(audioIn);

                    // 音量を操作できるか確認し、最小値（無音）に設定する
                    if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        volume.setValue(volume.getMinimum());
                    }

                    // 再生を開始する（システム内部のミキサーなどを起動させる目的）
                    clip.start();
                    // 少しだけ待機する
                    Thread.sleep(50);
                    // 再生を停止する
                    clip.stop();
                    // リソースを解放する
                    clip.close();
                    // ストリームも閉じる
                    audioIn.close();

                    // 初期化完了フラグを立てる
                    initialized = true;
                    // デバッグ用にログを出力
                    System.out.println("[SoundManager] Audio system ready");

                } catch (Exception e) {
                    // 例外が発生した場合はスタックトレースを出力
                    e.printStackTrace();
                }
            });

            // ウォームアップスレッドを開始する
            warmupThread.start();

            // ウォームアップ完了を最大500ms待つ（ゲーム起動時の遅延を許容）
            try {
                warmupThread.join(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //指定されたパスのサウンドファイルを再生する
    public static void play(String path) {
        // 再生処理のために新しいスレッドを作成して開始する（非同期処理）
        new Thread(() -> {
            // リソース解放のために変数をtryの外で宣言
            AudioInputStream audioIn = null;
            Clip clip = null;
            try {
                // 指定されたパスからURLを取得
                URL url = SoundManager.class.getResource(path);
                // ファイルが見つからない場合はエラー出力して終了
                if (url == null) {
                    System.err.println("Sound not found: " + path);
                    return;
                }

                // 音声入力ストリームを取得
                audioIn = AudioSystem.getAudioInputStream(url);
                // 再生用のClipを取得
                clip = AudioSystem.getClip();

                // ラムダ式内で使用するためにfinal変数に代入
                final AudioInputStream finalAudioIn = audioIn;
                final Clip finalClip = clip;

                // 再生中のリストに追加して管理する
                activeClips.add(finalClip);

                // 再生状態の変化を監視するリスナーを追加
                clip.addLineListener((LineEvent event) -> {
                    // イベントが「停止（STOP）」だった場合の処理
                    if (event.getType() == LineEvent.Type.STOP) {
                        try {
                            // Clipを閉じてリソースを解放する
                            finalClip.close();
                            // ストリームも閉じる
                            finalAudioIn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 再生が終わったらリストから削除
                        activeClips.remove(finalClip);
                    }
                });

                // クリップを開いてデータを読み込む
                clip.open(audioIn);
                // 再生を開始する
                clip.start();

            } catch (Exception e) {
                // エラーが発生した場合はスタックトレースを表示
                e.printStackTrace();
                // エラー時のクリーンアップ処理
                if (clip != null) {
                    clip.close(); // クリップを閉じる
                    activeClips.remove(clip); // リストから削除
                }
                if (audioIn != null) {
                    try {
                        audioIn.close(); // ストリームを閉じる
                    } catch (Exception closeEx) {
                        closeEx.printStackTrace();
                    }
                }
            }
        }).start(); // スレッドの開始
    }

    /**
     * 再生中の全ての音を停止する
     */
    public static void stopAll() {
        // リストに対する操作を同期化（排他制御）する
        synchronized (activeClips) {
            // ConcurrentModificationExceptionを防ぐためにコピーを作成してループする
            for (Clip clip : new ArrayList<>(activeClips)) {
                try {
                    // 再生中であれば停止する
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                    // リソースを解放する
                    clip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // リストをクリアする
            activeClips.clear();
        }
    }
}
