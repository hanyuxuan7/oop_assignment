public class Cuboid implements Shape3D {
    private Square base;
    private Rectangle sides;
    public Cuboid (double w, double h) {
        this.base = new Square(w);
        this.sides = new Rectangle(w, h);
    }
    public double getSurfaceArea() {
        return 2 * base.getArea() + 4 * sides.getArea();
    }
}