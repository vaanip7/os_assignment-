import java.util.concurrent.Semaphore;

public class DiningPhilosophers {

    static final int N = 5;

    static Semaphore mutex = new Semaphore(1);
    static Semaphore[] chopstick = new Semaphore[N];

    static class Philosopher extends Thread {

        int i;

        Philosopher(int i) {
            this.i = i;
        }

        public void run() {

            try {
                System.out.println("Philosopher " + i + " is thinking");

                mutex.acquire();

                if (i % 2 == 0) {
                    chopstick[i].acquire();
                    chopstick[(i + 1) % N].acquire();
                }
                else {
                    chopstick[(i + 1) % N].acquire();
                    chopstick[i].acquire();
                }

                mutex.release();

                System.out.println("Philosopher " + i + " is eating");

                Thread.sleep(1000);

                chopstick[i].release();
                chopstick[(i + 1) % N].release();

                System.out.println("Philosopher " + i + " finished eating");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < N; i++) {
            chopstick[i] = new Semaphore(1);
        }

        Philosopher[] p = new Philosopher[N];

        for (int i = 0; i < N; i++) {
            p[i] = new Philosopher(i);
            p[i].start();
        }

        for (int i = 0; i < N; i++) {
            p[i].join();
        }
    }
}