import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Comparator;

public class ObjectAnalyzer {
    public static void main(String[] args) throws Exception {
        Class<FooGrandSon> c  = FooGrandSon.class;
        printAncestors(c);
        printInterfaces(c);
        printModifiers(c);
        printMembers(c);
        callPrivateMethod(c);

    }

    private static void callPrivateMethod(Class<?> c) throws Exception {
        Constructor<?> defCons = c.getConstructor(null);
        FooGrandSon fgs = (FooGrandSon) defCons.newInstance();
        Method mFoo1 = c.getDeclaredMethod("foo1", int.class);
        mFoo1.setAccessible(true);
        System.out.printf("\n\t Calling private method via reflection\n");
        mFoo1.invoke(fgs, 3);
    }

    private static void printMembers(Class<?> c) {
        System.out.printf("\n\t Fields of the class " + c.getName() + "\n");
        Field[] fields = c.getDeclaredFields();
        for(Field f: fields) {
            System.out.println(f.getName());
        }
    }

    private static void printModifiers(Class<?> c) {
        System.out.printf("\n\t Modifiers of the class " + c.getName() + "\n");
        Class<?>[] classArr = c.getDeclaredClasses();
        int mod;
        String className;
        for(Class<?> inner: classArr) {
            mod = inner.getModifiers();
            className = inner.getSimpleName();
            System.out.println(Modifier.toString(mod) + " " + className );
        }
    }

    private static void printInterfaces(Class<?> c) {
        System.out.printf("\t \nInterfaces of the class " + c.getName() + "\n");
        Class<?>[] interArr = c.getInterfaces();
        for(Class<?> cl: interArr) {
            System.out.println(cl.getName());
        }
    }

    public static void printAncestors(Class<?> c) {
        System.out.printf("\n\t Ancestors of the class " + c.getName() + "\n");
        while(null != c) {
            System.out.println(c.getName());
            c = c.getSuperclass();
        }
    }
}

class FooFather {
    public void test() {}
}

class FooSon extends FooFather {
    public void test() {
        System.out.println("FooSon");
    }
}

class FooGrandSon extends FooSon implements Comparable<FooGrandSon> , Comparator<FooGrandSon> {
    private int a;
    public static final String name = "Max";
    private ArrayList<Integer> arrList;

    public  FooGrandSon() {

    }

    public void test() {
        System.out.println("FooGrandSon");
    }
    private void foo1(int a) {
        System.out.println("i am foo1 " + a);
    }

    @Override
    public int compareTo(FooGrandSon fooGrandSon) {
        return 0;
    }

    @Override
    public int compare(FooGrandSon fooGrandSon, FooGrandSon t1) {
        return 0;
    }

    private static class InnerStatic {

    }
    private class Engine {

    }
}

