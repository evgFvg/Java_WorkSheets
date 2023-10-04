import java.util.concurrent.atomic.AtomicInteger;

public class Exe3A_PingPongAtomic {
    private static final PingPong pp = new PingPong();

    public static void main(String[] args) throws InterruptedException {
        Thread prod = new Thread(pp::produce);
        Thread cons = new Thread(pp::consume);

        prod.start();
        cons.start();

        prod.join();
        cons.join();
    }
}


class PingPong {
    private final AtomicInteger counter = new AtomicInteger(20);

    public void produce() {
        while (counter.get() > 0) {
            if ((counter.get() % 2) == 0) {
                System.out.println("Ping");
                counter.decrementAndGet();
            }
        }
    }

    public void consume() {
        while (counter.get() > 0 ) {
            if((counter.get() % 2) != 0) {
                System.out.println("\t\tPong");
                counter.decrementAndGet();
            }
        }
    }
}
