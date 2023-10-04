import static java.lang.Math.PI;

public class Circle extends Shape {
    private double radius;

    public Circle() {
        radius = 1.0;
    }

    public Circle(double radius) {
        super();
        this.radius = radius;
    }

    public Circle(double radius, String color, boolean filled) {
        super(color, filled);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return PI * Math.pow(radius, 2);
    }

    @Override
    public double getPerimeter() {
        return 2 * PI * radius;
    }

    @Override
    public String toString() {
        return "A Circle with radius + " + radius + " which is a subclass of " + super.toString();
    }
}
