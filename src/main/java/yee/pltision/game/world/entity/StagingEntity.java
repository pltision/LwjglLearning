package yee.pltision.game.world.entity;

import java.util.function.Function;

/**
 *
 * @param <E> 现在我要去除实体类型改用能力
 */
public class StagingEntity<E extends Entity> {
    E entity;
    int index;
    private boolean isValid;

    public StagingEntity(E entity, Function<StagingEntity<?>,Integer> idFactory) {
        this.entity = entity;
        this.index = idFactory.apply(this);
        this.isValid = true;
    }

    public int getIndex() {
        return index;
    }
    
    public E getEntity() {
        return entity;
    }

    public <C extends Entity> StagingEntity<C> cast(Class<C> clazz){
        if(clazz.isInstance(entity)){
            //noinspection unchecked
            return (StagingEntity<C>) this;
        }
        throw new ClassCastException("Cannot cast entity " + entity.getClass().getName() + " to " + clazz.getName());
    }
    
    public void invalid() {
        isValid = false;
    }

    public boolean isValid() {
        return isValid;
    }
}