package portal.server;

import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import portal.dto.FigureDTO;
import portal.dto.RectangleDTO;
import portal.stupid.LevelParameters;
import portal.dto.CircleDTO;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

@Controller
public class SimplePlatformerController {
    private final Logger LOGGER = LoggerFactory.getLogger(SimplePlatformerController.class);
    LevelParameters levelparameters;
    SimplePlatformerLevel simplePlatformerLevel;
    long lastActionTimeMillisecond = Calendar.getInstance().getTimeInMillis();
    public static final double NANO_TO_BASE = 1.0e9;
    private boolean paused;
    private long last;

    private Integer LEVEL_HEIGHT=400;
    private Integer LEVEL_WIDTH=600;

    public SimplePlatformerController() {
        levelparameters = new LevelParameters(LEVEL_HEIGHT, LEVEL_WIDTH,0);
        simplePlatformerLevel = new SimplePlatformerLevel();
        simplePlatformerLevel.initializeWorld(LEVEL_HEIGHT, LEVEL_WIDTH);
        start();
    }

    @GetMapping("/api/level/rolling/ball/description")
    public @ResponseBody
    LevelParameters getLevelDescription() {
        return levelparameters;
    }


    @GetMapping("/api/rolling/object/state/")
    public @ResponseBody
    List<FigureDTO> getObjectState() {
        Circle circle = (Circle) simplePlatformerLevel.getWheel().getFixture(0).getShape();
        Transform transform = simplePlatformerLevel.getWheel().getTransform();
        Vector2 wc = transform.getTransformed(circle.getCenter());
        Vector2 newPos = new Vector2(wc.x * 25 + LEVEL_WIDTH / 2, LEVEL_HEIGHT - wc.y * (circle.getRadius() * 25));
        CircleDTO dto = new CircleDTO();
        dto.setRadius(circle.getRadius() * 25);
        dto.setX(newPos.x);
        dto.setY(newPos.y);
        List<FigureDTO> figureDTOList=new ArrayList<>();
        RectangleDTO left=new RectangleDTO();
        RectangleDTO right=new RectangleDTO();
        RectangleDTO floor=new RectangleDTO();
        org.dyn4j.geometry.Rectangle leftRec= ( org.dyn4j.geometry.Rectangle) simplePlatformerLevel.getLeft().getFixture(0).getShape();
        org.dyn4j.geometry.Rectangle rightRec= ( org.dyn4j.geometry.Rectangle) simplePlatformerLevel.getLeft().getFixture(0).getShape();
        org.dyn4j.geometry.Rectangle floorRec= ( org.dyn4j.geometry.Rectangle) simplePlatformerLevel.getLeft().getFixture(0).getShape();

        left.setHeight(leftRec.getHeight());
        left.setWidth(leftRec.getWidth());
         wc = transform.getTransformed(leftRec.getCenter());
        left.setX(wc.x);
        left.setY(wc.y);

        right.setHeight(rightRec.getHeight());
        right.setWidth(rightRec.getWidth());
         wc = transform.getTransformed(rightRec.getCenter());
        right.setX(wc.x);
        right.setY(wc.y);

        floor.setHeight(floorRec.getHeight());
        floor.setWidth(floorRec.getWidth());
        wc = transform.getTransformed(floorRec.getCenter());
        floor.setX(wc.x);
        floor.setY(wc.y);


        figureDTOList.add(dto);
        figureDTOList.add(left);
        figureDTOList.add(right);
        figureDTOList.add(floor);
        return figureDTOList;
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
        long time = System.nanoTime();
        long diff = time - this.last;
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastActionTimeMillisecond > 20) {
            simplePlatformerLevel.leftPressed.set(false);
            simplePlatformerLevel.rightPressed.set(false);
        }

        this.last = time;
        double elapsedTime = (double) diff / NANO_TO_BASE;

        if (!paused) {
            simplePlatformerLevel.update(elapsedTime);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void start() {
        this.last = System.nanoTime();
        Thread thread = new Thread(() -> {
            while (true) {
                gameLoop();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }


}
