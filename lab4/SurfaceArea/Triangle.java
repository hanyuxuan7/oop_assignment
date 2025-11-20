public class Triangle implements Shape2D {
    private double base, height;
    public Triangle(double b, double h) {
        this.base = b;
        this.height = h;
    }
    public double getArea() {
        return 0.5*base*height;
    }
    public String getName() {
        return "Triangle";
    }
}