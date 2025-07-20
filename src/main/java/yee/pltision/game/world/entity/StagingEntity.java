package yee.pltision.game.world.entity;

public class StagingEntity<E extends Entity> {
    E entity;
    int index;
    private boolean isValid;

    public StagingEntity(E entity, int index) {
        this.entity = entity;
        this.index = index;
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