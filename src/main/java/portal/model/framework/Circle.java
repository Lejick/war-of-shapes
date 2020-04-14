package portal.model.framework;

import org.jbox2d.common.Vec2;
import portal.component.Figure;
import portal.component.RectangleDTO;

public class Circle implements Figure {
    float radius;
    Vec2 vec;
    float speedX;
    float speedY;
    String type="circle";
    String color = "green";

    public Circle(float radius, Vec2 vec) {
        this.vec = vec;
        this.radius=radius;
    }

    public Vec2 getVec() {
        return vec;
    }

    public void setVec(Vec2 vec) {
        this.vec = vec;
    }

    public CircleDTO getDTO() {
        CircleDTO dto = new CircleDTO();
        dto.setX(vec.x);
        dto.setY(vec.y);
        dto.setRadius(radius);
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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
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
