package portal.server;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class LevelParametersServer {
    int maxHeight;
    int maxWidth;

    public LevelParametersServer(int maxHeight, int maxWidth) {
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }
}
