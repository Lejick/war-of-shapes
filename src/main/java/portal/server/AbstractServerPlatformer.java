package portal.server;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import portal.dto.FigureDTO;
import portal.dto.RectangleDTO;
import portal.dto.TextDTO;

import portal.dto.CircleDTO;
import portal.simple.Rectangle;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public abstract class AbstractServerPlatformer {
    private final Logger LOGGER = LoggerFactory.getLogger(AbstractServerPlatformer.class);
    private LevelParameters levelparameters;
    private ServerPlatformerLevelIF simplePlatformerLevel;
    long lastActionTimeMillisecond = Calendar.getInstance().getTimeInMillis();
    public static final double NANO_TO_BASE = 1.0e9;
    private boolean paused;
    private long last;
    private int levelHeight, levelWidth;

    private int scale = 20;

    public AbstractServerPlatformer(ServerPlatformerLevelIF serverPlatformerLevel, int levelHeight, int levelWidth) {
        this.levelHeight = levelHeight;
        this.levelWidth = levelWidth;
        this.simplePlatformerLevel = serverPlatformerLevel;
        levelparameters = new LevelParameters(levelHeight, levelWidth);
        simplePlatformerLevel.initializeWorld(levelHeight / scale, levelWidth / scale);
    }

    public void resetWorld() {
        paused = true;
        simplePlatformerLevel.initializeWorld(levelHeight / scale, levelWidth / scale);
        paused = false;
    }

    public AbstractServerPlatformer() {
    }

    protected LevelParameters getLevelDescription() {
        return levelparameters;
    }


    protected synchronized List<FigureDTO> getObjectState() {
        List<FigureDTO> figureDTOList = new ArrayList<>();
        for (Body body : simplePlatformerLevel.getObstaclesList()) {
            RectangleDTO obstacleDTO = new RectangleDTO(body, scale, levelWidth, levelHeight);
            figureDTOList.add(obstacleDTO);
        }
        CircleDTO circleDTO = new CircleDTO(simplePlatformerLevel.getActionBody(), scale, levelWidth, levelHeight);
        figureDTOList.add(circleDTO);
        // figureDTOList.add(getCircleTransformed(circleDTO));
        // figureDTOList.add(getCircleNatural());
        figureDTOList.add(getCircleForce());
        return figureDTOList;
    }

    protected synchronized List<FigureDTO> getObjectState2() {
        List<FigureDTO> figureDTOList = new ArrayList<>();
        for (Body body : simplePlatformerLevel.getObstaclesList()) {
            RectangleDTO obstacleDTO = new RectangleDTO(body, scale, levelWidth, levelHeight);
            figureDTOList.add(obstacleDTO);
        }
        RectangleDTO rectangleDTO = new RectangleDTO(simplePlatformerLevel.getActionBody(), scale, levelWidth, levelHeight);
        figureDTOList.add(rectangleDTO);
        // figureDTOList.add(getCircleTransformed(circleDTO));
        // figureDTOList.add(getCircleNatural());
        figureDTOList.add(getCircleForce());
        return figureDTOList;
    }

    protected void action(Integer action) {
        lastActionTimeMillisecond = Calendar.getInstance().getTimeInMillis();
        if (action == 65) {
            simplePlatformerLevel.getLeftPressed().set(true);
        } else if (action == 68) {
            simplePlatformerLevel.getRightPressed().set(true);
        } else if (action == 32) {
            simplePlatformerLevel.getJumpPressed().set(true);
        }
    }

    private void gameLoop() {
        long time = System.nanoTime();
        long diff = time - this.last;
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastActionTimeMillisecond > 20) {
            simplePlatformerLevel.getLeftPressed().set(false);
            simplePlatformerLevel.getRightPressed().set(false);
            simplePlatformerLevel.getJumpPressed().set(false);
        }

        this.last = time;
        double elapsedTime = (double) diff / NANO_TO_BASE;

        if (!paused) {
            simplePlatformerLevel.update(elapsedTime);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    protected void start() {
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

    private TextDTO getCircleNatural() {
        DecimalFormat df = new DecimalFormat("0.00");
        Transform transform = simplePlatformerLevel.getActionBody().getTransform();
        TextDTO texInnertDTO = new TextDTO();
        Circle circle = (Circle) simplePlatformerLevel.getActionBody().getFixture(0).getShape();
        Vector2 wc = transform.getTransformed(circle.getCenter());
        texInnertDTO.setHeight(10);
        texInnertDTO.setWidth(120);
        texInnertDTO.setX(levelWidth - 150);
        texInnertDTO.setY(35);
        texInnertDTO.setText("Natural  x:" + df.format(wc.x) + "  y:" + df.format(wc.y));
        return texInnertDTO;
    }

    private TextDTO getCircleTransformed(CircleDTO dto) {
        DecimalFormat df = new DecimalFormat("0.00");
        TextDTO textDTO = new TextDTO();
        textDTO.setHeight(10);
        textDTO.setWidth(120);
        textDTO.setX(levelWidth - 150);
        textDTO.setY(15);
        textDTO.setText("Transformed  x:" + df.format(dto.getX()) + "  y:" + df.format(dto.getY()));
        return textDTO;
    }

    private TextDTO getCircleForce() {
        DecimalFormat df = new DecimalFormat("0.00");
        Body actionBody = simplePlatformerLevel.getActionBody();
        TextDTO texInnertDTO = new TextDTO();

        texInnertDTO.setHeight(10);
        texInnertDTO.setWidth(120);
        texInnertDTO.setX(levelWidth - 150);
        texInnertDTO.setY(35);
       texInnertDTO.setText("Velocity x:" + df.format(actionBody.getLinearVelocity().x) + "  y:" +
                df.format(actionBody.getLinearVelocity().y));
        return texInnertDTO;
    }

}
