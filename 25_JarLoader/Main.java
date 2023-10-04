import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MyClassLoader loader = new MyClassLoader("/home/jey/B/DynamicClassLoader/src/main/java/SLLGeneric.jar" ,
                "ListIterator");
        Class<?>[] arr = loader.load();

        for(Class<?> c: arr) {
            System.out.println(c.getName());
        }
    }
}
