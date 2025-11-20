import java.util.ArrayList;

public class Shape3DApp {
    public static void main(String[] args) {
        ArrayList<Shape3D> shapes = new ArrayList<>();

        shapes.add(new Sphere(10));            // radius 10
        shapes.add(new Pyramid(20, 25));      // base 20, height 25
        shapes.add(new Cuboid(50, 20));     // width 50, height 20

        double totalArea = 0.0;
        for (Shape3D s : shapes) {
            double a = s.getSurfaceArea();
            System.out.println("surface area = " + a);
            totalArea += a;
        }

        System.out.println("Total 3D surface area = " + totalArea);
    }
}
