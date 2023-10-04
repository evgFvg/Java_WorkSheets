import java.util.concurrent.Semaphore;

public class Exe3A_PingPongSemaphore {
    private static final SemPingPong spp = new SemPingPong();

    public static void main(String[] args) {
        Thread prod = new Thread(spp::produce);
        Thread cons = new Thread(spp::consume);

        prod.start();
        cons.start();

        try {
            prod.join();
            cons.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}


class SemPingPong {
    private static final Semaphore semProd = new Semaphore(1);
    private static final Semaphore semCons = new Semaphore(0);
    private static int counter = 20;

    public void produce()  {
        while (counter != 0) {
            try {
                semProd.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Ping");
            counter--;
            semCons.release();
        }
    }

    public void consume()  {
        while (counter != 0) {
            try {
                semCons.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\t\tPong");
            semProd.release();
        }
    }
}
