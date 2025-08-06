package yee.pltision.game.world.entity;

import org.joml.Vector3f;

public interface DynamicPositionMutable extends DynamicPosition{
    Vector3f getPosition();

    void setPosition(Vector3f pos);
}
