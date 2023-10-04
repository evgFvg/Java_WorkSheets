/*
  Dev. : Evgenii
  Date 11.07.2023
  Reviewer: Or
 */
public class Exe1_CreateThreads {
    public static void main(String[] args) throws InterruptedException {
        Thread producer = new Thread(new Producer());
        Thread consumer = new Consumer();
        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

}

class Producer implements Runnable {
    @Override
    public void run() {
        String[] arr = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (int i = 0; i < 5; i++) {
            System.out.println("Hello from Producer " + arr[i]);
        }
    }
}

class Consumer extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Hello from Consumer " + i);
        }
    }
}
