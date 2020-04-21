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
import portal.dto.TextDTO;
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

    private Integer LEVEL_HEIGHT = 400;
    private Integer LEVEL_WIDTH = 400;
    private int scale = 20;

    public SimplePlatformerController() {
        levelparameters = new LevelParameters(LEVEL_HEIGHT, LEVEL_WIDTH, 0);
        simplePlatformerLevel = new SimplePlatformerLevel();
        simplePlatformerLevel.initializeWorld(LEVEL_HEIGHT / scale, LEVEL_WIDTH / scale);
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

        List<FigureDTO> figureDTOList = new ArrayList<>();
        RectangleDTO leftDTO = new RectangleDTO(simplePlatformerLevel.getLeft(), scale, LEVEL_WIDTH, LEVEL_HEIGHT);
        RectangleDTO rightDTO = new RectangleDTO(simplePlatformerLevel.getRight(), scale, LEVEL_WIDTH, LEVEL_HEIGHT);
        RectangleDTO floorDTO = new RectangleDTO(simplePlatformerLevel.getFloor(), scale, LEVEL_WIDTH, LEVEL_HEIGHT);
        CircleDTO circleDTO = new CircleDTO(simplePlatformerLevel.getWheel(),scale,LEVEL_WIDTH,LEVEL_HEIGHT);

        figureDTOList.add(circleDTO);
        figureDTOList.add(leftDTO);
        figureDTOList.add(rightDTO);
        figureDTOList.add(floorDTO);
        figureDTOList.add(getCircleTransformed(circleDTO));
        figureDTOList.add(getCircleNatural());
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

    private TextDTO getCircleNatural(){
        Transform transform = simplePlatformerLevel.getWheel().getTransform();
        TextDTO texInnertDTO = new TextDTO();
        Circle circle = (Circle) simplePlatformerLevel.getWheel().getFixture(0).getShape();
        Vector2 wc = transform.getTransformed(circle.getCenter());
        texInnertDTO.setHeight(10);
        texInnertDTO.setWidth(120);
        texInnertDTO.setX(LEVEL_WIDTH - 220);
        texInnertDTO.setY(35);
        texInnertDTO.setText("Inner wheel  x:" + wc.x + "  y:" + wc.y);
        return texInnertDTO;
    }

    private TextDTO getCircleTransformed(CircleDTO dto){
        TextDTO textDTO = new TextDTO();
        textDTO.setHeight(10);
        textDTO.setWidth(120);
        textDTO.setX(LEVEL_WIDTH - 120);
        textDTO.setY(15);
        textDTO.setText("Circle  x:" + Math.round(dto.getX()*100)/100 + "  y:" + Math.round(dto.getY()*100)/100);
        return  textDTO;
    }

}
