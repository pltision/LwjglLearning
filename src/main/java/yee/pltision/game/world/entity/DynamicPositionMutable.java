package yee.pltision.game.world.entity;

import org.joml.Vector3d;

public interface DynamicPositionMutable extends DynamicPosition{
    Vector3d getPosition();

    void setPosition(Vector3d pos);
}
