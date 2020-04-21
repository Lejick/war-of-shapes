package portal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

@JsonSerialize
public class RectangleDTO implements FigureDTO{
    double height;
    double width;
    double x;
    double y;
    String type="rectangle";
    String color = "red";

    public RectangleDTO(Body simpleBody, int scale, int levelWidth, int levelHeight) {
        Rectangle rectangle = (Rectangle) simpleBody.getFixture(0).getShape();
        Transform transform = simpleBody.getTransform();
        Vector2 wc = transform.getTransformed(rectangle.getCenter());
        Vector2 newPos = new Vector2(wc.x * scale+levelWidth/2-(rectangle.getWidth()*scale)/2, levelHeight - wc.y * scale-(rectangle.getHeight()*scale)/2);
        setWidth(rectangle.getWidth() * scale);
        setHeight(rectangle.getHeight() * scale);
        setX(newPos.x);
        setY(newPos.y);

    }

    public RectangleDTO() {
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
