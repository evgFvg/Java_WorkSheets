import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class WeakList {

    public  static  final int N_LISTS = 1000_000;
    public static  final int N_ELEMENTS = 100_000;
    public static  void memoryTest() {
        List< WeakReference<int[]>> weakList = new ArrayList<>();
        Runtime run = Runtime.getRuntime();

        for(int i = 0; i < N_LISTS; ++i ) {
            int[] arr = new int[N_ELEMENTS];
            WeakReference<int[]> weakRef = new WeakReference<>(arr);
            weakList.add(weakRef);
            System.out.printf("After creating %d instances, Free Memory %d, Total memory %d: %n", i, run.freeMemory(), run.totalMemory());
        }

        assert (weakList.size() == N_LISTS);
    }
}
