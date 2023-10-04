public class Main {
    public static void main(String[] args) {

        /*************************Eager Singleton**********************/
        EagerDatabase one = EagerDatabase.getSingleton();
        EagerDatabase other = EagerDatabase.getSingleton();
        System.out.println(one == other);
        one.addIntToDB(5);
        one.addIntToDB(10);
        Integer a = one.getElementByIndex(1);
        Integer b = other.getElementByIndex(1);
        System.out.println(a == b);

        /**************************InitOnDemand**********************/

        InitializationOnDemand singleton = InitializationOnDemand.getInstance();
        InitializationOnDemand s2 = InitializationOnDemand.getInstance();
        System.out.println(s2 == singleton);
        /******************************lazy init ********************************/

        LazySingleton lz = LazySingleton.getInstance();
        LazySingleton lz2 = LazySingleton.getInstance();
        System.out.println(lz == lz2);

        /*******************Double check lazy*****************************/
        DoubleCheckLazy dlc = DoubleCheckLazy.getDCL();
        DoubleCheckLazy dlc2 = DoubleCheckLazy.getDCL();
        System.out.println(dlc == dlc2);
    }
}
