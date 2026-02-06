package io.numberrun.Core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

public class SoundManager {

    private static final List<Clip> activeClips = Collections.synchronizedList(new ArrayList<>());
    private static volatile boolean initialized = false;

    /**
     * オーディオシステムを事前に初期化（実際に音を無音で再生して完全に起動させる）
     */
    public static void warmup() {
        if (initialized) {
            return;
        }

        synchronized (SoundManager.class) {
            if (initialized) {
                return;
            }

            // 別スレッドでウォームアップを実行（メインスレッドをブロックしない）
            Thread warmupThread = new Thread(() -> {
                try {
                    URL url = SoundManager.class.getResource("/sounds/powerup.wav");
                    if (url == null) {
                        System.err.println("Warmup sound not found");
                        return;
                    }

                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);

                    // 音量を0にして無音で再生
                    if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        volume.setValue(volume.getMinimum());
                    }

                    clip.start();
                    Thread.sleep(50); // 短めに待機
                    clip.stop();
                    clip.close();
                    audioIn.close();

                    initialized = true;
                    System.out.println("[SoundManager] Audio system ready");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            warmupThread.start();

            // ウォームアップ完了を待つ（最大500ms）
            try {
                warmupThread.join(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定されたパスのサウンドファイルを再生する
     */
    public static void play(String path) {
        new Thread(() -> {
            AudioInputStream audioIn = null;
            Clip clip = null;
            try {
                URL url = SoundManager.class.getResource(path);
                if (url == null) {
                    System.err.println("Sound not found: " + path);
                    return;
                }

                audioIn = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();

                final AudioInputStream finalAudioIn = audioIn;
                final Clip finalClip = clip;

                // 再生リストに追加
                activeClips.add(finalClip);

                clip.addLineListener((LineEvent event) -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        try {
                            finalClip.close();
                            finalAudioIn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 再生が終わったらリストから削除
                        activeClips.remove(finalClip);
                    }
                });

                clip.open(audioIn);
                clip.start();

            } catch (Exception e) {
                e.printStackTrace();
                // エラー時のクリーンアップ
                if (clip != null) {
                    clip.close();
                    activeClips.remove(clip);
                }
                if (audioIn != null) {
                    try {
                        audioIn.close();
                    } catch (Exception closeEx) {
                        closeEx.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 再生中の全ての音を停止する
     */
    public static void stopAll() {
        synchronized (activeClips) {
            for (Clip clip : new ArrayList<>(activeClips)) {
                try {
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                    clip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            activeClips.clear();
        }
    }
}
