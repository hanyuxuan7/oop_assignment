import java.util.ArrayList;

public class Shape2DApp {
    public static void main(String[] args) {
        ArrayList<Shape2D> shapes = new ArrayList<>();

        shapes.add(new Circle(10));            // radius 10
        shapes.add(new Triangle(20, 25));      // base 20, height 25
        shapes.add(new Rectangle(50, 20));     // width 50, height 20

        double totalArea = 0.0;
        for (Shape2D s : shapes) {
            double a = s.getArea();
            System.out.println(s.getName() + " area = " + a);
            totalArea += a;
        }

        System.out.println("Total 2D area = " + totalArea);
    }
}
