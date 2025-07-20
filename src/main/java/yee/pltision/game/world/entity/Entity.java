package yee.pltision.game.world.entity;

import yee.pltision.game.client.EntityRender;

public interface Entity extends DynamicPosition{
    void tick();
    void discard();

    //如果渲染是函数式的，才需要EntityType让同类实体共用一个渲染器
    EntityRender<?> createRender();
}
