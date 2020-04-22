package portal.server.moving;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import portal.dto.FigureDTO;
import portal.server.AbstractServerPlatformer;
import portal.server.LevelParameters;

import java.util.List;

@Controller
public class MovingPlatformerController extends AbstractServerPlatformer {
    private static Integer LEVEL_HEIGHT = 400;
    private static Integer LEVEL_WIDTH = 400;

    public MovingPlatformerController() {
        super(new MovingPlatformerLevel(), LEVEL_HEIGHT, LEVEL_WIDTH);
        start();
    }

    @GetMapping("/api/level/description/movingp")
    public @ResponseBody
    LevelParameters getLevelDescriptionImpl() {
        return getLevelDescription();
    }


    @GetMapping("/api/object/state/movingp")
    public @ResponseBody
    List<FigureDTO> getObjectStateImpl() {
        return getObjectState();
    }


    @ResponseBody
    @RequestMapping(value = "/api/action//movingp/{action}")
    public void actionImpl(@PathVariable(value = "action") Integer action) {
        action(action);
    }

}
