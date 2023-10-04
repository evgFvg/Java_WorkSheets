public class Main {
    public static void main(String[] args) {
      /*  B gg = new B(5);
        System.out.printf("Entered main ,  val of a is %d, val of b is %d%n", gg.getA(), gg.getB());
        gg.setD(607); */
/*
        Shape s1 = new Circle(5.5, "RED", false);
        System.out.println(s1);
        System.out.println(s1.getArea());
        System.out.println(s1.getPerimeter());
        System.out.println(s1.getColor());
        //System.out.println(s1.isFilled()); compilation error - access only through getMethods
        //System.out.println(s1.getRadius()); same

        Circle c1 = (Circle) s1;
        System.out.println(c1);
        System.out.println(c1.getArea());
        System.out.println(c1.getPerimeter());
        System.out.println(c1.getColor());
        // System.out.println(c1.isFilled()); same
        System.out.println(c1.getRadius());

        // Shape s2 = new Shape(); abstract class can not be instanced

        Shape s3 = new Rectangle(1.0, 2.0, "RED", false);
        System.out.println(s3);
        System.out.println(s3.getArea());
        System.out.println(s3.getPerimeter());
        System.out.println(s3.getColor());
        // System.out.println(s3.getLength()); Need to be downcasted first

        Rectangle r1 = (Rectangle) s3;
        System.out.println(r1);
        System.out.println(r1.getArea());
        System.out.println(r1.getColor());
        System.out.println(r1.getLength());
*/
        Shape s4 = new Square(6.6);
        //System.out.println(s4);
        //System.out.println(s4.getArea());
        //System.out.println(s4.getColor());
        //System.out.println(s4.getSide());  Need to be downcasted first

        Rectangle r2 = (Rectangle) s4;
        System.out.println(r2);
        System.out.println(r2.getArea());
        System.out.println(r2.getColor());
        //System.out.println(r2.getSide()); SuperClass obj doesn't familiar with subclass methods
        System.out.println(r2.getLength());

        Square sq1 = (Square) r2;
        System.out.println(sq1);
        System.out.println(sq1.getArea());
        System.out.println(sq1.getColor());
        System.out.println(sq1.getSide());
        System.out.println(sq1.getLength());


    }
}

class A {
    private int a;
    private int b;
    public static final int STATIC_VAR;

    static {
        System.out.println("Entered static block A, static var is Initialized");
        STATIC_VAR = 19;
    }

    {
        System.out.printf("Entered non_static block A,  val of a was %d, val of b was %d%n", a, b);
        System.out.println("Entered non_static block A, and b is Initialized");
        a = 15;
        b = 16;
    }

    public A(int a, int b) {
        System.out.printf("Entered constructor A,  val of a is %d, val of b is %d%n", this.a, this.b);
        this.a = a;
        this.b = b;
    }

    public A() {
        System.out.printf("Entered default constructor A,  val of a is %d, val of b is %d%n", this.a, this.b);
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}

class B extends A {
    private int d;
    public static final int C_VALUE;

    static {
        System.out.println("Entered static block in B");
        C_VALUE = 90;
    }

    public B(int d) {
        super();
        this.d = d;
        System.out.printf("Entered constructor B,  val of a is %d, val of b is %d%n", getA(), getB());
        System.out.println("Entered constructor B, static value of C_VALUE is " + B.C_VALUE);
    }

    public void setD(int d) {
        this.d = d;
    }
}


