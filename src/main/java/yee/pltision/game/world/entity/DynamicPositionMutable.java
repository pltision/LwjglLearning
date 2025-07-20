package yee.pltision.game.world.entity;

import org.joml.Vector3f;

public interface DynamicPositionMutable extends DynamicPosition{
    void setPosition(Vector3f pos);
}
