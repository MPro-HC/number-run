package io.numberrun.System;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.numberrun.Component.Component;
import io.numberrun.Component.Renderable;
import io.numberrun.Component.Transform;
import io.numberrun.UI.Graphics;
import io.numberrun.UI.InputEvent;
import io.numberrun.UI.InputState;

/**
 * ゲームワールド エンティティとシステムを管理する
 */
public class World {

    private final List<Entity> entities = new ArrayList<>();
    private final List<Entity> entitiesToAdd = new ArrayList<>();
    private final List<GameSystem> systems = new ArrayList<>();
    private boolean systemsSorted = false;

    /**
     * 新しいエンティティを生成
     */
    public Entity spawn(Component... components) {
        Entity entity = new Entity();
        for (Component component : components) {
            entity.addComponent(component);
        }
        entitiesToAdd.add(entity);
        return entity;
    }

    /**
     * システムを追加
     */
    public World addSystem(GameSystem system) {
        systems.add(system);
        systemsSorted = false;
        return this;
    }

    /**
     * 複数のシステムを追加
     */
    public World addSystems(GameSystem... systems) {
        for (GameSystem system : systems) {
            addSystem(system);
        }
        return this;
    }

    /**
     * 特定のコンポーネントを持つエンティティを取得
     */
    public List<Entity> query(Class<? extends Component> componentType) {
        return entities.stream()
                .filter(Entity::isActive)
                .filter(e -> e.hasComponent(componentType))
                .collect(Collectors.toList());
    }

    /**
     * 複数のコンポーネントを全て持つエンティティを取得
     */
    @SafeVarargs
    public final List<Entity> query(Class<? extends Component>... componentTypes) {
        return entities.stream()
                .filter(Entity::isActive)
                .filter(e -> {
                    for (Class<? extends Component> type : componentTypes) {
                        if (!e.hasComponent(type)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * 全てのアクティブなエンティティを取得
     */
    public List<Entity> getAllEntities() {
        return entities.stream()
                .filter(Entity::isActive)
                .collect(Collectors.toList());
    }

    /**
     * 更新処理（内部用）
     */
    public void update(float deltaTime) {
        // 保留中のエンティティを追加
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();

        // 非アクティブなエンティティを削除
        entities.removeIf(e -> !e.isActive());

        // システムをソート
        if (!systemsSorted) {
            systems.sort(Comparator.comparingInt(GameSystem::getPriority));
            systemsSorted = true;
        }

        // 各システムを更新
        for (GameSystem system : systems) {
            system.update(this, deltaTime);
        }
    }

    /**
     * 入力イベントを処理（内部用）
     */
    public void handleInput(InputEvent event, InputState inputState) {
        for (GameSystem system : systems) {
            system.onInput(this, event, inputState);
        }
    }

    /**
     * 描画処理（内部用）
     */
    public void render(Graphics g) {
        // 描画可能なエンティティをZ-orderでソートして描画
        List<Entity> renderableEntities = query(Renderable.class, Transform.class);

        renderableEntities.sort((a, b) -> {
            int za = a.getComponent(Renderable.class).map(Renderable::getZOrder).orElse(0);
            int zb = b.getComponent(Renderable.class).map(Renderable::getZOrder).orElse(0);
            return Integer.compare(za, zb);
        });

        for (Entity entity : renderableEntities) {
            Optional<Transform> transformOpt = entity.getComponent(Transform.class);
            Optional<Renderable> renderableOpt = entity.getComponent(Renderable.class);

            if (transformOpt.isPresent() && renderableOpt.isPresent()) {
                Transform transform = transformOpt.get();
                Renderable renderable = renderableOpt.get();

                // 変換を適用
                AffineTransform saved = g.saveTransform();
                g.transform(
                        transform.getX(),
                        transform.getY(),
                        transform.getRotation(),
                        transform.getScaleX(),
                        transform.getScaleY()
                );

                // 描画
                renderable.render(g);

                // 変換を復元
                g.restoreTransform(saved);
            }
        }
    }

    /**
     * システムを開始（内部用）
     */
    public void start() {
        for (GameSystem system : systems) {
            system.onStart(this);
        }
    }

    /**
     * システムを停止（内部用）
     */
    public void stop() {
        for (GameSystem system : systems) {
            system.onStop(this);
        }
    }
}
