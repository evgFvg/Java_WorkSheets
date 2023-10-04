public class LazySingleton {
    private static LazySingleton instance;
    private LazySingleton() {

    }
    public static LazySingleton getInstance() {
        if(null == instance) {
            instance = new LazySingleton();
        }
        return instance;
    }
}

class DoubleCheckLazy {
    private DoubleCheckLazy() {

    }
    private static DoubleCheckLazy dcl;
    public static DoubleCheckLazy getDCL() {
        if (null == dcl) {
            synchronized (DoubleCheckLazy.class) {
                if(null == dcl) {
                    dcl = new DoubleCheckLazy();
                }
            }
        }
        return dcl;
    }
}
