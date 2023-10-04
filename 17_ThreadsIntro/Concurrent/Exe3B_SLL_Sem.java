import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Exe3B_SLL_Sem {
    private static final SLLSem spp = new SLLSem();

    public static void main(String[] args) {
        Thread prod1 = new Thread(spp::produce);
        Thread prod2 = new Thread(spp::produce);
        Thread cons1 = new Thread(spp::consume);
        Thread cons2 = new Thread(spp::consume);

        List<Thread> threadArr = List.of(prod1, prod2, cons1, cons2);

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

class SLLSem {
    private final List<String> buff = new LinkedList<>();
    private final AtomicInteger consCounter = new AtomicInteger(20);
    private final AtomicInteger prodCounter = new AtomicInteger(20);
    private final Semaphore sem = new Semaphore(0);


    public void produce() {
        synchronized (this) {
            while (prodCounter.get() > 0) {
                buff.add("\t\tPong" + prodCounter);
                System.out.println("Ping" + prodCounter + " added");
                prodCounter.decrementAndGet();
                sem.release();
            }

        }
    }

    public void consume() {
        synchronized (this) {
            try {
                sem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (consCounter.get() > 0) {
                System.out.println(buff.remove(0) + " released");
                consCounter.decrementAndGet();
            }
        }
    }
}

