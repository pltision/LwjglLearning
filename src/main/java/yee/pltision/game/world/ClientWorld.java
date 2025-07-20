package yee.pltision.game.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.game.client.EntityRender;
import yee.pltision.game.world.entity.Entity;
import yee.pltision.game.world.entity.StagingEntity;

public class ClientWorld extends World {



    @Nullable private StagingEntity<?> player;

    @Nullable
    Entity getPlayer(){
        if(player!=null&&player.isValid())
            return (Entity) player;
        return null;
    }

    public ClientWorld()
    {
        super();
    }

    public void setPlayer(@Nullable StagingEntity<?> stagingEntity){
        player=stagingEntity;
    }

}