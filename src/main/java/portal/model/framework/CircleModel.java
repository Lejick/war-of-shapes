package portal.model.framework;

import org.dyn4j.geometry.Vector2;
import org.jbox2d.common.Vec2;
import portal.component.Figure;
import portal.component.RectangleDTO;

public class CircleModel implements Figure {
    double radius;
    Vector2 vec;
    double speedX;
    double speedY;
    String type="circle";
    String color = "green";

    public CircleModel(float radius, Vector2 vec) {
        this.vec = vec;
        this.radius=radius;
    }

    public Vector2 getVec() {
        return vec;
    }

    public void setVec(Vector2 vec) {
        this.vec = vec;
    }

    public CircleDTO getDTO() {
        CircleDTO dto = new CircleDTO();
        dto.setX(vec.x);
        dto.setY(vec.y);
        dto.setRadius(radius);
        return dto;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
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
