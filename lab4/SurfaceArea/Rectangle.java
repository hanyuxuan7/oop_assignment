public class Rectangle implements Shape2D {
    private double width, height;

    public Rectangle(double w, double h) {
        this.width = w;
        this.height = h;
    }
    public double getArea() {
        return width * height;
    }
    public String getName() {
        return "Rectangle";
    }
}