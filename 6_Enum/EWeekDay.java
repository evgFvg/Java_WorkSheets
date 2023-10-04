import java.util.ArrayList;

public enum EWeekDay {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private final int eValue;
    private static ArrayList<String> strDays ;

    static {
        strDays = new ArrayList<>();
        strDays.add("MONDAY");
        strDays.add("TUESDAY");
        strDays.add("WEDNESDAY");
        strDays.add("THURSDAY");
        strDays.add("FRIDAY");
        strDays.add("SATURDAY");
        strDays.add("SUNDAY");
    }

    EWeekDay(int eValue) {
        this.eValue = eValue;
    }

    public int getEnumValue() {
        return eValue;
    }

    public static EWeekDay fromValue(int day) {
        if (day < 1 || day > 7) {
            return null;
        }
        return EWeekDay.valueOf(strDays.get(day - 1));
    }
    public static final  void printAll () {
        for(EWeekDay ed: EWeekDay.values()) {
            System.out.println(ed);
        }
    }
}
