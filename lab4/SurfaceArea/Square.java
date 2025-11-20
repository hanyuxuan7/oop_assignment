public class Square implements Shape2D {
    private double width;
    public Square(double w){
        this.width = w;
    }

    public double getArea() {
        return width * width;
    }

    public String getName() {
        return "Square";
    }
}