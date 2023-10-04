import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Exe3B_SLL_Simple {
    private static final SLL spp = new SLL();

    public static void main(String[] args) {
        Thread prod1 = new Thread(spp::produce);
        Thread cons1 = new Thread(spp::consume);
        Thread cons2 = new Thread(spp::consume);
        Thread cons3 = new Thread(spp::consume);

        List<Thread> threadArr = List.of(prod1, cons1, cons2, cons3);

        for(Thread t: threadArr) {
            t.start();
        }

        try {
            for(Thread t: threadArr) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}


class SLL {
    private static final List<String> buff = new ArrayList<>();
    private static final AtomicInteger counter = new AtomicInteger(10);

    public synchronized void produce() {
        while (counter.get() > 0) {
            while(!buff.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Pingooo");
            counter.decrementAndGet();
            buff.add("\t\tPongoooo");
            notifyAll();
        }
    }

    public synchronized void consume()  {
        while (counter.get() > 0) {
            while(buff.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(buff.remove(0));
            notifyAll();
        }

    }
}

