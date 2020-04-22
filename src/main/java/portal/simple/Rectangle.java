package portal.simple;

import org.dyn4j.geometry.Vector2;
import portal.dto.RectangleDTO;

public class Rectangle implements Figure {
    double height;
    double width;
    Vector2 vec;
    double speedX;
    double speedY;
    String type="rectangle";
    String color = "red";

    public Rectangle(double heigth, double width, Vector2 vec) {
        this.height = heigth;
        this.width = width;
        this.vec = vec;
    }

    public Vector2 getVec() {
        return vec;
    }

    public void setVec(Vector2 vec) {
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

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWidth() {
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
