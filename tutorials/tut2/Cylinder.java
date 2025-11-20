package tutorials.tut2;

public class Cylinder extends Circle{
    protected int height;
    Cylinder() {
        super(); this.height = 0;
    }    
    Cylinder(int x, int y, int r, int h) { 
        super(x, y, r); this.height = h;
    }
    public void setHeight(int h) {
        this.height = h;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRadius() {
        return this.radius;
    }

    public double area() {
        return 2 * super.area() + this.height * super.circumference();
    }

    public double volume() {
        return super.area() * this.height;
    }
}
