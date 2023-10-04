public class Square extends Rectangle{
    private double side;
    public Square(){
        super();
        side = 1.0;
    }
    public Square(double side) {
        super();
        this.side = side;
    }
    public  Square(double side, String color, boolean filled) {
        super(side, side, color, filled);
        this.side = side;
    }

    public double getSide() {
        return side;
    }

    public void setSide(double side) {
        this.side = side;
    }

    @Override
    public void setWidth(double width) {
        side = width;
    }

    @Override
    public void setLength(double length) {
        side = length;
    }

    @Override
    public String toString() {
        return "A Square with side = " + getSide()  + " which is a subclass of " + super.toString();
    }
}
