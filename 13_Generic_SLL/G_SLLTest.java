import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class G_SLLTest {
    private static G_SLL<String> sllStrings;
    private static G_SLL<Student> sllStudents;
    private static final int NUM_ELEMENTS = 100;

    @BeforeEach
    void setup() {
        sllStrings = new G_SLL<>();
        sllStudents = new G_SLL<>();

        for (int i = 0; i < 10; ++i) {
            sllStudents.pushFront(GetRandomStudent());
        }
    }

    private static Student GetRandomStudent() {
        int randNameIndex = 0;
        int randAgeIndex = 0;
        Random rand = new Random();
        List<String> students = List.of("Ben", "Alex", "Max", "Bob", "Nick", "Kate", "Suzan");
        List<Integer> ages = List.of(23, 45, 56, 41, 18, 26, 33, 54, 52, 29, 57);
        randNameIndex = rand.nextInt(students.size());
        randAgeIndex = rand.nextInt(ages.size());

        return new Student(ages.get(randAgeIndex), students.get(randNameIndex));
    }


    @Test
    @Order(1)
    void isEmpty() {
        assertTrue(sllStrings.isEmpty());
        assertEquals(0, sllStrings.getSize());

        assertFalse(sllStudents.isEmpty());
        assertEquals(10, sllStudents.getSize());
    }

    @Test
    @Order(2)
    void find() {
        Student newS = new Student(45, "Jim");
        sllStudents.pushFront(newS);
        Iterator<Student> iterator = sllStudents.find(newS);
        assertEquals(iterator.next(), newS);
        Exception ex = assertThrows(NoSuchElementException.class, ()-> {
            Iterator<Student> it = sllStudents.find(new Student(22, "Alla"));
            it.next();
        });
        assertEquals("Illegal use of Iterator", ex.getMessage());
    }

    @Test
    @Order(3)
    void popFront() {
        for (int i = 0; i < 5; ++i) {
            sllStudents.popFront();
        }
        assertEquals(5, sllStudents.getSize());
    }

    @Test
    @Order(4)
    void newReverse() {
        int a = 0;
        int b = 0;
        int[] arr = IntStream.range(0, NUM_ELEMENTS).toArray();
        G_SLL<Integer> sll = new G_SLL<>();

        for(Integer in: arr) {
            sll.pushFront(in);
        }

        G_SLL<Integer> reversed = G_SLL.newReverse(sll);
        Iterator<Integer> reversedIter = reversed.iterator();
        Iterator<Integer> sllIter = sll.iterator();

        while(reversedIter.hasNext() && sllIter.hasNext()) {
            a = reversedIter.next();
            b = sllIter.next();
            assertEquals(NUM_ELEMENTS - 1, a + b);
        }
    }

    @Test
    @Order(5)
    void print() {
        sllStudents.printSLL();
    }

    @Test
    @Order(6)
    void testException() {
        Iterator<Student> iter = sllStudents.iterator();
        Exception ex = assertThrows(ConcurrentModificationException.class, ()-> {
           sllStudents.popFront();
           iter.next();
        });
        assertEquals("Illegal modification of G_SLL", ex.getMessage());
    }

    @Test
    @Order(7)
    void testMerge() {
        G_SLL<Student> other = new G_SLL<>();
        other.pushFront(new Student(34, "Ivan"));
        other.pushFront(new Student(44, "Michael"));
        other.pushFront(new Student(32, "Antonio"));

        G_SLL<Student> res = G_SLL.merge(sllStudents, other);
        assertEquals(13, res.getSize());


    }
}


class Student {
    private int age;
    private String name;

    Student(int age, String name) {
        this.age = age;
        this.name = name;
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


    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name + " " + this.age + " years old";
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name);
    }
}