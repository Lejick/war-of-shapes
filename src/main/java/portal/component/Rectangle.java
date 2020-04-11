package portal.component;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jbox2d.common.Vec2;

public class Rectangle implements Figure {
    float height;
    float width;
    Vec2 vec;
    float speedX;
    float speedY;
    String type="rectangle";
    String color = "red";

    public Rectangle(float heigth, float width, Vec2 vec) {
        this.height = heigth;
        this.width = width;
        this.vec = vec;
    }

    public Vec2 getVec() {
        return vec;
    }

    public void setVec(Vec2 vec) {
        this.vec = vec;
    }

    public RectangleDTO getDTO(){
        RectangleDTO dto=new RectangleDTO();
        dto.setX(vec.x);
        dto.setY(vec.y);
        dto.setHeight(height);
        dto.setWidth(width);
        return dto;
    }
    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
