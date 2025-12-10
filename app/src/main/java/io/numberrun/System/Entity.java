package io.numberrun.System;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.numberrun.Component.Component;

/**
 * 全てのゲーム要素は Entity として扱う Entity はコンポーネントのコンテナとして機能する
 */
public class Entity {

    private static long nextId = 0;

    private final long id;
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();
    private boolean active = true;

    public Entity() {
        this.id = nextId++;
    }

    /**
     * コンポーネントを追加
     */
    public <T extends Component> Entity addComponent(T component) {
        components.put(component.getClass(), component);
        return this;
    }

    /**
     * コンポーネントを取得
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> Optional<T> getComponent(Class<T> type) {
        // 完全一致を試す
        Component component = components.get(type);
        if (component != null) {
            return Optional.of((T) component);
        }

        // インターフェースやスーパークラスで検索
        for (Map.Entry<Class<? extends Component>, Component> entry : components.entrySet()) {
            if (type.isAssignableFrom(entry.getKey())) {
                return Optional.of((T) entry.getValue());
            }
        }

        return Optional.empty();
    }

    /**
     * コンポーネントを持っているか確認
     */
    public <T extends Component> boolean hasComponent(Class<T> type) {
        return getComponent(type).isPresent();
    }

    /**
     * コンポーネントを削除
     */
    public <T extends Component> void removeComponent(Class<T> type) {
        components.remove(type);
    }

    /**
     * エンティティIDを取得
     */
    public long getId() {
        return id;
    }

    /**
     * エンティティがアクティブかどうか
     */
    public boolean isActive() {
        return active;
    }

    /**
     * エンティティをアクティブ/非アクティブにする
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * エンティティを破棄（次のフレームで削除される）
     */
    public void destroy() {
        this.active = false;
    }
}
