import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SLLTest {
    private static Student[] arr;
    private static final int STUDENTS_NUM = 10;
    static private Student nick;

    @BeforeAll
    static void setup() {
        nick = new Student(24, "Nick");
        arr = new Student[STUDENTS_NUM];
        for (int i = 0; i < STUDENTS_NUM; ++i) {
            if (i == 5) {
                arr[i] = nick;
            } else {
                arr[i] = new Student(i, "Ben_" + i);

            }
        }
    }

    @Test
    void isEmpty() {
        SLL sll = new SLL();
        assertTrue(sll.isEmpty());
    }

    @Test
    void getSize() {
        SLL sll = new SLL();
        assertEquals(0, sll.getSize());
    }

    @Test
    void pushFront() {
        SLL sll = new SLL();
        for (int i = 0; i < STUDENTS_NUM; ++i) {
            sll.pushFront(arr[i]);
        }
        assertEquals(STUDENTS_NUM, sll.getSize());
        assertNotNull(sll.begin());
        assertFalse(sll.isEmpty());

    }

    @Test
    void popFront() {
        SLL sll = new SLL();

        for (int i = 0; i < STUDENTS_NUM; ++i) {
            sll.pushFront(arr[i]);
        }

        while (sll.popFront() != null) {
        }
        assertEquals(0, sll.getSize());
        assertNull(sll.begin());
        assertTrue(sll.isEmpty());
    }

    @Test
    void find() {
        SLL sll = new SLL();
        for (int i = 0; i < STUDENTS_NUM; ++i) {
            sll.pushFront(arr[i]);
        }

        ListIterator li = sll.find(nick);
        assertNotNull(li);
        assertTrue(nick.equals(li.next()));
    }
}


class Student {
    private int age;
    private String name;

    Student(int age, String name) {
        this.age = age;
        this.name = name;
    }

    Student() {
        age = 18;
        name = "Ben";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Student other = (Student) obj;
        return age == other.age && Objects.equals(name, other.getName());
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}