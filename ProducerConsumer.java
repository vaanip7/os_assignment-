import java.util.concurrent.Semaphore;

public class ProducerConsumer {
    static int[] buffer = new int[5];
    static int in = 0, out = 0;
    static Semaphore empty = new Semaphore(5);
    static Semaphore full = new Semaphore(0);
    static Semaphore mutex = new Semaphore(1);

    static class Producer extends Thread {
        public void run() {
            for (int item = 1; item <= 10; item++) {
                try {
                    empty.acquire();
                    mutex.acquire();
                    buffer[in] = item;
                    System.out.println("Produced: " + item);
                    in = (in + 1) % 5;
                    mutex.release();
                    full.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    static class Consumer extends Thread {
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    full.acquire();
                    mutex.acquire();
                    int item = buffer[out];
                    System.out.println("Consumed: " + item);
                    out = (out + 1) % 5;
                    mutex.release();
                    empty.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Producer p = new Producer();
        Consumer c = new Consumer();
        p.start();
        c.start();
        p.join();
        c.join();
    }
}