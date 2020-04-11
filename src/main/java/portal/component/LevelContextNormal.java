package portal.component;

import org.jbox2d.common.Vec2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;

@Controller
public class LevelContextNormal {
    private final Logger LOGGER = LoggerFactory.getLogger(LevelContextNormal.class);
    Rectangle rectangle;
    LevelParameters levelparameters;
    long lastActionTimeMillisecond = Calendar.getInstance().getTimeInMillis();

    public LevelContextNormal() {
        rectangle = new Rectangle(30, 30, new Vec2(50,50));
        levelparameters = new LevelParameters(500, 500, 0.01f);
        rectangle.setSpeedX(2);
        rectangle.setSpeedY(2);
    }

    @GetMapping("/api/level/normal/description")
    public @ResponseBody
    LevelParameters getLevelDescription() {
        return levelparameters;
    }


    @GetMapping("/api/object/state/")
    public @ResponseBody
    RectangleDTO getObjectState() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastActionTimeMillisecond > 20) {
            lastActionTimeMillisecond = currentTime;
            rectangle = updatePosition(rectangle);
        }
        return rectangle.getDTO();
    }

    @ResponseBody
    @RequestMapping(value = "/api/action/{action}")
    public void action(@PathVariable(value = "action") Integer action) {
        LOGGER.info("action: " + action);
        if(action==32){
            float speedY=rectangle.getSpeedY();
            speedY--;
            rectangle.setSpeedY(speedY);
        }
        if(action==65){
            float speedX=rectangle.getSpeedX();
                speedX--;
                rectangle.setSpeedX(speedX);
        }
        if(action==68){
            float speedX=rectangle.getSpeedX();
            speedX++;
            rectangle.setSpeedX(speedX);
        }
    }
    private Rectangle updatePosition(Rectangle rectangle) {
        Vec2 vec=rectangle.getVec();
        float speedX = rectangle.getSpeedX();
        float speedY = rectangle.getSpeedY();

        vec.x = vec.x + speedX;
        vec.y = vec.y + speedY;

        if (vec.x <= 0 || vec.x >= levelparameters.getMaxWidth() - rectangle.getWidth()) {
            speedX = -1 * speedX;
        }
        if (vec.y >= levelparameters.getMaxHeight()-rectangle.getHeight() || vec.y <= 0) {
            speedY = -1 * speedY;
        }
        rectangle.setVec(vec);
        rectangle.setVec(vec);
        rectangle.setSpeedX(speedX);
        rectangle.setSpeedY(speedY);
        return rectangle;
    }


}
