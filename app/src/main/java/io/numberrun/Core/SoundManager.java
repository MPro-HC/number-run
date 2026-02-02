package io.numberrun.Core;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class SoundManager {

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

                clip.addLineListener((LineEvent event) -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        finalClip.close();
                        try {
                            finalAudioIn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
                // Clean up resources on error
                if (clip != null) {
                    clip.close();
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
}
