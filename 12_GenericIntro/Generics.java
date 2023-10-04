import java.util.List;

public class Generics {
    public static void main(String[] args) {
        printArray(List.of("Hello", "honey", "I", "am", "home"));
        printArray(List.of(1,2,3,4,5,6));
        printArray(List.of(new Object(), new Object(), new Object()));

        fooTester();
    }

    public static void printArray(List<?> list) {
        System.out.println(list);
    }

    public static void fooTester() {
        FooReference<String, Integer> foo1= new FooReference<>("Hello", 69);
        System.out.println(foo1);
        FooReference<List<String>, Integer> foo2= new FooReference<>(List.of("Hello", "world"), 69);
        System.out.println(foo2);


    }

}
class FooReference <T, E> {
    private T t;
    private E e;

    public FooReference(T t, E e) {
        this.t = t;
        this.e = e;
    }

    public E getE() {
        return e;
    }

    public T getT() {
        return t;
    }

    public void setE(E e) {
        this.e = e;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        Class<?> c = this.getClass();
        StringBuilder sb = new StringBuilder();
        sb.
                append("Class name is ").
                append(c.getName()).
                append(" t = ").
                append(t).
                append(" e = ").
                append(e);
        return sb.toString();
    }
}


