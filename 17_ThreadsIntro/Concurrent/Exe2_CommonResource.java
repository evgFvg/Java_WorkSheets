import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Exe2_CommonResource {
    public static void main(String[] args)  {
        NoSynchronization ns = new NoSynchronization();
        ns.launchThreads();
        SynchronizedMethod sm = new SynchronizedMethod();
        sm.launchThreads();
        SynchronizedBlock sb = new SynchronizedBlock();
        sb.launchThreads();
        AtomicVar av = new AtomicVar();
        av.launchThreads();
        Reentrant re = new Reentrant();
        re.launchThreads();
    }

}

class NoSynchronization {
    static private final int MAX_COUNTER = 10_000_000;
    private static int global = 0;

    public void printValue() {
        System.out.println("Global val is " + global);
    }

    public void launchThreads() {
        Thread th1 = new Thread(() -> {
            for (int i = 0; i < MAX_COUNTER; ++i) {
                ++global;
            }
        });
        Thread th2 = new Thread(() -> {
            for (int i = 0; i < MAX_COUNTER; ++i) {
                ++global;
            }
        });

        long start = System.nanoTime();
        th1.start();
        th2.start();

        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long end = System.nanoTime();
        System.out.println("The runTime without synchronization is " + (end - start) / 1_000_000 + " m_seconds");
        printValue();
    }
}

class SynchronizedMethod {
    static private final int MAX_COUNTER = 10_000_000;
    private static int global = 0;

    public static void printValue() {
        System.out.println("Global val is " + global);
    }

    public void launchThreads() {
        Thread th1 = new Thread(SynchronizedMethod::incrementGlobalVar);
        Thread th2 = new Thread(SynchronizedMethod::incrementGlobalVar);

        long start = System.nanoTime();
        th1.start();
        th2.start();

        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();

        System.out.println("The runTime with synchro method is " + (end - start) / 1_000_000 + " m_seconds");
        printValue();
    }

    private static synchronized void incrementGlobalVar() {
        for (int i = 0; i < MAX_COUNTER; ++i) {
            ++global;
        }
    }
}


class SynchronizedBlock {
    static private final int MAX_COUNTER = 10_000_000;
    private int global = 0;
    private final Object monitor = new Object();

    public void printValue() {
        System.out.println("Global val is " + global);
    }

    public void launchThreads() {
        Thread th1 = new Thread(this::incrementGlobalVar);
        Thread th2 = new Thread(this::incrementGlobalVar);

        long start = System.nanoTime();
        th1.start();
        th2.start();
        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("The runTime with synchro block is " + (end - start) / 1_000_000 + " m_seconds");
        printValue();
    }

    private void incrementGlobalVar() {
        synchronized (monitor) {
            for (int i = 0; i < MAX_COUNTER; ++i) {
                ++global;
            }
        }
    }
}



class AtomicVar {
    private static final int MAX_COUNTER = 10_000_000;
    private final AtomicInteger aInt = new AtomicInteger(0);

    public void printValue() {
        System.out.println("Global atomic val is " + aInt);
    }

    public void launchThreads() {
        Thread th1 = new Thread(this::incrementAtomic);
        Thread th2 = new Thread(this::incrementAtomic);

        long start = System.nanoTime();
        th1.start();
        th2.start();
        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("The runTime with atomic var is " + (end - start) / 1_000_000 + " m_seconds");
        printValue();
    }

    private  void incrementAtomic() {
        for (int i = 0; i < MAX_COUNTER; ++i) {
            aInt.incrementAndGet();
        }
    }
}




class Reentrant {
    static private final int MAX_COUNTER = 10_000_000;
    private final ReentrantLock lock = new ReentrantLock();
    private static int global = 0;

    public void printValue() {
        System.out.println("Via Reentrant lock val is " + global);
    }

    public void launchThreads() {
        Thread th1 = new Thread(() -> {
            lock.lock();
            for (int i = 0; i < MAX_COUNTER; ++i) {
                global++;
            }
            lock.unlock();
        });


        Thread th2 = new Thread(() -> {
            lock.lock();
            for (int i = 0; i < MAX_COUNTER; ++i) {
                global++;
            }
            lock.unlock();
        });

        long start = System.nanoTime();
        th1.start();
        th2.start();
        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("The runTime with Reentrant lock is " + (end - start) / 1_000_000 + " m_seconds");
        printValue();
    }
}







