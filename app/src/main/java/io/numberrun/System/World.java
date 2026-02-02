package io.numberrun.System;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JFrame;

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

    private JFrame globalFrame;

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
        // 親を持たないルートエンティティのみを対象とする
        List<Entity> rootRenderableEntities = query(Renderable.class, Transform.class).stream()
                .filter(e -> !e.getParent().isPresent())
                .collect(Collectors.toList());

        rootRenderableEntities.sort((a, b) -> {
            float za = a.getComponent(Renderable.class).map(Renderable::getZOrder).orElse(0f);
            float zb = b.getComponent(Renderable.class).map(Renderable::getZOrder).orElse(0f);
            return Float.compare(za, zb);
        });

        for (Entity entity : rootRenderableEntities) {
            renderEntity(g, entity);
        }
    }

    /**
     * エンティティとその子を再帰的に描画
     */
    private void renderEntity(Graphics g, Entity entity) {
        Optional<Transform> transformOpt = entity.getComponent(Transform.class);
        Optional<Renderable> renderableOpt = entity.getComponent(Renderable.class);

        AffineTransform saved = null;
        boolean hasTransform = transformOpt.isPresent();

        // 変換を適用
        if (hasTransform) {
            Transform transform = transformOpt.get();
            saved = g.saveTransform();
            g.transform(
                    transform.getX(),
                    transform.getY(),
                    transform.getRotation(),
                    transform.getScaleX(),
                    transform.getScaleY()
            );
        }

        // 子エンティティを取得してZ-orderでソート
        List<Entity> children = entity.getChildren().stream()
                .filter(Entity::isActive)
                .filter(e -> e.hasComponent(Renderable.class))
                .collect(Collectors.toList());

        children.sort((a, b) -> {
            float za = a.getComponent(Renderable.class).map(Renderable::getZOrder).orElse(0f);
            float zb = b.getComponent(Renderable.class).map(Renderable::getZOrder).orElse(0f);
            return Float.compare(za, zb);
        });

        // zOrderが負の子エンティティを先に描画（親より後ろに表示）
        for (Entity child : children) {
            float childZOrder = child.getComponent(Renderable.class).map(Renderable::getZOrder).orElse(0f);
            if (childZOrder < 0) {
                renderEntity(g, child);
            } else {
                break;
            }
        }

        // 親を描画
        if (renderableOpt.isPresent()) {
            renderableOpt.get().render(g);
        }

        // zOrderが0以上の子エンティティを描画（親より前に表示）
        for (Entity child : children) {
            float childZOrder = child.getComponent(Renderable.class).map(Renderable::getZOrder).orElse(0f);
            if (childZOrder >= 0) {
                renderEntity(g, child);
            }
        }

        // 変換を復元
        if (hasTransform && saved != null) {
            g.restoreTransform(saved);
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

    /**
     * Swing にアクセスしたい時用
     */
    public void setGlobalFrame(JFrame frame) {
        this.globalFrame = frame;
    }

    public JFrame getGlobalFrame() {
        return this.globalFrame;
    }
}
