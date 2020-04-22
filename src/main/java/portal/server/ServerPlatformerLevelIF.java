package portal.server;

import org.dyn4j.dynamics.Body;
import portal.dto.FigureDTO;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public interface ServerPlatformerLevelIF {

    void initializeWorld(float height, float width);

    void initPlayBody();

    void initObstacles();

    Body getActionBody();

    void update(double elapsedTime);

    List<Body> getObstaclesList();

    AtomicBoolean getLeftPressed();

    AtomicBoolean getRightPressed();

    AtomicBoolean getJumpPressed();

}
