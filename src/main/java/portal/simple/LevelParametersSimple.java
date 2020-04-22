package portal.simple;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class LevelParametersSimple {
    int maxHeight;
    int maxWidth;
    float frictionForce;

    public LevelParametersSimple(int maxHeight, int maxWidth, float frictionForce) {
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.frictionForce = frictionForce;
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

    public float getFrictionForce() {
        return frictionForce;
    }

    public void setFrictionForce(float frictionForce) {
        this.frictionForce = frictionForce;
    }
}
