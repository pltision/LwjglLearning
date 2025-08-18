package yee.pltision.game.world.entity;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EntityStorage {
    ArrayList<StagingEntity<?>> entities = new ArrayList<>();
    Stack<Integer> emptyElements = new Stack<>();

    /**
     * 添加一个实体到管理器
     *
     * @param entity 实体 非空，不能重复（好像不一定）
     */
    public <E extends Entity> StagingEntity<E> add(E entity) {
        StagingEntity<E> stagingEntity;
        if(emptyElements.empty()){
            stagingEntity = new StagingEntity<>(entity, entities.size());
            entities.add(stagingEntity);
            return stagingEntity;
        }
        else{
            stagingEntity = new StagingEntity<>(entity, emptyElements.pop());
            entities.set(stagingEntity.getIndex(), stagingEntity);
            return stagingEntity;
        }
    }

    /**
     * 从列表中删除一个实体，并将StagingEntity标记无效.
     * 你需要在World中卸载实体而不是这里.
     */
    public void remove(StagingEntity<?> entity) {
        StagingEntity<?> stagingEntity = entities.get(entity.getIndex());
        stagingEntity.invalid();
        entities.set(stagingEntity.getIndex(), null);
        emptyElements.push(stagingEntity.getIndex());
    }
    public void forEach(Consumer<StagingEntity<?>> consumer) {
        for (StagingEntity<?> entity : entities) {
            if (entity!=null) {
                consumer.accept(entity);
            }
        }
    }
    public void forEachEntity(Consumer<Entity> consumer) {
        for (StagingEntity<?> entity : entities) {
            if (entity!=null) {
                consumer.accept(entity.getEntity());
            }
        }
    }

    public <C extends Entity> void forEachEntity(Consumer<C> consumer, Class<C> castClass, Predicate<C> predicate) {
        forEach(stagingEntity -> {
            if(castClass.isInstance(stagingEntity.getEntity())){
                C casted = castClass.cast(stagingEntity.getEntity());
                if(predicate.test(casted)){
                    consumer.accept(casted);
                }
            }
        });
    }

}