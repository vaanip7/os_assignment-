import java.util.concurrent.Semaphore; 
 
public class ReaderWriter { 
 
    static Semaphore mutex = new Semaphore(1); 
    static Semaphore wrt = new Semaphore(1); 
    static int readCount = 0; 
    static int data = 0; 
 
    static class Reader extends Thread { 
        int id; 
        Reader(int id) { 
            this.id = id; 
        } 
        public void run() { 
            try { 
                mutex.acquire(); 
                readCount++; 
 
                if (readCount == 1) 
                    wrt.acquire(); 
                mutex.release(); 
                System.out.println("Reader " + id + " is reading data = " + data); 
                Thread.sleep(500); 
                mutex.acquire(); 
                readCount--; 
 
                if (readCount == 0) 
                    wrt.release(); 
                mutex.release(); 
            } catch (InterruptedException e) { 
                Thread.currentThread().interrupt(); 
            } 
        } 
    } 
    static class Writer extends Thread { 
        int id; 
 
        Writer(int id) { 
            this.id = id; 
        } 
 
        public void run() { 
            try { 
                wrt.acquire(); 
                data++; 
                System.out.println("Writer " + id + " wrote data = " + data); 
                Thread.sleep(500); 
                wrt.release(); 
            } catch (InterruptedException e) { 
                Thread.currentThread().interrupt(); 
            } 
        } 
    } 
    public static void main(String[] args) throws InterruptedException { 
        Thread r1 = new Reader(1); 
        Thread r2 = new Reader(2); 
        Thread r3 = new Reader(3); 
        Thread w1 = new Writer(1); 
        Thread w2 = new Writer(2); 
        r1.start(); 
        w1.start(); 
        r2.start(); 
        r3.start(); 
        w2.start(); 
 
        r1.join(); 
        r2.join(); 
        r3.join(); 
 
        w1.join(); 
        w2.join(); 
    } 
}