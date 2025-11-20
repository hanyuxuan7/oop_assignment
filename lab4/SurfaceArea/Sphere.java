public class Sphere implements Shape3D {
    private Circle base;
    public Sphere (double r) {
        this.base = new Circle(r);
    }
    public double getSurfaceArea() {
        return 4 * base.getArea();
    }
}