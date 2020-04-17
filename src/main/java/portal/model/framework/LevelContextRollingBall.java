package portal.model.framework;

import org.dyn4j.geometry.Vector2;
import org.jbox2d.common.Vec2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import portal.component.LevelParameters;
import portal.component.Rectangle;
import portal.component.RectangleDTO;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class LevelContextRollingBall {
    private final Logger LOGGER = LoggerFactory.getLogger(LevelContextRollingBall.class);
    LevelParameters levelparameters;
    SimplePlatformerLevel simplePlatformerLevel;
    long lastActionTimeMillisecond = Calendar.getInstance().getTimeInMillis();
    /**
     * The conversion factor from nano to base
     */
    public static final double NANO_TO_BASE = 1.0e9;
    SimulationBody simulationBody;
    CircleModel circleModel;
    /**
     * True if the simulation is paused
     */
    private boolean paused;

    /**
     * The time stamp for the last iteration
     */
    private long last;

    public LevelContextRollingBall() {
        levelparameters = new LevelParameters(500, 500, 0.01f);
        simplePlatformerLevel = new SimplePlatformerLevel();
        simplePlatformerLevel.initializeWorld(500L, 500L);
        circleModel = new CircleModel(30, new Vector2(50D, 50D));
        start();
    }

    @GetMapping("/api/level/rolling/ball/description")
    public @ResponseBody
    LevelParameters getLevelDescription() {
        return levelparameters;
    }


    @GetMapping("/api/rolling/object/state/")
    public @ResponseBody
    CircleDTO getObjectState() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastActionTimeMillisecond > 20) {
            lastActionTimeMillisecond = currentTime;
            simulationBody = simplePlatformerLevel.getWheel();
        }
        Vector2 newPos = simulationBody.getLocalCenter();
        circleModel.setVec(newPos);
        CircleDTO dto = circleModel.getDTO();
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/api/rolling/action/{action}")
    public void action(@PathVariable(value = "action") Integer action) {
        LOGGER.info("action: " + action);
        Vector2 linearVelocity = simulationBody.getLinearVelocity();
        if (action == 32) {
            linearVelocity.y--;
        }
        if (action == 65) {
            simplePlatformerLevel.leftPressed.set(true);
        }
        if (action == 68) {
            simplePlatformerLevel.rightPressed.set(true);
        } else {
            simplePlatformerLevel.leftPressed.set(false);
            simplePlatformerLevel.rightPressed.set(false);
        }
    }

    private void gameLoop() {
        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
        // convert from nanoseconds to seconds
        double elapsedTime = (double) diff / NANO_TO_BASE;

        // render anything about the simulation (will render the World objects)

        if (!paused) {
            // update the World
            simplePlatformerLevel.update(elapsedTime);
        }
        // Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();
    }

    private void start() {
        // initialize the last update time
        this.last = System.nanoTime();
        // don't allow AWT to paint the canvas since we are
        // run a separate thread to do active rendering
        // because we don't want to do it on the EDT
        Thread thread = new Thread() {
            public void run() {
                // perform an infinite loop stopped
                // render as fast as possible
                while (true) {
                    gameLoop();
                    // you could add a Thread.yield(); or
                    // Thread.sleep(long) here to give the
                    // CPU some breathing room
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        // set the game loop thread to a daemon thread so that
        // it cannot stop the JVM from exiting
        thread.setDaemon(true);
        // start the game loop
        thread.start();
    }


}
