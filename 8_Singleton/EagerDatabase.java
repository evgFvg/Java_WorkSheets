import java.util.ArrayList;

public class EagerDatabase {

    private static EagerDatabase singleton = new EagerDatabase();
    private ArrayList<Integer> database;
    public static EagerDatabase getSingleton(){
        return singleton;
    }
    private EagerDatabase() {
        database = new ArrayList<>();
    }
    public void addIntToDB(Integer a) {
        database.add(a);
    }
    public Integer getElementByIndex(int index) {
        Integer res = null;
        if(index >= 0 && index < singleton.database.size()) {
            res =  singleton.database.get(index);
        }
        return res;
    }
}


