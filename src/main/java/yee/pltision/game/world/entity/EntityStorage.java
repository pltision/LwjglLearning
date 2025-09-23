package yee.pltision.game.world.entity;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class EntityStorage {
    // 使用IdStorage替代原来的ArrayList和Stack
    private final IdStorage<StagingEntity<?>> entities = new IdStorage<>();

    /**
     * 添加一个实体到管理器
     *
     * @param entity 实体 非空，不能重复
     */
    public <E extends Entity> StagingEntity<E> add(E entity) {
        return new StagingEntity<>(entity, entities::alloc);
    }

    /**
     * 从列表中删除一个实体，并将StagingEntity标记无效.
     * 你需要在World中卸载实体而不是这里.
     */
    public void remove(StagingEntity<?> entity) {
        if (entity != null && entity.isValid() && entities.containsId(entity.getIndex())) {
            entity.invalid();
            entities.delete(entity.getIndex());
        }
    }

    public void forEach(Consumer<StagingEntity<?>> consumer) {
        entities.forEach((id, stagingEntity) -> {
            if (stagingEntity != null && stagingEntity.isValid()) {
                consumer.accept(stagingEntity);
            }
        });
    }

    public void forEachEntity(Consumer<Entity> consumer) {
        entities.forEach((id, stagingEntity) -> {
            if (stagingEntity != null && stagingEntity.isValid()) {
                consumer.accept(stagingEntity.getEntity());
            }
        });
    }

    public <C extends Entity> void forEachEntity(Consumer<C> consumer, Class<C> castClass, Predicate<C> predicate) {
        entities.forEach((id, stagingEntity) -> {
            if (stagingEntity != null && stagingEntity.isValid()) {
                Entity entity = stagingEntity.getEntity();
                if (castClass.isInstance(entity)) {
                    C casted = castClass.cast(entity);
                    if (predicate.test(casted)) {
                        consumer.accept(casted);
                    }
                }
            }
        });
    }

    /**
     * 获取实体存储中的实体数量
     * @return 实体数量
     */
    public int size() {
        return entities.size();
    }

    /**
     * 检查存储是否为空
     * @return 如果为空返回true
     */
    public boolean isEmpty() {
        return entities.isEmpty();
    }

    /**
     * 根据ID获取实体
     * @param id 实体ID
     * @return 对应的StagingEntity，如果不存在返回null
     */
    public StagingEntity<?> get(int id) {
        return entities.get(id);
    }

    /**
     * 清空所有实体
     */
    public void clear() {
        // 先将所有实体标记为无效
        forEach(StagingEntity::invalid);
        // 然后清空存储
        entities.clear();
    }
}