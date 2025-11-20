public class Pyramid implements Shape3D {
    private Square base;
    private Triangle sides;
    public Pyramid(double b, double h) {
        this.base = new Square(b);
        this.sides = new Triangle(b,h);
    }
    public double getSurfaceArea() {
        return base.getArea() + 4 * sides.getArea();
    }

}