package portal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MenuController {

    private final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);
    public static final String FLOAT_FORM = "floating";

    public static final String JUMP_FORM = "jumping";

    public static final String ROLLING_BALL_FORM = "rollingBall";

    public static final String INDEX_FORM = "index";

    public static final String SERVER_BASED_FORM = "serverbased";

    @GetMapping("/floating")
    public String upload() {
        return FLOAT_FORM;
    }

    @GetMapping("/jumping")
    public String jump() {
        return JUMP_FORM;
    }

    @GetMapping("/serverbased")
    public String serverbased() {
        return SERVER_BASED_FORM;
    }

    @GetMapping("/rollingBall")
    public String srollingBall() {
        return ROLLING_BALL_FORM;
    }

    @GetMapping("/")
    public String index() {
        return INDEX_FORM;
    }
}