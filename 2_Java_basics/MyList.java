import java.util.List;
import java.util.ArrayList;


public class MyList {
    public  static  final int N_LISTS = 1000;
    public static  final int N_ELEMENTS = 100_000;
    public void memoryTest() {
        List<int[]> myList = new ArrayList<>();
        Runtime run = Runtime.getRuntime();

        for(int i = 0; i < N_LISTS; ++i ) {
            int[] arr = new int[N_ELEMENTS];
            myList.add(arr);
            System.out.printf("After creating %d instances, Free Memory %d, Total memory %d: %n", i, run.freeMemory(), run.totalMemory());
        }

        assert (myList.size() == N_LISTS);
    }

}

