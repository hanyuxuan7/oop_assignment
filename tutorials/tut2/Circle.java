package tutorials.tut2;

public class Circle extends Point {
    protected int radius;
    static double PI = 3.1415926;
    public Circle() {
        super(); this.radius = 0;
    }

    public Circle( int x, int y, int r) {
        super(x,y);
        this.radius = r;
    }

    public void setRadius(int r) {
        this.radius = r;
    }

    public int getRadius() {
        return this.radius;
    }

    public double area() {
        return PI * this.radius * this.radius;
    }

    public double circumference() {
        return 2 * PI * this.radius;
    }
}
