package io.numberrun.Component;

import io.numberrun.UI.Graphics;

/**
 * 描画可能なコンポーネントの基底インターフェース エンティティを画面に描画するために使用する
 */
public interface Renderable extends Component {

    /**
     * 描画処理
     *
     * @param g グラフィックスラッパー
     */
    void render(Graphics g);

    /**
     * 描画順序（Z-order）。小さい値が先に描画される
     *
     * @return 描画順序
     */
    default float getZOrder() {
        return 0;
    }

    void setZOrder(float zOrder);

    default float getWidth() {
        return 0;
    }

    default float getHeight() {
        return 0;
    }

    default Renderable withZOrder(float zOrder) {
        setZOrder(zOrder);
        return this;
    }
}
