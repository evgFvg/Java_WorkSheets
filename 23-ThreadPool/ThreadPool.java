/************************************
 * Developer: Evgenii Feigin        *
 * Reviewer: Moshe Ben Shmuel       *
 * Date: 25.7.2023                  *
 ***********************************/

/* Project description
Implementation of Java built-in ThreadPoolExecutor.
 */

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool implements Executor {
    private final WaitableQueue<Task<?>> wq = new WaitableQueue<>();
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);
    private final Queue<Thread> pool = new ConcurrentLinkedQueue<>();
    private int numOfThreads = 0;
    private boolean isPaused = false;
    private final Semaphore pauseSem = new Semaphore(0);

    public ThreadPool(int numOfThreads) {
        initNewThreads(numOfThreads);
    }

    private void initNewThreads(int threadNum) {
        this.numOfThreads += threadNum;
        for (int i = 0; i < threadNum; ++i) {
            Thread t = new SingleThreadStarter();
            pool.add(t);
            t.start();
        }
    }

    private class SingleThreadStarter extends Thread {
        private boolean isAlive = true;

        @Override
        public void run() {
            Task<?> task;
            while (isAlive) {
                try {
                    task = wq.dequeue();
                    task.setThread(Thread.currentThread());
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread.interrupted();
            }
            if (!isShutdown.get()) {
                pool.remove(Thread.currentThread());
            }
        }
    }

    public Future<Void> submit(Runnable runnable, Priority priority) {
        return submit(runnableToCallable(runnable, null), priority);
    }

    public <T> Future<T> submit(Runnable runnable, Priority priority, T value) {
        return submit(runnableToCallable(runnable, value), priority);
    }

    public <T> Future<T> submit(Callable<T> callable) {
        return submit(callable, Priority.DEFAULT);
    }

    public <T> Future<T> submit(Callable<T> callable, Priority priority) {
        if (isShutdown.get()) {
            throw new RejectedExecutionException("Impossible to add a new task after shutdown");
        }
        if (null == callable) {
            throw new NullPointerException("Adding null Tasks are not allowed");
        }
        Task<T> newTask = new Task<>(callable, priority.getPriority());
        wq.enqueue(newTask);
        return newTask.future;
    }

    private <T> Callable<T> runnableToCallable(Runnable runnable, T t) {
        return () -> {
            runnable.run();
            return t;
        };
    }

    public void setNumOfThreads(int newNumOfThreads) {
        if (0 > newNumOfThreads) {
            throw new IllegalArgumentException("Negative num of threads is not allowed");
        }
        int threadDiff = newNumOfThreads - this.numOfThreads;
        if (0 < threadDiff) {
            initNewThreads(threadDiff);
        } else {
            removeThreads(-threadDiff, Priority.HIGH.getPriority() + 1);
        }
    }

    private void removeThreads(int numThreadsToRemove, int priorityVal) {
        this.numOfThreads -= numThreadsToRemove;
        while (numThreadsToRemove > 0) {
            wq.enqueue(new Task<>(runnableToCallable(ThreadPool::shutDownTask, null), priorityVal));
            --numThreadsToRemove;
        }
    }

    @Override
    public void execute(Runnable command) {
        submit(command, Priority.DEFAULT);
    }

    public void shutDown() {
        if(isPaused) {
            throw new IllegalStateException("Not allowed to shutdown after pause");
        }
        removeThreads(this.numOfThreads, Priority.LOW.getPriority() - 1);
        isShutdown.set(true);
    }

    private static void shutDownTask() {
        ((SingleThreadStarter) Thread.currentThread()).isAlive = false;
    }

    public void pause() {
        if (isShutdown.get()) {
            throw new RejectedExecutionException("Pause after shutdown is not allowed");
        }
        if (isPaused) {
            throw new UnsupportedOperationException("Double pausing is not allowed");
        }
        for (Thread ignored : pool) {
            wq.enqueue(new Task<>(runnableToCallable(this::pauseTask, null), Priority.HIGH.getPriority() + 1));
        }
        isPaused = true;
    }

    private void pauseTask() {
        try {
            pauseSem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        if (isPaused) {
            isPaused = false;
            pauseSem.release(this.numOfThreads);
        }
    }

    public void awaitTermination() throws InterruptedException {
        if (!isShutdown.get()) {
            throw new RejectedExecutionException("Calling awaitTermination before shutdown is not allowed");
        }
        for (Thread t : pool) {
                t.join();
        }
        pool.clear();
    }

    public int getNumOfThreads() {  //For testing only
        return this.numOfThreads;
    }


    private class Task<T> implements Runnable, Comparable<Task<T>> {
        private final int priorityValue;
        private final Callable<T> callable;
        private final CallableFuture future = new CallableFuture();
        private final AtomicReference<TaskState> taskState = new AtomicReference<>(TaskState.READY);
        private Thread currThread = null;
        private Exception threadException = null;
        private final ReentrantLock futureLock = new ReentrantLock();
        private final Condition futureCondition = futureLock.newCondition();

        Task(Callable<T> callable, int priority) {
            this.priorityValue = priority;
            this.callable = callable;
        }

        @Override
        public int compareTo(Task<T> other) {
            return Integer.compare(other.priorityValue, this.priorityValue);
        }

        @Override
        public void run() {
            try {
                taskState.set(TaskState.RUNNING);
                future.setFutureValue(callable.call());
            } catch (Exception e) {
                threadException = e;
            } finally {
                taskState.set(TaskState.DONE);
                futureLock.lock();
                futureCondition.signalAll();
                futureLock.unlock();
            }
        }

        private void setThread(Thread currThread) {
            this.currThread = currThread;
        }

        private class CallableFuture implements Future<T> {
            private T retVal;

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                if (taskState.get() == TaskState.READY) {
                    if (wq.remove(Task.this)) {
                        taskState.set(TaskState.CANCELLED);
                        return true;
                    }
                }
                futureLock.lock();
                if (mayInterruptIfRunning && !isDone() && taskState.get() != TaskState.CANCELLED) {
                    currThread.interrupt();
                    taskState.set(TaskState.CANCELLED);
                }
                futureLock.unlock();
                return isCancelled();
            }

            private void setFutureValue(T value) {
                this.retVal = value;
            }

            @Override
            public boolean isCancelled() {
                return taskState.get() == TaskState.CANCELLED;
            }

            @Override
            public boolean isDone() {
                return taskState.get() == TaskState.DONE;
            }

            @Override
            public T get() throws InterruptedException, ExecutionException {
                futureLock.lock();
                throwExceptionIfCancelled();
                while (taskState.get() != TaskState.DONE) {
                    futureCondition.await();
                }
                throwExceptionIfCancelled();
                throwExceptionIfGotIt();
                futureLock.unlock();
                return retVal;
            }

            private void throwExceptionIfCancelled() {
                if (taskState.get() == TaskState.CANCELLED) {
                    throw new CancellationException("Can not retrieve value of the cancelled task");
                }
            }

            private void throwExceptionIfGotIt() throws ExecutionException {
                if (null != threadException) {
                    throw new ExecutionException(threadException);
                }
            }

            @Override
            public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                timeout = unit.toNanos(timeout);
                futureLock.lock();
                throwExceptionIfCancelled();
                while (taskState.get() != TaskState.DONE && timeout > 0) {
                    timeout = futureCondition.awaitNanos(timeout);
                }
                throwExceptionIfCancelled();
                if (timeout <= 0) {
                    throw new TimeoutException("Result waiting time is out");
                }
                throwExceptionIfGotIt();
                futureLock.unlock();
                return retVal;
            }
        }

        private enum TaskState {
            READY,
            CANCELLED,
            RUNNING,
            DONE
        }
    }

}




