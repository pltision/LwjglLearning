package yee.pltision.game.world;

import yee.pltision.game.world.entity.Entity;
import yee.pltision.game.world.entity.EntityStorage;
import yee.pltision.game.world.entity.StagingEntity;

public class World {

    protected EntityStorage storage = new EntityStorage();

    public World() {
    }

    public void tick() {
        storage.forEachEntity(Entity::tick);
    }

    public void discardEntity(StagingEntity<?> entity){
        if(entity.isValid()) {
            entity.getEntity().discard();
            storage.remove(entity);
        }
    }

    public <E extends Entity> StagingEntity<E> addEntity(E entity) {
        return storage.add(entity);
    }



}