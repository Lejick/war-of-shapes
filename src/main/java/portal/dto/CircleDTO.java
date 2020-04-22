package portal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

@JsonSerialize
public class CircleDTO implements FigureDTO{
    double radius;
    double x;
    double y;
    String type="circle";
    String color = "green";


    public CircleDTO(Body simpleBody, int scale, int levelWidth,int levelHeight) {
        Circle circle = (Circle) simpleBody.getFixture(0).getShape();
        Transform transform = simpleBody.getTransform();
        Vector2 wc = transform.getTransformed(circle.getCenter());
        Vector2 newPos = new Vector2(wc.x * scale + levelWidth / 2, levelHeight - wc.y * scale);
        setRadius(circle.getRadius() * scale);
        setX(newPos.x);
        setY(newPos.y);

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
