package portal.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import portal.model.DataService;

@Controller
public class LevelController {

    private final Logger LOGGER = LoggerFactory.getLogger(LevelController.class);
    public static final String FLOAT_FORM = "floating";

    public static final String JUMP_FORM = "jumping";

    public static final String INDEX_FORM = "index";

    @Autowired
    DataService dataService;

    @GetMapping("/floating")
    public String upload(Model model) {
        return FLOAT_FORM;
    }

    @GetMapping("/jumping")
    public String jump(Model model) {
        return JUMP_FORM;
    }

    @GetMapping("/")
    public String index() {
        return INDEX_FORM;
    }

    @ResponseBody
    @RequestMapping(value = "/api/log/{action}")
    public void logAction(@PathVariable(value = "id") String action) {
        LOGGER.info("action: " + action);
    }
}