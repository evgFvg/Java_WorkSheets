import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.*;

public class Exe1 {


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Thread t = new Thread(ThreadInstance::printCurrentTime);
        t.start();
        t.join();
        /*OR*/
        ExecutorService ex = Executors.newSingleThreadExecutor();

        ex.execute(ThreadInstance::printCurrentTime);
        ex.execute(ThreadInstance::sleepOneMIn);
        ex.awaitTermination(5, TimeUnit.SECONDS);


        Future<String> future = ex.submit(ThreadInstance::sleepRetVal);
        System.out.println("Is thread done ? : " + future.isDone());
        boolean res =  future.cancel(true);
        Thread.sleep(2000);
        System.out.println("Is success on cancelling sleep thread ? : " + res);
        System.out.println("Is thread cancelled ? : " + future.isCancelled());
        System.out.println("Is thread done ? : " + future.isDone());
        ex.shutdown();

        }


}





class ThreadInstance {
    public static void printCurrentTime() {
        System.out.println("Current time is " + LocalTime.now());
    }
    public static void sleepOneMIn() {
        System.out.println("Thread #2 start's sleeping");
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
        System.out.println("Sleep of thread #2 is over");
    }

    public static String sleepRetVal() {
        LocalTime start = LocalTime.now();
        try {

            System.out.println("Thread #3 start's sleeping");
            Thread.sleep(5_000);
            System.out.println("Sleep is over");
        } catch (InterruptedException e) {
            System.out.println("Sleep of thread #3 was interrupted");
            e.printStackTrace();
        }
        LocalTime end = LocalTime.now();
        Duration d = Duration.between(start, end);
        Long mins =d.toMillis();

        return "I slept " + mins;
    }
}
