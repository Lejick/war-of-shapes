package portal.component;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LevelContextNormal {
    @GetMapping("/api/level/normal/description")
    public @ResponseBody LevelDesription getLevelDescription() {
        LevelDesription levelDesription = new LevelDesription(250,450,0.01f);
        return levelDesription;
    }

}
