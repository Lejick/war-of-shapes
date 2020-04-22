package portal.server.circle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import portal.dto.FigureDTO;
import portal.server.LevelParameters;
import portal.server.AbstractServerPlatformer;
import java.util.List;

@Controller
public class CirclePlatformerController extends AbstractServerPlatformer {
    private static Integer LEVEL_HEIGHT = 400;
    private static Integer LEVEL_WIDTH = 400;

    public CirclePlatformerController() {
        super(new CirclePlatformerLevel(), LEVEL_HEIGHT, LEVEL_WIDTH);
        start();
    }

    @GetMapping("/api/level/description/rolling")
    public @ResponseBody
    LevelParameters getLevelDescriptionImpl() {
        return getLevelDescription();
    }


    @GetMapping("/api/object/state/rolling")
    public @ResponseBody
    List<FigureDTO> getObjectStateImpl() {
        return getObjectState();
    }


    @ResponseBody
    @RequestMapping(value = "/api/action//rolling/{action}")
    public void actionImpl(@PathVariable(value = "action") Integer action) {
       action(action);
    }


}
