package yee.pltision.game.world.entity;

import org.jetbrains.annotations.Nullable;
import yee.pltision.game.client.EntityRenderer;

public interface Entity extends DynamicPosition{
    void tick();
    void discard();

    //如果渲染是函数式的，才需要EntityType让同类实体共用一个渲染器
    //如果为空则不创建渲染程序
    @Nullable EntityRenderer<?> createRender();

}
