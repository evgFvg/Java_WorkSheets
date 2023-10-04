import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassInfo {
    public static void main(String[] args)  {
        if(args.length == 0) {
            System.out.println("Please provide a proper Class name");
            return;
        }
        try {
            Class<?> clazz = Class.forName(args[0]);
            System.out.println("class name is  " + clazz.getName());

            Class<?>[] interfaces = clazz.getInterfaces();
            Field[] fields = clazz.getFields();

            System.out.println("Package name is " + clazz.getPackageName());
            System.out.println("Superclass name is " + clazz.getSuperclass().getName());

            Method[] methods = clazz.getDeclaredMethods();
            System.out.println("Methods in class " + args[0] + "are ");
            for(Method m: methods) {
                System.out.println(m.getName());
            }
            System.out.println("Interfaces in class " + args[0] + "are ");
            if(interfaces.length == 0) {
                System.out.println("No interfaces in the received class");
            }
            else {
                for(Class<?> inf : interfaces) {
                    System.out.println(inf.getName());
                }
            }

            for(Field f: fields) {
                System.out.println("Field name: " + f.getName());
                System.out.println("Field type: " + f.getType());
                System.out.println();
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}