package io.numberrun.Core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class SoundManager {

    // 再生中のClipを保持するリスト（勝手にGCされて音が途切れるのを防ぐ＆一括停止用）
    private static final List<Clip> activeClips = Collections.synchronizedList(new ArrayList<>());

    /**
     * 指定されたパスのサウンドファイルを再生する
     *
     * @param path リソースパス (例: "/sounds/powerup.wav")
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
     * 現在再生中の全ての音を停止する
     */
    public static void stopAll() {
        synchronized (activeClips) {
            // コピーを作って回さないと、close()した瞬間にイベントリスナーが動いてremoveされエラーになる可能性がある
            List<Clip> clipsToStop = new ArrayList<>(activeClips); 
            for (Clip clip : clipsToStop) {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.close(); // closeすればSTOPイベントが飛び、リストからはリスナー経由で消える
            }
            activeClips.clear();
        }
    }
}