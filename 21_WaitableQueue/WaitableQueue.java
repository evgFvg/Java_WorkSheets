/****************************************************************************
 *  Developer: Evgenii                                                      *
 *  Reviewer: Aviv                                                          *
 *  Date: 17.07.2023                                                        *
 ***************************************************************************/
/*Simple implementation of the PriorityBlockingQueue of Java built-in*/

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeoutException;

public class WaitableQueue<E> {
    private final PriorityQueue<E> pq;
    private final Object monitor = new Object();

    public WaitableQueue() {
        pq = new PriorityQueue<>();
    }

    public WaitableQueue(Comparator<E> comparator) {
        pq = new PriorityQueue<>(comparator);
    }

    /*why built-an throws ClassCastException*/
    public boolean enqueue(E e) throws NullPointerException  {
        boolean res = false;
        synchronized (monitor) {
            res = pq.add(e);
            monitor.notifyAll();
        }
        return res;
    }


    public E dequeue() throws InterruptedException {
        synchronized (monitor) {
            while (pq.isEmpty()) {
                monitor.wait();
            }
            return  pq.poll();
        }
    }

    public E dequeue(long timeout) throws TimeoutException, InterruptedException  {
        synchronized (monitor) {
            long expectedEndTime =  System.currentTimeMillis() + timeout;
            while (pq.isEmpty() && timeout > 0) {
                monitor.wait(timeout);
                timeout = expectedEndTime - System.currentTimeMillis();
            }
            if(pq.isEmpty()) {
                throw new TimeoutException("Waiting time out");
            }
            return pq.poll();
        }
    }

    public boolean remove(Object o) {
        synchronized (monitor) {
            return pq.remove(o);
        }
    }
    public int size() {
        return pq.size();
    }
}
