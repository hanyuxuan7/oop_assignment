package tutorials.tut1;

public class Circle {
    private double radius;
    private static final double PI = 3.14159;

    //constructor
    public Circle(double rad) {
        this.radius = rad;
    }
    public void setRadius(double rad) {
        this.radius = rad;
    }
    public double getRadius() {
        return radius;
    }
    public double area() {
        return PI * radius * radius;
    }
    public double circumference() {
        return 2 * PI * radius;
    }


    //printing = void methods
    public void printArea() {
        System.out.println("Area: " + area());
    }
    public void printCircumference() {
        System.out.println("Circumference: " + circumference());
    }
}
