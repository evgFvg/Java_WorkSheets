public class Hello_world {
    public static void main(String[] args) {
        System.out.println("Hello world!!!!!!");
        MySecondClass gg = new MySecondClass();
        MySecondClass.foo1();
        gg.foo2();

        MyList ml = new MyList();
        ml.memoryTest();

        WeakList.memoryTest();

        StackOverflow.overflow(0);

    }


}
