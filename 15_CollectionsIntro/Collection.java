import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class Collection {
    public static void main(String[] args) {
        int[] arr = {44, 6, 2, 34, 546};
        for (int a : arr) {
            System.out.println(a);
        }
        List<Integer> list = createList(arr);
        list.sort((a, b) -> a.compareTo(b));
        System.out.println(list);

        MapExe mapDays = new MapExe();
        mapDays.printEntries();
        mapDays.printValues();

        DataObject[] arrData = new DataObject[10];
        for(int i = 0; i < 10; ++i) {
            arrData[i] = new DataObject("Hello" + i, i * 2);
        }
    }


    private static List<Integer> createList(int[] arr) {
        Function<int[], List<Integer>> arrConverter = array -> {
            List<Integer> res = new ArrayList<>();
            for (int a : array) {
                res.add(a);
            }
            return res;
        };
        return arrConverter.apply(arr);
    }
}

class MapExe {
    private HashMap<String, Integer> map = new HashMap<>();
    public MapExe() {
        List<String> days = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        for (int i = 1; i < 8; ++i) {
            map.put(days.get(i - 1), i);
        }
    }

    public void printEntries() {
        for(Map.Entry<String, Integer> ent: map.entrySet()) {
            System.out.println(ent);
        }
    }
    public void printValues() {
        for(Integer i: map.values()) {
            System.out.println(i);
        }

    }

}

