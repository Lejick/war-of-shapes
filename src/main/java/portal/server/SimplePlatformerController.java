package portal.server;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import portal.stupid.LevelParameters;
import portal.dto.CircleDTO;

import java.awt.*;
import java.util.Calendar;

@Controller
public class SimplePlatformerController {
    private final Logger LOGGER = LoggerFactory.getLogger(SimplePlatformerController.class);
    LevelParameters levelparameters;
    SimplePlatformerLevel simplePlatformerLevel;
    long lastActionTimeMillisecond = Calendar.getInstance().getTimeInMillis();
    /**
     * The conversion factor from nano to base
     */
    public static final double NANO_TO_BASE = 1.0e9;
    /**
     * True if the simulation is paused
     */
    private boolean paused;

    /**
     * The time stamp for the last iteration
     */
    private long last;

    public SimplePlatformerController() {
        levelparameters = new LevelParameters(500, 500, 0.01f);
        simplePlatformerLevel = new SimplePlatformerLevel();
        simplePlatformerLevel.initializeWorld(500L, 500L);
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
        Circle circle = (Circle) simplePlatformerLevel.getWheel().getFixture(0).getShape();
        Transform transform = simplePlatformerLevel.getWheel().getTransform();
        Vector2 wc = transform.getTransformed(circle.getCenter());
        Vector2 newPos = new Vector2(wc.x * 25 + 250, 500 - wc.y * (circle.getRadius() * 25));
        CircleDTO dto = new CircleDTO();
        dto.setRadius(circle.getRadius() * 25);
        dto.setX(newPos.x);
        dto.setY(newPos.y);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/api/rolling/action/{action}")
    public void action(@PathVariable(value = "action") Integer action) {
        LOGGER.info("action: " + action);
        lastActionTimeMillisecond = Calendar.getInstance().getTimeInMillis();
        if (action == 65) {
            simplePlatformerLevel.leftPressed.set(true);
        } else if (action == 68) {
            simplePlatformerLevel.rightPressed.set(true);
        }
    }

    private void gameLoop() {
        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastActionTimeMillisecond > 20) {
            simplePlatformerLevel.leftPressed.set(false);
            simplePlatformerLevel.rightPressed.set(false);
        }

        this.last = time;
        // convert from nanoseconds to seconds

        double elapsedTime = (double) diff / NANO_TO_BASE;

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
