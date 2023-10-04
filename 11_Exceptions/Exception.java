import java.io.IOException;

public class Exception {
    public static void main(String[] args)  {
        ThrowCreator tc = new ThrowCreator();
        try {
            tc.func1();
            tc.func4();
        } catch (IOException io) {
            io.printStackTrace();
        }

        tc.func2();
        tc.indexExceptionCreator();
        DeletableClass dc = new DeletableClass(5);
    }
}

class ThrowCreator {
    public void func1() throws IOException {

    }
    public void func2() {
        throw new NullPointerException("Error msg");
    }
    public void func3() {
        throw new MyException1("Own runTime exception error");
    }
    public void func4() throws MyException2 {
        throw new MyException2("Own Checked Exception error");
    }
    public void indexExceptionCreator() {

        byte[] arr= new byte[100];
        arr[101] = 0;


    }
}

class MyException1 extends RuntimeException {
    public MyException1(String err, Throwable thr) {
        super(err, thr);
    }
    public MyException1(String err) {
        super(err);
    }
}

class MyException2 extends IOException {
    public MyException2(String err, Throwable thr) {
        super(err, thr);
    }
    public MyException2(String err) {
        super(err);
    }
}
