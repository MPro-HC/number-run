package io.numberrun.Game.GameOver;

import javax.imageio.ImageIO;

import io.numberrun.Component.Image;
import io.numberrun.Component.Renderable;
import io.numberrun.Game.Easing.Easing;
import io.numberrun.UI.Graphics;

public class GameOverAd implements Renderable {

    private final Image adImage;
    private boolean isExiting = false; // 退場中か？

    private final Easing transition;
    private final Easing pulse;

    public GameOverAd(int maxWidth, int maxHeight, Easing transition, Easing pulse) {
        this.transition = transition;
        this.pulse = pulse;

        // ランダムに画像を選択する
        // /ads/ad_01.jpg ~ /ads/ad_13.jpg
        int adNumber = (int) (Math.random() * 13) + 1;
        String adImagePath = String.format("/ads/ad_%02d.jpg", adNumber);
        try {
            java.awt.Image adImage = ImageIO.read(GameOverAd.class.getResource(adImagePath));

            // 画像の縦横サイズを取得して、アスペクト比を維持したまま縮小する
            float aspectRatio = (float) adImage.getWidth(null) / adImage.getHeight(null);
            float width = adImage.getWidth(null);
            float height = adImage.getHeight(null);

            if (width > maxWidth) {
                width = maxWidth;
                height = width / aspectRatio;
            }
            if (height > maxHeight) {
                height = maxHeight;
                width = height * aspectRatio;
            }

            this.adImage = new Image(
                    adImage,
                    width, height
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to load ad image: " + adImagePath, e);
        }

    }

    @Override
    public void render(Graphics g) {
        adImage.render(g);
    }

    @Override
    public float getZOrder() {
        return adImage.getZOrder();
    }

    @Override
    public void setZOrder(float zOrder) {
        adImage.setZOrder(zOrder);
    }

    @Override
    public float getWidth() {
        return adImage.getWidth();
    }

    @Override
    public float getHeight() {
        return adImage.getHeight();
    }

    public boolean getIsExiting() {
        return isExiting;
    }

    public boolean isExiting() {
        return isExiting;
    }

    public void setExiting(boolean exiting) {
        this.isExiting = exiting;
    }

    public Easing getTransition() {
        return transition;
    }

    public Easing getPulse() {
        return pulse;
    }

    public void tickEasing(float deltaTime) {
        transition.tick(deltaTime);
        pulse.tick(deltaTime);
    }

}
