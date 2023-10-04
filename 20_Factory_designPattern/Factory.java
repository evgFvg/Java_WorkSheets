/*
 *  Dev. : Evgenii
 *  Reviewer: Lilach
 *  Date 16.07.2023
* */

//Example of use of the Factory design pattern


import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Factory<K, D, T> {
    private final Map<K, Function<D, ? extends T>> map = new HashMap<>();

    public void add(K key, Function<D, ? extends T> impFunction) {
        map.put(key, impFunction);
    }

    public T getInstance(K key, D param) {
        return map.get(key).apply(param);
    }


    public static void main(String[] args) {
        Factory<String, Integer, Double> f1 = new Factory<>();
        f1.add("one", a -> a + 6.0);  //lambda expression
        Double d = f1.getInstance("one", 7);

        Factory<String, String, StringBuilder> f2 = new Factory<>();
        Student alex = new Student("Alex");

        f2.add("Alex", alex);
        f2.add("StringBuilder", StringBuilder::new);
        f2.add("Alex", Student::staticMethod);  //static method reference
        f2.add("Nick", alex::nonStaticMethod); //non-static method reference


        Factory<String, String, Student> f3 = new Factory<>();
        f3.add("new Stud", Student::new);
        Student newStudent = f3.getInstance("new Stud", "Max");
        System.out.println(newStudent);
    }

}


class Student implements Function<String, StringBuilder> {

    private final String name;

    Student(String name) {
        this.name = name;
    }


    @Override
    public StringBuilder apply(String s) {
        return new StringBuilder(name).append("i am pure apply method");
    }

    public static StringBuilder staticMethod(String s) {
        return new StringBuilder(s).append("I am static");
    }

    public StringBuilder nonStaticMethod(String s) {
        return new StringBuilder(s).append("I am nonStaticMethod");
    }


    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
