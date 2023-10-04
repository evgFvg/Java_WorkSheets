import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Exe2 {
    private static int nExecutions = 5;
    private static final int interval = 2;
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Using single thread pool");
        ExecutorService singleExec = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            singleExec.submit(RunFunctionFactory::printThreadInfo);
        }
        singleExec.shutdown();
        Thread.sleep(2000);


        System.out.println("Using 3-thread pool");
        ExecutorService threeThreadExecutor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            threeThreadExecutor.submit(RunFunctionFactory::printThreadInfo);
        }
        threeThreadExecutor.shutdown();
        Thread.sleep(2000);

        System.out.println("Using cashed pool");
        ExecutorService cashedExec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            cashedExec.submit(RunFunctionFactory::printThreadInfo);
        }
        cashedExec.shutdown();
        Thread.sleep(2000);


        System.out.println("Using scheduled pool");
        ScheduledExecutorService schExecutor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            if(nExecutions > 0) {
                RunFunctionFactory.printThreadInfo();
                System.out.println("nExecutions val is " + nExecutions);
                nExecutions--;
            }else {
                schExecutor.shutdown();
            }
        };

        schExecutor.scheduleAtFixedRate(task, 0, interval, TimeUnit.SECONDS);
    }
}


class RunFunctionFactory {
    static void printThreadInfo() {
        System.out.println("thread " + Thread.currentThread().getName() + Thread.currentThread().threadId());
    }
}
