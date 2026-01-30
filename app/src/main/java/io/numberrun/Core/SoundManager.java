package io.numberrun.Core;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundManager {

    /**
     * 指定されたパスのサウンドファイルを再生する
     * @param path リソースパス (例: "/sounds/powerup.wav")
     */
    public static void play(String path) {
        new Thread(() -> {
            try {
                URL url = SoundManager.class.getResource(path);
                if (url == null) {
                    System.err.println("Sound not found: " + path);
                    return;
                }

                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}