package io.numberrun.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import javax.imageio.ImageIO;

import io.numberrun.UI.Graphics;

// 画像を表示できる renderable コンポーネント
// 基本的に URL 経由で読みたい
public class Image implements Renderable {

    private float zOrder = 0;

    private final java.awt.Image image;
    private Optional<Float> width = Optional.empty();
    private Optional<Float> height = Optional.empty();

    public Image(java.awt.Image image) {
        this.image = image;
        this.width = Optional.of((float) image.getWidth(null));
        this.height = Optional.of((float) image.getHeight(null));
    }

    public Image(java.awt.Image image, float width, float height) {
        this.image = image;
        this.width = Optional.of(width);
        this.height = Optional.of(height);
    }

    public Image(URL imageUrl) {
        try {
            this.image = ImageIO.read(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + imageUrl, e);
        }

        this.width = Optional.of((float) image.getWidth(null));
        this.height = Optional.of((float) image.getHeight(null));
    }

    public Image(URL imageUrl, float width, float height) {
        try {
            this.image = ImageIO.read(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + imageUrl, e);
        }

        this.width = Optional.of(width);
        this.height = Optional.of(height);
    }

    @Override
    public void render(Graphics g) {
        float w = width.orElse((float) image.getWidth(null));
        float h = height.orElse((float) image.getHeight(null));
        // 中心原点で描画
        g.drawImage(image, -w / 2, -h / 2, w, h);
    }

    @Override
    public float getWidth() {
        return width.orElse((float) image.getWidth(null));
    }

    @Override
    public float getHeight() {
        return height.orElse((float) image.getHeight(null));
    }

    @Override
    public float getZOrder() {
        return zOrder;
    }

    @Override
    public void setZOrder(float zOrder) {
        this.zOrder = zOrder;
    }

}
