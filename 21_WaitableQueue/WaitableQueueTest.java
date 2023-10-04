import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class WaitableQueueTest {

    @Test
    void enqueueDequeue() throws InterruptedException {
        WaitableQueue<Student> studQueue = new WaitableQueue<>(Student::comparator);
        ThreadInstance th = new ThreadInstance(studQueue);
        Thread prod1 = new Thread(th::produce);
        Thread prod2 = new Thread(th::produce);

        prod1.start();
        prod2.start();

        try {
            prod1.join();
            prod2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(20, studQueue.size());


        Thread cons1 = new Thread(th::consumeUsingDequeue);
        Thread cons2 = new Thread(th::consumeUsingDequeue);

        cons1.start();
        cons2.start();

        Thread prod3 = new Thread(th::addOneStudent);
        Thread.sleep(3000);
        prod3.start();

        try {
            cons1.join();
            cons2.join();  //cons 2 is waiting for notify
//            prod3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, studQueue.size());
    }

    @Test
    void testRemove() {
        WaitableQueue<Student> studQueue = new WaitableQueue<>(Student::comparator);
        ThreadInstance th = new ThreadInstance(studQueue);
        Thread prod1 = new Thread(th::produce);

        prod1.start();

        try {
            prod1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(10, studQueue.size());

        Thread cons1 = new Thread(th::consumeUsingRemove);
        Thread cons2 = new Thread(th::consumeUsingRemove);

        cons1.start();
        cons2.start();

        try {
            cons1.join();
            cons2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(0, studQueue.size());
    }

    @Test
    void EmptyDequeueTimeOut() {
        WaitableQueue<Student> studQueue = new WaitableQueue<>(Student::comparator);
        assertThrows(TimeoutException.class, () -> studQueue.dequeue(3000));
    }
}

class Student implements Comparable<Student> {
    private final String name;
    private final int ID;

    public Student(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.ID, other.getID());
    }

    public static int comparator(Student s1, Student s2) {
        return s1.ID - s2.ID;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", ID=" + ID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return ID == student.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}

class ThreadInstance {
    private final List<Student> school = List.of(
                    new Student("Alex", 1),
                    new Student("Noam", 23),
                    new Student("Helen", 13),
                    new Student("Kate", 41),
                    new Student("Gilbert", 35),
                    new Student("Nick", 26),
                    new Student("Ben", 17),
                    new Student("Bill", 8),
                    new Student("Jim", 9),
                    new Student("Sindy", 10));
    private final WaitableQueue<Student> studQueue;
    ThreadInstance(WaitableQueue<Student> studQueue) {
        this.studQueue = studQueue;
    }


    public void produce() {
        for(Student s: school) {
            studQueue.enqueue(s);
        }
    }

    public void addOneStudent() {
            System.out.println("Adding last student to the queue");
            studQueue.enqueue(new Student("Ivan", 100));
    }

    public void consumeUsingDequeue()  {
        while (studQueue.size() != 0) {
            try {
                studQueue.dequeue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void consumeUsingRemove()  {
        Student[] arr = school.toArray(new Student[0]);
        boolean res = true;
        for(int i = 0; i < arr.length && res; i++) {
            res = studQueue.remove(arr[i]);
        }
    }
}
