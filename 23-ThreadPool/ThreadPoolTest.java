import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolTest {
    private static final int NUM_THREADS = 5;
    private ThreadPool pool = null;

    @BeforeEach
    public void startPool() {
        pool = new ThreadPool(NUM_THREADS);
    }

    @Test
    void submit() throws InterruptedException {
        pool.submit(Tasks::lowPriorityRunnable, Priority.LOW);
        pool.submit(Tasks::defPriorityCallable);
        pool.submit(Tasks::highPriorityRunnable, Priority.HIGH);
        pool.submit(Tasks::highPriorityCallable, Priority.HIGH);


        pool.shutDown();
        pool.awaitTermination();
    }

    @Test
    void testGet() throws ExecutionException, InterruptedException {
        Runnable runnerHigh = Tasks::highPriorityRunnable;
        Callable<String> defCallable = Tasks::defPriorityCallable;
        Runnable sleepy = Tasks::sleepingLowRunnable;
        Callable<Integer> sleepyCallable = Tasks::sleepingHighCallable;
        Callable<String> lowCallable = Tasks::highPriorityCallable;

        pool.submit(runnerHigh, Priority.HIGH);
        pool.submit(defCallable);
        pool.submit(sleepy, Priority.DEFAULT);
        Future<Integer> sleepTime = pool.submit(sleepyCallable, Priority.HIGH);
        Future<String> reversedString = pool.submit(lowCallable, Priority.LOW);

        assertFalse(sleepTime.isDone());
        assertFalse(sleepTime.isCancelled());
        Throwable awaitBeforeShutDown = assertThrows(RejectedExecutionException.class, () -> {
            pool.awaitTermination();
        });
        assertEquals("Calling awaitTermination before shutdown is not allowed", awaitBeforeShutDown.getMessage());

        pool.shutDown();
        pool.awaitTermination();

        assertTrue(sleepTime.isDone());
        assertFalse(sleepTime.isCancelled());
        assertEquals(5, sleepTime.get());
        assertEquals("elballaCytiroirPhgih olleH", reversedString.get());

    }

    @Test
    void testGetTimeOut() {
        Callable<String> bigSleepy = Tasks::sleepLover;
        Future<String> fut = pool.submit(bigSleepy);

        assertFalse(fut.isDone());
        assertFalse(fut.isCancelled());
        Throwable getTimeOutException = assertThrows(TimeoutException.class, () -> {
            fut.get(2, TimeUnit.SECONDS);
        });
        assertEquals("Result waiting time is out", getTimeOutException.getMessage());
        pool.shutDown();
    }

    @Test
    void testFutureCancel() throws InterruptedException {
        ThreadPool singleThreadpool = new ThreadPool(1);
        singleThreadpool.submit(Tasks::sleepingHighCallable, Priority.HIGH);
        Future<String> fut = singleThreadpool.submit(Tasks::sleepLover, Priority.LOW);

        assertTrue(fut.cancel(false));
        assertTrue(fut.isCancelled());
        assertFalse(fut.isDone());
        singleThreadpool.shutDown();
        singleThreadpool.awaitTermination();
    }

    @Test
    void testPause() throws InterruptedException, ExecutionException {
        ThreadPool singleThreadpool = new ThreadPool(1);

        singleThreadpool.submit(Tasks::sleepLover, Priority.HIGH);
        Future<Integer> fut = singleThreadpool.submit(Tasks::sumIndexTask, Priority.LOW);
        singleThreadpool.pause();
        System.out.println("Pause for 5 sec");
        Thread.sleep(5000);
        assertFalse(fut.isDone());
        System.out.println("resume");
        singleThreadpool.resume();
        singleThreadpool.shutDown();
        singleThreadpool.awaitTermination();
        assertTrue(fut.isDone());
        assertEquals(10, fut.get());
    }

    @Test
    void setNumOfThreads() throws InterruptedException {
        assertEquals(5, pool.getNumOfThreads());
        pool.setNumOfThreads(7);
        Thread.sleep(1000);
        assertEquals(7, pool.getNumOfThreads());
        pool.setNumOfThreads(5);
        Thread.sleep(1000);
        assertEquals(5, pool.getNumOfThreads());
        pool.shutDown();
        pool.awaitTermination();
    }

    @Test
    void shutDown() throws InterruptedException {

        ThreadPool tp = new ThreadPool(5);
        tp.setNumOfThreads(8);
        tp.setNumOfThreads(4);
//        Thread.sleep(5000);
        Thread.sleep(1000);

        System.out.println(tp.getNumOfThreads());

        tp.shutDown();
        tp.awaitTermination();

    }
}

class Tasks {
    public static void lowPriorityRunnable() {
        System.out.println("Low Priority runnable started");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void highPriorityRunnable() {
        System.out.println("High priority Runnable started");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String defPriorityCallable() {
        System.out.println("def priority task started");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder("Hello defPriorityCallable");
        return sb.reverse().toString();
    }

    public static String highPriorityCallable() {
        System.out.println("high priority callable started");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder("Hello highPriorityCallable");
        return sb.reverse().toString();
    }

    public static Integer sleepingHighCallable() throws InterruptedException {
        System.out.println("Started high callable");
        Thread.sleep(5000);
        return 5;
    }

    public static void sleepingLowRunnable() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sleeping low Runnable slept for 5 sec");
    }

    public static String sleepLover() throws InterruptedException {
        System.out.println("sleepLover task running");
        Thread.sleep(3000);
        return "Task slept for 5 sec";
    }

    public static Integer sumIndexTask() {
        System.out.println("pauseTask task running");
        int res = 0;
        for (int i = 0; i < 5; ++i) {
            res += i;
        }
        return res;
    }


}