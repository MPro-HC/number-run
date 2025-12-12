package io.numberrun.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import javax.imageio.ImageIO;

import io.numberrun.UI.Graphics;

// 画像を表示できる renderable コンポーネント
// 基本的に URL 経由で読みたい
public class Image implements Renderable {

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
        g.drawImage(image, 0, 0,
                width.orElse((float) image.getWidth(null)),
                height.orElse((float) image.getHeight(null)));
    }
}
