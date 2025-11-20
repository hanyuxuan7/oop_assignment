public class Circle implements Shape2D {
    private double radius;
    public Circle(double r) {
        this.radius = r;
    }
    public double getArea() {
        return Math.PI * radius * radius;
    }
    public String getName() {
        return "Circle";
    }
}